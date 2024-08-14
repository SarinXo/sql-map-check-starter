package ilya.starter.sqlcheck.verifyer.impl;

import ilya.starter.sqlcheck.exception.model.SqlMismatchFieldsNamesContainer;
import ilya.starter.sqlcheck.model.Pair;
import ilya.starter.sqlcheck.userapi.SqlSkipTypeCheck;
import ilya.starter.sqlcheck.userapi.SqlBind;
import ilya.starter.sqlcheck.userapi.SqlIdType;
import ilya.starter.sqlcheck.exception.SqlBindingAppException;
import ilya.starter.sqlcheck.exception.model.SqlBindProblem;
import ilya.starter.sqlcheck.exception.model.SqlMismatchFieldsTypesContainer;
import ilya.starter.sqlcheck.exception.model.SqlNotFoundFieldsContainer;
import ilya.starter.sqlcheck.exception.model.SqlTooManyFieldsContainer;
import ilya.starter.sqlcheck.verifyer.VerifySqlBinding;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleVerifySqlBinding implements VerifySqlBinding {
    private final List<SqlBindProblem> problems = new ArrayList<>();

    @Override
    public void verifyAll(Set<Class<?>> sqlViews) throws SqlBindingAppException {
        sqlViews.forEach(this::verify);
        throwIfExistsProblem();
    }

    @Override
    public void throwIfExistsProblem() throws SqlBindingAppException {
        if(problems.isEmpty())
            return;

        throw new SqlBindingAppException(problems);
    }

    @Override
    public void verify(Class<?> sqlView) throws SqlBindingAppException {
        final var entity = extractEntityClass(sqlView);
        final var entityFields = getFieldsFromClass(entity);
        final var sqlFields = getFieldsFromClass(sqlView);

        long entityFieldsSize = sizeWithoutSqlSkipCheck(entityFields);
        long sqlFieldsSize = sizeWithoutSqlSkipCheck(sqlFields);

        if(entityFieldsSize != sqlFieldsSize) {
            if(entityFieldsSize > sqlFieldsSize) {
                final var problem = haveNotMappedFields(entity, entityFields, sqlView, sqlFields);
                problems.add(problem);
            } else {
                final var problem = tooManyFields(entity, entityFields, sqlView, sqlFields);
                problems.add(problem);
            }
            return;
        }

        var problemWithMapping = checkCorrectFields(entity, entityFields, sqlView, sqlFields);
        if(problemWithMapping != null)
            problems.add(problemWithMapping);

        var problemWithNames = checkCorrectNames(entity, entityFields, sqlView, sqlFields);
        if(problemWithNames != null)
            problems.add(problemWithNames);

    }

    private long sizeWithoutSqlSkipCheck(Set<Field> fields) {
        return fields.stream()
                .filter(field -> !skipTypeCheck(field))
                .count();
    }

    @Nullable
    private SqlMismatchFieldsTypesContainer checkCorrectFields(Class<?> entity, Set<Field> entityFields,
                                                               Class<?> sqlView, Set<Field> sqlFields) {
        var commonFields = new HashSet<>(entityFields);
        commonFields.retainAll(sqlFields);

        entityFields.removeAll(commonFields);
        sqlFields.removeAll(commonFields);

        var mismatchFields = new HashMap< Field, Pair<Class<?>, Class<?>> >();
        mismatchFields.putAll(findFieldMismatches(entityFields, sqlFields));
        mismatchFields.putAll(findFieldMismatches(sqlFields, entityFields));

        return mismatchFields.isEmpty()
                ? null
                : new SqlMismatchFieldsTypesContainer(sqlView, entity, mismatchFields);
    }

    private SqlMismatchFieldsNamesContainer checkCorrectNames(Class<?> entity, Set<Field> entityFields,
                                                              Class<?> sqlView, Set<Field> sqlFields) {
        var entityFieldsNames = entityFields.stream()
                .filter(field -> !skipTypeCheck(field))
                .map(Field::getName)
                .collect(Collectors.toSet());
        var sqlFieldsNames = sqlFields.stream()
                .filter(field -> !skipTypeCheck(field))
                .map(Field::getName)
                .collect(Collectors.toSet());
        var copy = Set.copyOf(entityFieldsNames);
        entityFieldsNames.removeAll(sqlFieldsNames);
        sqlFieldsNames.removeAll(copy);

        var mismatchFields = new HashMap<Class<?>, Set<String>>();
        if(!entityFieldsNames.isEmpty())
            mismatchFields.put(entity, entityFieldsNames);
        if(!sqlFieldsNames.isEmpty())
            mismatchFields.put(sqlView, sqlFieldsNames);

        return mismatchFields.isEmpty()
                ? null
                : new SqlMismatchFieldsNamesContainer(mismatchFields);
    }

    private Map<Field, Pair<Class<?>, Class<?>>> findFieldMismatches(Set<Field> fields1, Set<Field> fields2) {
        Map<Field, Pair<Class<?>, Class<?>>> mismatches = new HashMap<>();

        for (Field field1 : fields1) {
            for (Field field2 : fields2) {
                if (!isIdenticalNames(field1, field2) ||
                        shouldSkipTypeCheck(field1, field2) ||
                        isEqualsTypes(field1, field2))
                    continue;

                Pair<Class<?>, Class<?>> classPair = new Pair<>(field2.getType(), field1.getType());
                mismatches.put(field1, classPair);
            }
        }

        return mismatches;
    }

    private boolean isIdenticalNames(Field f1, Field f2) {
        return f1.getName().equals(f2.getName());
    }

    private boolean shouldSkipTypeCheck(Field f1, Field f2) {
        return annotationChecker(f1, f2) || annotationChecker(f2, f1);
    }

    private boolean isEqualsTypes(Field f1, Field f2) {
        return f1.getType().getName().equals(f2.getType().getName());
    }

    private boolean annotationChecker(Field f1, Field f2) {
        return skipTypeCheck(f1) || correctSqlIdType(f1, f2);
    }

    private boolean skipTypeCheck(Field f) {
        return f.getAnnotation(SqlSkipTypeCheck.class) != null;
    }

    private boolean correctSqlIdType(Field f1, Field f2) {
        SqlIdType sqlIdType = f1.getAnnotation(SqlIdType.class);
        return sqlIdType != null && sqlIdType.value().getName().equals(f2.getType().getName());
    }

    private Class<?> extractEntityClass(Class<?> sqlView) {
        return sqlView.getAnnotation(SqlBind.class).value();
    }

    private Set<Field> getFieldsFromClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toSet());
    }

    private SqlTooManyFieldsContainer tooManyFields(Class<?> entity, Set<Field> entityFields,
                                                    Class<?> sqlView, Set<Field> sqlFields) {
        difference(sqlFields, entityFields);
        var sqlViewNotResolvedFields = sqlFields.stream().toList();
        return new SqlTooManyFieldsContainer(sqlView, entity, sqlViewNotResolvedFields);
    }

    private SqlNotFoundFieldsContainer haveNotMappedFields(Class<?> entity, Set<Field> entityFields,
                                                           Class<?> sqlView, Set<Field> sqlFields) {
        difference(entityFields, sqlFields);
        var sqlViewNotResolvedFields = entityFields.stream().toList();
        return new SqlNotFoundFieldsContainer(sqlView, entity, sqlViewNotResolvedFields);
    }

    private void difference(Set<Field> diminishable, Set<Field> subtracted) {
        var common = new HashSet<Field>();
        for (var f1 : diminishable) {
            for (var f2 : subtracted) {
                if(isIdenticalNames(f1, f2) || skipTypeCheck(f1) || skipTypeCheck(f2)) {
                    common.add(f1);
                }
            }
        }
        diminishable.removeAll(common);
    }
}

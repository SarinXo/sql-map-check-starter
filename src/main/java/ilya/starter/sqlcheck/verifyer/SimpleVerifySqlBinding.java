package ilya.starter.sqlcheck.verifyer;

import ilya.starter.sqlcheck.annotation.SqlBind;
import ilya.starter.sqlcheck.exception.SqlBindingAppException;
import ilya.starter.sqlcheck.exception.model.SqlBindProblem;
import ilya.starter.sqlcheck.exception.model.SqlNotFoundFieldsContainer;
import ilya.starter.sqlcheck.exception.model.SqlTooManyFieldsContainer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleVerifySqlBinding implements VerifySqlBinding{
    private final List<SqlBindProblem> problems = new ArrayList<>();

    @Override
    public void verify(Class<?> sqlView) throws SqlBindingAppException {
        final var entity = extractEntityClass(sqlView);
        final var entityFields = getFieldsFromClass(entity);
        final var sqlFields = getFieldsFromClass(sqlView);

        if(entityFields.size() != sqlFields.size()) {
            if(entityFields.size() > sqlFields.size()){
                final var problem = haveNotMappedFields(entity, entityFields, sqlView, sqlFields);
                problems.add(problem);
            } else {
                final var problem = tooManyFields(entity, entityFields, sqlView, sqlFields);
            }
            return;
        }



    }

    private SqlTooManyFieldsContainer tooManyFields(Class<?> entity, Set<Field> entityFields,
                                                    Class<?> sqlView, Set<Field> sqlFields) {
        return null;
    }

    private SqlNotFoundFieldsContainer haveNotMappedFields(Class<?> entity, Set<Field> entityFields,
                                                           Class<?> sql, Set<Field> sqlFields) {
        return null;
    }

    private Class<?> extractEntityClass(Class<?> sqlView) {
        return sqlView.getAnnotation(SqlBind.class).mappedClass();
    }

    private Set<Field> getFieldsFromClass(Class<?> clazz) {
        return Arrays.stream(clazz.getFields())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void verifyAll(Set<Class<?>> sqlViews) throws SqlBindingAppException {
        sqlViews.forEach(this::verify);
    }

    @Override
    public void throwIfExistsProblem() throws SqlBindingAppException {
        if(problems.isEmpty())
            return;

        throw new SqlBindingAppException(problems);
    }
}

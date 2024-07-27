package ilya.starter.sqlcheck.exception.model;


import ch.qos.logback.core.joran.sanity.Pair;

import java.lang.reflect.Field;
import java.util.Map;


public record SqlNotCorrectTypeContainer(Class<?> sqlDto,
                                         Class<?> entity,
                                         Map< Field, Pair<Class<?>, Class<?>> > mismatchFields) implements SqlBindProblem {
    @Override
    public String toMessage() {
        final var sb = new StringBuilder();
        sb.append("Field mismatch between sql view class '")
                .append(sqlDto.getName()).append("' and '").append(entity.getName()).append("'\n");
        for(final var entry : this.mismatchFields.entrySet()) {
            final var field = entry.getKey();
            final var clazz1 = entry.getValue().first;
            final var clazz2 = entry.getValue().second;
            sb.append("mismatched types for field'").append(field.getName()).append("'\n")
                    .append("\tone class have type '").append(clazz1.getName()).append("'\n")
                    .append("\tanother one class have type '").append(clazz2.getName()).append("'\n");
        }
        return sb.append("\n\n").toString();
    }
}

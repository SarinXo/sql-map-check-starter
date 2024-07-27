package ilya.starter.sqlcheck.exception.model;

import java.lang.reflect.Field;
import java.util.List;

public record SqlTooManyFieldsContainer(Class<?> sqlDto,
                                        Class<?> entity,
                                        List<Field> sqlViewNotResolvedFields) implements SqlBindProblem {

    @Override
    public String toMessage() {
        final var sb = new StringBuilder();
        sb.append("Field mismatch between sql view class '")
                .append(sqlDto.getName()).append("' and '").append(entity.getName()).append("'\n")
                .append("sql view have too many fields, that don't belong to an entity");
        for(final var field : this.sqlViewNotResolvedFields) {
            sb.append("'").append(field.getName()).append("'\n");
        }
        return sb.append("\n\n").toString();
    }
}

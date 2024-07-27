package ilya.starter.sqlcheck.exception.model;

import java.lang.reflect.Field;
import java.util.List;

public record SqlNotFoundFieldsContainer(Class<?> sqlDto,
                                         Class<?> entity,
                                         List<Field> entityNotResolvedFields) implements SqlBindProblem {

    @Override
    public String toMessage() {
        final var sb = new StringBuilder();
        sb.append("Field mismatch between sql view class '")
                .append(sqlDto.getName()).append("' and '").append(entity.getName()).append("'\n")
                .append("entity have this fields but sql view doesn't have ");
        for(final var field : this.entityNotResolvedFields) {
            sb.append("'").append(field.getName()).append("'\n");
        }
        return sb.append("\n\n").toString();
    }
}

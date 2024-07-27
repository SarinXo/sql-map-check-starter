package ilya.starter.sqlcheck.exception.model;

import java.lang.reflect.Field;
import java.util.List;

public record SqlMismatchFieldsContainer(Class<?> sqlDto,
                                         Class<?> entity,
                                         List<Field> mismatchFields) implements SqlBindProblem {
    @Override
    public String toMessage() {
        final var sb = new StringBuilder();
        sb.append("Field mismatch between sql view class '")
                .append(sqlDto.getName()).append("' and '").append(entity.getName()).append("'\n");
        for(final var field : this.mismatchFields) {
            sb.append("not resolved field '").append(field.getName()).append("'\n");
        }
        return sb.append("\n\n").toString();
    }
}

package ilya.starter.sqlcheck.exception.model;

import java.util.Map;
import java.util.Set;

public record SqlMismatchFieldsNamesContainer(Map<Class<?>, Set<String>> mismatchFields) implements SqlBindProblem {
    @Override
    public String toMessage() {
        final var sb = new StringBuilder();
        sb.append("Field mismatch between NAMES sql view class\n");
        for(final var entry : this.mismatchFields.entrySet()) {
            final var clazz = entry.getKey();
            final var fieldNames = entry.getValue();
            sb.append("\nmismatched NAMES for '").append(clazz.getName()).append("'\n")
                    .append("\tunresolved fields names: \n");
            for(final var name : fieldNames) {
                sb.append("\t\t'").append(name).append("'\n");
            }
        }
        return sb.append("\n\n").toString();
    }
}
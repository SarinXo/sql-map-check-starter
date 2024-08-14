package ilya.starter.sqlcheck.exception;

import ilya.starter.sqlcheck.exception.model.SqlBindProblem;
import jakarta.annotation.Nonnull;

import java.util.List;

public class SqlBindingAppException extends RuntimeException{
    private final List<SqlBindProblem> bindExceptions;

    public SqlBindingAppException(@Nonnull List<SqlBindProblem> bindExceptions) {
        super(getMessage(bindExceptions));
        this.bindExceptions = bindExceptions;
    }

    public SqlBindingAppException(Throwable cause, @Nonnull List<SqlBindProblem> bindExceptions) {
        super(getMessage(bindExceptions), cause);
        this.bindExceptions = bindExceptions;
    }

    public SqlBindingAppException(String message, List<SqlBindProblem> bindExceptions) {
        super(String.join(message, getMessage(bindExceptions)));
        this.bindExceptions = bindExceptions;
    }

    public SqlBindingAppException(String message, Throwable cause, List<SqlBindProblem> bindExceptions) {
        super(String.join(message, getMessage(bindExceptions)), cause);
        this.bindExceptions = bindExceptions;
    }

    private static String getMessage(@Nonnull List<SqlBindProblem> bindExceptions) {
        final var sb = new StringBuilder();
        sb.append("""
                \n\n
                ****************************
                APPLICATION SQL CHECK FAILED
                ****************************
                """);
        sb.append("\nError with Sql binding! Reasons:\n\n");
        bindExceptions.stream()
                .map(SqlBindProblem::toMessage)
                .forEach(sb::append);
        return sb.toString();
    }

}

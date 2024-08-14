package ilya.starter.sqlcheck.userapi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface SqlBind {
    /**
     * Сопоставляет класс аннотированный данной аннотацией
     * с классом ORM модели. Их поля будут проверяться
     * Например:
     * <pre>{@code
     * @Entity(name = "table")
     * public class Table {
     *     ...
     * }
     *
     * @SqlBind(Table.class)
     * public class SqlTable {
     *     ...
     * }
     * }</pre>
     *
     * @return класс ORM модели
     */
    Class<?> value();
}

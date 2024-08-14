package ilya.starter.sqlcheck.userapi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface SqlIdType {
    /**
     * Сопоставляет класс id к не соответствующему атрибуту sql.
     * Например:
     * <pre>{@code
     * @Entity(name = "table")
     * public class Table {
     *     ...
     *     @ManyToOne(fetch = FetchType.LAZY)
     *     @JoinColumn(name = Fields.A_ENTITY, nullable = false)
     *     private AnotherEntity entity;
     * }
     *
     * @SqlBind(Table.class)
     * public class SqlTable {
     *     ...
     *     @SqlIdType(AnotherEntity.class)
     *     private Integer entity;
     * }
     * }</pre>
     *
     * @return класс соответствующий атрибуту sql
     */
    Class<?> value();
}

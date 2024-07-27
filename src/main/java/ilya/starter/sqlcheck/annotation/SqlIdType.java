package ilya.starter.sqlcheck.annotation;

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
     *     @SqlIdType(Integer.class)
     *     @OneToMany(mappedBy = "mappedColumn")
     *     private AnotherEntity entity;
     * }
     *
     * @SqlBind(Table.class)
     * public class SqlTable {
     *     ...
     *     private Integer entity;
     * }
     * }</pre>
     * @return класс соответствующий атрибуту sql
     */
    Class<?> mappedClass();
}

package ilya.starter.sqlcheck.userapi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Используется для того, чтобы избежать проверки поля на тип.
 * Пример: типы дженерики от @MappedSuperClass, когда нельзя использовать аннотацию {@link SqlIdType}
 *
 * <pre>{@code
 * @Entity(name = "table")
 * public class Table {
 *     ...
 *     @SkipSqlTypeCheck
 *     @OneToMany(mappedBy = "mappedColumn")
 *     private List<AnotherEntity> entities;
 * }
 *
 * @SqlBind(Table.class)
 * public class SqlTable {
 *     ...
 *     //no 'entities' field in SQL table
 * }
 * }</pre>
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SqlSkipTypeCheck {
}

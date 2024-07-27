package ilya.starter.sqlcheck.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Используется для того, чтобы избежать проверки поля на тип.
 * Пример: типы дженерики от @MappedSuperClass, когда нельзя использовать аннотацию {@link SqlIdType}
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SkipTypeCheck {
}

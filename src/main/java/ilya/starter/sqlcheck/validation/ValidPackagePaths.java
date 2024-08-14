package ilya.starter.sqlcheck.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PackagePathValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidPackagePaths {
    String message() default "Invalid package path format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

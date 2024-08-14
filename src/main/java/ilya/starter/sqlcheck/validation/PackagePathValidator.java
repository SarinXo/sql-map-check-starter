package ilya.starter.sqlcheck.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

public class PackagePathValidator implements ConstraintValidator<ValidPackagePaths, List<String>> {
    private final static Logger log = LogManager.getLogger();
    private static final Pattern PACKAGE_PATH_PATTERN = Pattern.compile("([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+");

    @Override
    public boolean isValid(List<String> paths, ConstraintValidatorContext context) {
        if (paths == null) {
            log.warn("sql package for sql-map-check-starter doesn't specified");
            return true;
        }
        for (String path : paths) {
            if (!PACKAGE_PATH_PATTERN.matcher(path).matches()) {
                return false;
            }
        }
        return true;
    }
}

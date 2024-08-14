package ilya.starter.sqlcheck.configuration;

import ilya.starter.sqlcheck.validation.ValidPackagePaths;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "sqlcheck")
public class SqlCheckProperties {
    @ValidPackagePaths
    private final List<String> pathsToPackages;

    @ConstructorBinding
    public SqlCheckProperties(List<String> pathsToPackages) {
        this.pathsToPackages = pathsToPackages;
    }

    public List<String> getPathsToPackages() {
        return pathsToPackages;
    }
}

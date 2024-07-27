package ilya.starter.sqlcheck.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sqlcheck")
public class SqlCheckProperties {
    private List<String> pathsToPackages;

    public List<String> getPathsToPackages() {
        return pathsToPackages;
    }
}

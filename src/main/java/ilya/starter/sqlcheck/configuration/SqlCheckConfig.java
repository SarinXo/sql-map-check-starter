package ilya.starter.sqlcheck.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "ilya.starter.sqlcheck")
@ConditionalOnProperty(prefix = "sqlcheck", name = "enable")
public class SqlCheckConfig {

}



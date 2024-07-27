package ilya.starter.sqlcheck.configuration;

import ilya.starter.sqlcheck.verifyer.SimpleSqlViewLoader;
import ilya.starter.sqlcheck.verifyer.SqlViewLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
@ConfigurationPropertiesScan(basePackages = "ilya.starter.sqlcheck")
@ConditionalOnProperty(prefix = "sqlcheck", name = "enable")
public class SqlCheckConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean(SqlViewLoader.class)
    public SqlViewLoader sqlViewLoader(SqlCheckProperties properties){
        return new SimpleSqlViewLoader(properties.getPathsToPackages());
    }
}



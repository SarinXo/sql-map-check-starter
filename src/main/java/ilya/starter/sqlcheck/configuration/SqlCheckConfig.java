package ilya.starter.sqlcheck.configuration;

import ilya.starter.sqlcheck.verifyer.impl.SimpleSqlViewLoader;
import ilya.starter.sqlcheck.verifyer.impl.SimpleVerifySqlBinding;
import ilya.starter.sqlcheck.verifyer.SqlViewLoader;
import ilya.starter.sqlcheck.verifyer.VerifySqlBinding;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationPropertiesScan(basePackages = "ilya.starter.sqlcheck")
@EnableConfigurationProperties(SqlCheckProperties.class)
@ConditionalOnProperty(prefix = "sqlcheck", name = "enable", havingValue = "true")
public class SqlCheckConfig {

    @Bean
    @ConditionalOnMissingBean(SqlViewLoader.class)
    public SqlViewLoader sqlViewLoader(SqlCheckProperties properties){
        return new SimpleSqlViewLoader(properties.getPathsToPackages());
    }

    @Bean
    @ConditionalOnMissingBean(VerifySqlBinding.class)
    public VerifySqlBinding verifySqlBinding() {
        return new SimpleVerifySqlBinding();
    }

}



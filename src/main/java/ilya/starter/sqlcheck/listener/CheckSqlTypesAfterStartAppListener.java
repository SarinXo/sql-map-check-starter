package ilya.starter.sqlcheck.listener;

import ilya.starter.sqlcheck.verifyer.SqlViewLoader;
import ilya.starter.sqlcheck.verifyer.VerifySqlBinding;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CheckSqlTypesAfterStartAppListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ConfigurableListableBeanFactory beanFactory;

    public CheckSqlTypesAfterStartAppListener(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        var ctxt = event.getApplicationContext();
        var sqlViewClassesLoader = ctxt.getBean(SqlViewLoader.class);
        var sqlViewVerifier = ctxt.getBean(VerifySqlBinding.class);
        var classes = sqlViewClassesLoader.loadSqlViewClasses();
        sqlViewVerifier.verifyAll(classes);

        beanFactory.destroyBean(sqlViewClassesLoader);
        beanFactory.destroyBean(sqlViewVerifier);
        beanFactory.destroyBean(this);
    }

}

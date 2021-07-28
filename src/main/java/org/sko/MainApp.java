package org.sko;

import org.apache.camel.component.jpa.JpaComponent;
import org.apache.camel.component.metrics.messagehistory.MetricsMessageHistoryFactory;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.main.BaseMainSupport;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

/**
 * A Camel Application
 */
public class MainApp extends MainListenerSupport
{

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        final Main main = new Main();
        main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.addMainListener( new MainApp() );

        main.run(args);
    }

    @Override
    public void beforeConfigure( final BaseMainSupport main )
    {
        final MetricsRoutePolicyFactory metricsRoutePolicyFactory = new MetricsRoutePolicyFactory();
        metricsRoutePolicyFactory.setUseJmx( true );
        final MetricsMessageHistoryFactory messageHistoryFactory = new MetricsMessageHistoryFactory();
        messageHistoryFactory.setUseJmx( true );
        main.getCamelContext().addRoutePolicyFactory( metricsRoutePolicyFactory );
        main.getCamelContext().setMessageHistoryFactory( messageHistoryFactory );

        final HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();

        final EntityManagerFactory entityManagerFactory =
           hibernatePersistenceProvider.createEntityManagerFactory( "camel", Collections.emptyMap() );

        final JpaComponent component = new JpaComponent();
        component.setEntityManagerFactory( entityManagerFactory );
        component.setSharedEntityManager( true );
        component.setJoinTransaction( false );
        final JpaTransactionManager transactionManager = new JpaTransactionManager( entityManagerFactory );
        transactionManager.setPersistenceUnitName( "camel" );

        main.getCamelContext().getRegistry().bind( "transactionManager", transactionManager );
        main.getCamelContext().addComponent( "jpa", component );
    }
}

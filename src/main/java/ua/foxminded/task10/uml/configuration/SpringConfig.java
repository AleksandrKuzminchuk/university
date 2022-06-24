package ua.foxminded.task10.uml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
@PropertySource("application.properties")
public class SpringConfig {

    private final Environment environment;

    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        log.info("Create [DataSource]->");
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setSchema("public");
        dataSource.setDriverClassName(environment.getRequiredProperty("hibernate.driver_class"));
        dataSource.setUrl(environment.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUsername(environment.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(environment.getRequiredProperty("hibernate.connection.password"));
        dataSource.setSuppressClose(Boolean.parseBoolean(environment.getRequiredProperty("hibernate.connection.suppressClose")));
        log.info("Created [DataSource] -> SUCCESSFULLY");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        log.info("Create [sessionFactory]->");
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ua.foxminded.task10.uml");
        log.info("Create [properties]->");
        Properties properties = getProperties();
        log.info("Created [properties] -> SUCCESSFULLY");
        sessionFactory.setHibernateProperties(properties);
        log.info("Created [sessionFactory] -> SUCCESSFULLY");
        return sessionFactory;
    }

    private Properties getProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        hibernateProperties.put("hibernate.ejb.naming_strategy", environment.getRequiredProperty("hibernate.ejb.naming_strategy"));
        hibernateProperties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        hibernateProperties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return hibernateProperties;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        log.info("Create [transactionManager]->");
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        log.info("Created [transactionManager] -> SUCCESSFULLY");
        return transactionManager;
    }
}

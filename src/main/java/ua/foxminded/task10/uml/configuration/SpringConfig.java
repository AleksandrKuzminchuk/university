package ua.foxminded.task10.uml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ua.foxminded.task10.uml.util.PropertyManager;

import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
public class SpringConfig {

    @Bean
    public PropertyManager propertyManager() {
        return new PropertyManager("resources" + File.separator + "application.properties");
    }

    @Bean
    public DataSource dataSource() {
        log.info("Create [DataSource]->");
        PropertyManager propertyManager = propertyManager();
        String url = propertyManager.getProperty("url");
        String username = propertyManager.getProperty("username");
        String password = propertyManager.getProperty("password");
        String driver = propertyManager.getProperty("driver");
        boolean suppressClose = true;
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(url, username, password, suppressClose);
        dataSource.setSchema("public");
        dataSource.setDriverClassName(driver);
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
        hibernateProperties.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.getProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.getProperty("hibernate.format_sql", "true");
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

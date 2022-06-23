package ua.foxminded.task10.uml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ua.foxminded.task10.uml.exceptions.NotFoundException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
public class SpringConfig {

    @Bean
    public DataSource dataSource() {
        log.info("Create [InitialContext]->");
        InitialContext cxt = null;
        try {
            cxt = new InitialContext();
        } catch (NamingException e) {
            throw new NotFoundException("InitialContext not found!", e);
        }
        log.info("Created [InitialContext] -> SUCCESSFULLY");

        DataSource dataSource;
        try {
            log.info("Create [DataSource]->");
            log.info("Create [FINDING... lookup -> connection to DB]->");
            dataSource = (DataSource) cxt.lookup("java:/comp/env/jdbc/university");
            log.info("FOUND lookup -> connection to DB -> SUCCESSFULLY");
        } catch (NamingException e) {
            throw new NotFoundException("lookup not found!", e);
        }

        if (dataSource == null) {
            throw new NotFoundException("Data source not found!");
        }
        log.info("Created [DataSource] -> SUCCESSFULLY");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        log.info("Create [jdbcTemplate]->");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        log.info("Created [JdbcTemplate] -> SUCCESSFULLY");
        return jdbcTemplate;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        log.info("Create [sessionFactory]->");
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ua.foxminded.task10.uml");

        log.info("Create [hibernateProperties]->");
        Properties hibernateProperties = new Properties();
        hibernateProperties.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.getProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.getProperty("hibernate.format_sql", "true");
        log.info("Created [hibernateProperties] -> SUCCESSFULLY");

        sessionFactory.setHibernateProperties(hibernateProperties);

        log.info("Created [sessionFactory] -> SUCCESSFULLY");
        return sessionFactory;
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

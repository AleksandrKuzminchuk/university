package ua.foxminded.task10.uml.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    public static final String PROPERTIES_FILE = "db.properties";
    public static final String DRIVER = "db.driver";
    public static final String URL = "db.url";
    public static final String USER = "db.user";
    public static final String PASSWORD = "db.password";
    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);

    @Bean
    public PropertyManager getPropertyManager() {
        PropertyManager propertyManager = new PropertyManager(PROPERTIES_FILE);
        logger.info("[PropertyManager] -> created");
        return propertyManager;
    }

    @Bean
    public DataSource dataSource(PropertyManager propertyManager) throws ClassNotFoundException {
        String driver = propertyManager.getProperty(DRIVER);
        String url = propertyManager.getProperty(URL);
        String user = propertyManager.getProperty(USER);
        String password = propertyManager.getProperty(PASSWORD);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        logger.info("[DataSource] -> created");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("[JdbcTemplate] -> created");
        return jdbcTemplate;
    }

}

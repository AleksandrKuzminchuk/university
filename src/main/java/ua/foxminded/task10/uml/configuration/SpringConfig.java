package ua.foxminded.task10.uml.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan("ua.foxminded.task10.uml")
public class SpringConfig {
    public static final String PROPERTIES_FILE = "src/resources/db.properties";
    public static final String DRIVER = "db.driver";
    public static final String URL = "db.url";
    public static final String USER = "db.user";
    public static final String PASSWORD = "db.password";


    @Bean
    public PropertyManager getPropertyManager(){
        return new PropertyManager(PROPERTIES_FILE);
    }

    @Bean
    public DataSource dataSource(PropertyManager propertyManager){
        String driver = propertyManager.getProperty(DRIVER);
        String url = propertyManager.getProperty(URL);
        String user = propertyManager.getProperty(USER);
        String password = propertyManager.getProperty(PASSWORD);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/templates");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }




}

package ua.foxminded.task10.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UniversityApplication {

	private final Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(UniversityApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		log.info("Create [DataSource]->");
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
		dataSource.setSchema("public");
		dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
		dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
		dataSource.setSuppressClose(true);
		log.info("Created [DataSource] -> SUCCESSFULLY");
		return dataSource;
	}

//	@Bean
//	public LocalSessionFactoryBean sessionFactory() {
//		log.info("Create [sessionFactory]->");
//		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource());
//		sessionFactory.setPackagesToScan("ua.foxminded.task10.uml");
//		log.info("Create [properties]->");
//		Properties properties = getProperties();
//		log.info("Created [properties] -> SUCCESSFULLY");
//		sessionFactory.setHibernateProperties(properties);
//		log.info("Created [sessionFactory] -> SUCCESSFULLY");
//		return sessionFactory;
//	}

	private Properties getProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
		hibernateProperties.put("hibernate.ejb.naming_strategy", environment.getRequiredProperty("spring.jpa.hibernate.naming.implicit-strategy"));
		hibernateProperties.put("hibernate.show_sql", environment.getRequiredProperty("spring.jpa.properties.hibernate.show_sql"));
		hibernateProperties.put("hibernate.format_sql", environment.getRequiredProperty("spring.jpa.properties.hibernate.format_sql"));
		hibernateProperties.put("current_session_context_class", environment.getRequiredProperty("spring.jpa.properties.hibernate.current_session_context_class"));
		hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", environment.getRequiredProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults"));
		return hibernateProperties;
	}

//	@Bean
//	public HibernateTransactionManager transactionManager() {
//		log.info("Create [transactionManager]->");
//		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//		transactionManager.setSessionFactory(sessionFactory().getObject());
//		log.info("Created [transactionManager] -> SUCCESSFULLY");
//		return transactionManager;
//	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan("ua.foxminded.task10.uml.model");

		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactoryBean.setJpaProperties(getProperties());

		return entityManagerFactoryBean;
	}

	@Bean
	public PlatformTransactionManager transactionManager(){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
}

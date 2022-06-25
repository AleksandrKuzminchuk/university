package ua.foxminded.task10.uml.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Slf4j
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@ComponentScan(basePackages = "ua.foxminded.task10.uml")
@FieldDefaults(makeFinal = true ,level = AccessLevel.PRIVATE)
public class WebMvcConfig implements WebMvcConfigurer {

    ApplicationContext applicationContext;

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        log.info("Create [templateResolver]->");
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        log.info("Created [templateResolver] -> SUCCESSFULLY");
        return templateResolver;
    }

    @Bean
    public ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        log.info("Create [templateEngine]->");
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setEnableSpringELCompiler(true);
        log.info("Created [templateEngine] -> SUCCESSFULLY");
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver getThymeleafViewResolver() {
        log.info("Create [getThymeleafViewResolver]->");
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine(templateResolver()));
        log.info("Created [getThymeleafViewResolver] -> SUCCESSFULLY");
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        log.info("Create [configureDefaultServletHandling]->");
        configurer.enable();
        log.info("Created [configureDefaultServletHandling] -> SUCCESSFULLY");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Create [addResourceHandlers]->");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        log.info("Created [addResourceHandlers] -> SUCCESSFULLY");
    }
}

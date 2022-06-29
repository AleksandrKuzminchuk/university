package ua.foxminded.task10.uml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Slf4j
@Configuration
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class[] getRootConfigClasses() {
        log.info("Create [getRootConfigClasses]->");
        Class[] getRootConfigClasses = new Class[]{SpringConfig.class};
        log.info("Created [getRootConfigClasses] -> SUCCESSFULLY");
        return getRootConfigClasses;
    }

    @Override
    protected Class[] getServletConfigClasses() {
        log.info("Create [getServletConfigClasses]->");
        Class[] getServletConfigClasses = new Class[]{WebMvcConfig.class};
        log.info("Created [getServletConfigClasses] -> SUCCESSFULLY");
        return getServletConfigClasses;
    }

    @Override
    protected String[] getServletMappings() {
        log.info("Create [getServletMappings]->");
        String[] getServletMappings = new String[]{"/"};
        log.info("Created [getServletMappings] -> SUCCESSFULLY");
        return getServletMappings;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Create [onStartup]->");
        super.onStartup(servletContext);
        registerHiddenFieldFilter(servletContext);
        log.info("Created [onStartup] -> SUCCESSFULLY");
    }

    private void registerHiddenFieldFilter(ServletContext context){
        log.info("Create [registerHiddenFieldFilter]->");
        context.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
        log.info("Created [registerHiddenFieldFilter] -> SUCCESSFULLY");
    }
}

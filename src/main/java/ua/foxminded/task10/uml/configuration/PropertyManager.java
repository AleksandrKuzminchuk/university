package ua.foxminded.task10.uml.configuration;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

    public static final String PROPERTIES_FILE = "src/resources/db.properties";
    public static final String DRIVER = "db.driver";
    public static final String URL = "db.url";
    public static final String USER = "db.user";
    public static final String PASSWORD = "db.password";

    private final Properties properties;

    public PropertyManager(String propertiesPath){
        if (propertiesPath == null || propertiesPath.isEmpty()){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL_OR_EMPTY);
        }
        properties = new Properties();
        try{
            properties.load(new FileInputStream(propertiesPath));
        }catch (IOException e){
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public String getProperty(String propertyKey){
        return properties.getProperty(propertyKey);
    }
}

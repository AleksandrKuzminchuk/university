package ua.foxminded.task10.uml.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);
    private final Properties properties;

    public PropertyManager(String propertiesPath){
        if (propertiesPath == null || propertiesPath.isEmpty()){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL_OR_EMPTY);
        }
        properties = new Properties();
        try{
            properties.load(new FileInputStream(propertiesPath));
            logger.info("Properties from path '{}' successfully loaded", propertiesPath);
        }catch (IOException e){
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public String getProperty(String propertyKey){
        logger.info("getting property '{}'", propertyKey);
        return properties.getProperty(propertyKey);
    }
}

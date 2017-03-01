package ch.sebooom.dump1090.utils;

import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
import org.apache.commons.cli.MissingArgumentException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static ch.sebooom.dump1090.utils.PropertiesKey.*;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class Dump1090Properties {

    private Properties properties = new Properties();
    public final static String propertiesPath = "./config/application.properties";
    private final static Logger logger = Logger.getLogger(Dump1090Properties.class.getName());

    public static Dump1090Properties get() throws Dump1090PropertiesException {
        Dump1090Properties props = new Dump1090Properties();

        try {
            props.getProperties().load(new FileInputStream(propertiesPath));
            props.checkMandatory();
            props.checkDbProperties();
        } catch (IOException  | MissingArgumentException e) {
            throw new Dump1090PropertiesException(e);
        }

        return props;

    }

    private void checkDbProperties() throws Dump1090PropertiesException, MissingArgumentException {

        boolean propertiesMissing = false;

        String dbType = properties.getProperty(DB_TYPE.key());

        switch (dbType){
            case "mongodb":
                if(properties.get(MONGODB_HOST.key()) == null){
                    logMissingProperties(MONGODB_HOST);
                    propertiesMissing = true;
                }

                if(properties.get(MONGODB_PORT.key()) == null){
                    logMissingProperties(MONGODB_PORT);
                    propertiesMissing = true;
                }

                if(properties.get(MONGODB_DB.key()) == null){
                    logMissingProperties(MONGODB_DB);
                    propertiesMissing = true;
                }

                if(properties.get(MONGODB_COLLECTION.key()) == null){
                    logMissingProperties(MONGODB_COLLECTION);
                    propertiesMissing = true;
                }
            break;

            case "rethinkdb":
                if(properties.get(RETHINKDB_HOST.key()) == null){
                    logMissingProperties(RETHINKDB_HOST);
                    propertiesMissing = true;
                }

                if(properties.get(RETHINKDB_PORT.key()) == null){
                    logMissingProperties(RETHINKDB_PORT);
                    propertiesMissing = true;
                }

                if(properties.get(RETHINKDB_DB.key()) == null){
                    logMissingProperties(RETHINKDB_DB);
                    propertiesMissing = true;
                }

                if(properties.get(RETHINKDB_TABLE.key()) == null){
                    logMissingProperties(RETHINKDB_TABLE);
                    propertiesMissing = true;
                }

            break;

            default:
                throw new Dump1090PropertiesException(
                        String.format("dump1090.dbtype properties erroneus: %s",dbType));
        }

        if(propertiesMissing){
            logger.severe(JsonLog.technical(
                "At least one mandatory properties missing",
                    EventType.PROPERTIES,0));
            throw new MissingArgumentException("At least one properties missing, check log");
        }
    }

    private void logMissingProperties(PropertiesKey key) {
        logger.severe(JsonLog.technical(
                String.format("Property [%s] not found.",key.key()),
                EventType.PROPERTIES,0));
    }

    private void checkMandatory() throws MissingArgumentException {

        boolean propertiesMissing = false;

        if(properties.get(SERVER_PORT.key()) == null){
            logMissingProperties(SERVER_PORT);
            propertiesMissing = true;
        }

        if(properties.get(DUMP1090TCP_PORT.key()) == null){
            logMissingProperties(DUMP1090TCP_PORT);
            propertiesMissing = true;
        }

        if(properties.get(DUMP1090TCP_HOST.key()) == null){
            logMissingProperties(DUMP1090TCP_HOST);
            propertiesMissing = true;
        }

        if(properties.get(DB_TYPE.key()) == null){
            logMissingProperties(DB_TYPE);
            propertiesMissing = true;
        }

        if(propertiesMissing){
            logger.severe(JsonLog.technical(
                    "At least one mandatory properties missing",
                    EventType.PROPERTIES,0));
            throw new MissingArgumentException("At least one mandatory properties missing, check log");
        }
    }

    public Properties getProperties(){
        return this.properties;
    }

}

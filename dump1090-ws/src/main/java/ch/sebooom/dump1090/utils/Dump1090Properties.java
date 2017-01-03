package ch.sebooom.dump1090.utils;

import org.apache.commons.cli.MissingArgumentException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class Dump1090Properties {

    private Properties properties = new Properties();
    private static String propertiesPath = "./config/application.properties";
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

        String dbType = properties.getProperty("dump1090.dbtype");

        switch (dbType){
            case "mongodb":
                if(properties.get("mongodb.host") == null){
                    logger.severe("Property [mongodb.host] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("mongodb.port") == null){
                    logger.severe("Property [mongodb.port] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("mongodb.db") == null){
                    logger.severe("Property [mongodb.db] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("mongodb.collection") == null){
                    logger.severe("Property [mongodb.collection] not found.");
                    propertiesMissing = true;
                }
            break;

            case "rethinkdb":
                if(properties.get("rethinkdb.host") == null){
                    logger.severe("Property [rethinkdb.host] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("rethinkdb.port") == null){
                    logger.severe("Property [rethinkdb.port] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("rethinkdb.db") == null){
                    logger.severe("Property [rethinkdb.db] not found.");
                    propertiesMissing = true;
                }

                if(properties.get("rethinkdb.table") == null){
                    logger.severe("Property [rethinkdb.table] not found.");
                    propertiesMissing = true;
                }

            break;

            default:
                throw new Dump1090PropertiesException(
                        String.format("dump1090.dbtype properts erroneus: %s",dbType));
        }

        if(propertiesMissing){
            logger.severe("At least one mandatory properties missing");
            throw new MissingArgumentException("At least one properties missing, check log");
        }
    }

    private void checkMandatory() throws MissingArgumentException {

        boolean propertiesMissing = false;

        if(properties.get("server.port") == null){
            logger.severe("Property [server.port] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.tcp.port") == null){
            logger.severe("Property [dump1090.tcp.port] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.tcp.host") == null){
            logger.severe("Property [dump1090.tcp.host] not found.");
            propertiesMissing = true;
        }

        if(properties.get("dump1090.dbtype") == null){
            logger.severe("Property [dump1090.dbtype] not found.");
            propertiesMissing = true;
        }

        if(propertiesMissing){
            logger.severe("At least one mandatory properties missing");
            throw new MissingArgumentException("At least one mandatory properties missing, check log");
        }
    }

    public Properties getProperties(){
        return this.properties;
    }

}

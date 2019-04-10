package me.shamanov.resumedb.web.config;

import me.shamanov.resumedb.storage.SqlStorage;
import me.shamanov.resumedb.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Mike
 * Date: 09.04.2019
 */

public final class ResumeServletConfig {
    private static final String propFile = "config.properties";
    private static final Logger logger = Logger.getLogger(ResumeServletConfig.class.getName());
    private static Storage storage;

    static {
        try (InputStream is = ResumeServletConfig.class.getClassLoader().getResourceAsStream(propFile)) {
            Properties props = new Properties();
            props.load(is);

            storage = new SqlStorage(props);
//          storage = new XMLFileStorage("D:\\Java\\projects\\resumedb\\filestorage\\xml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while loading property file: " + propFile, e);
            throw new IllegalStateException("SqlStorage couldn't be instantiated! Check for correct settings in the configuration file: " + propFile, e);
        }
    }

    public static Storage getStorage() {
        if (storage == null) {
            throw new IllegalStateException("Storage has not been instantiated!");
        }
        return storage;
    }
}

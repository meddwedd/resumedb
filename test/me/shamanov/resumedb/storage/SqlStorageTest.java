package me.shamanov.resumedb.storage;

import me.shamanov.resumedb.web.config.ResumeServletConfig;

/**
 * Author: Mike
 * Date: 10.04.2019
 */

public class SqlStorageTest extends AbstractStorageTest {
    {
        storage = ResumeServletConfig.getStorage();
    }
}
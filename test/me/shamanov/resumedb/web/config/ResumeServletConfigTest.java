package me.shamanov.resumedb.web.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Mike
 * Date: 09.04.2019
 */

public class ResumeServletConfigTest {

    @Test
    public void testPropertiesLoad() {
        assertNotNull(ResumeServletConfig.getStorage());
    }
}
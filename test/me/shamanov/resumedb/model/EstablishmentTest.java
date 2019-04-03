package me.shamanov.resumedb.model;

import me.shamanov.resumedb.model.Establishment.Period;
import org.junit.Test;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

public class EstablishmentTest {

    /**
     * passes if input date doesn't match {@link Period#datePattern}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDatePatternInput() {
        Establishment.of("ЯрГУ", Period.of("01/01/99999", "01/01/2017", "Програмимист"));
    }
}
package me.shamanov.resumedb.model;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Mike
 * Date: 01.04.2019
 *
 * Used to store a list of {@link String} values for only text sections.
 */

public class MultipleTextHolder extends Holder<String> {
    private static long serialVersionUID = 1L;

    private MultipleTextHolder() {
        super();
    }

    private MultipleTextHolder(List<String> values) {
        super(values);
    }

    public static MultipleTextHolder from(String... values) {
        return from(Arrays.asList(values));
    }

    public static MultipleTextHolder from(List<String> values) {
        return new MultipleTextHolder(values);
    }
}

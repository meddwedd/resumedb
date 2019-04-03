package me.shamanov.resumedb.model;

import java.util.List;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

public class TextSection extends Section<String> {
    private static long serialVersionUID = 1L;

    private TextSection() {
        super();
    }

    private TextSection(String... values) {
        super(values);
    }

    private TextSection(List<String> values) {
        super(values);
    }

    public static TextSection from(String... values) {
        return new TextSection(values);
    }

    public static TextSection from(List<String> values) {
        return new TextSection(values);
    }
}

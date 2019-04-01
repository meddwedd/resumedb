package me.shamanov.resumebd.model;

import java.util.List;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

public class TextSection extends Section<String> {
    private static long serialVersionUID = 1L;

    public TextSection(String... values) {
        super(values);
    }

    public TextSection(List<String> values) {
        super(values);
    }
}

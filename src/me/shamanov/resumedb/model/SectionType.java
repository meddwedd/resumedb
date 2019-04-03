package me.shamanov.resumedb.model;

/**
 * Author: Mike
 * Date: 30.03.2019
 *
 * This enum is used as a part of {@code Resume} instance to distinguish section types in EnumMap collection.
 */

public enum SectionType {
    POSITION("Позиция"),
    ACCOMPLISHMENT("Достижения"),
    QUALITY("Личные качества"),
    EDUCATION("Образование"),
    EXPERIENCE("Опыт работы");

    String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }}

package me.shamanov.resumedb.model;

/**
 * Author: Mike
 * Date: 30.03.2019
 *
 * This enum is used as a part of {@code Resume} instance to distinguish contact types in EnumMap collection.
 */

public enum ContactType {
    EMAIL("E-mail"),
    MOBILE("Мобильный телефон"),
    PHONE("Домашний телефон"),
    SKYPE("Skype"),
    VK("ВКонтакте"),
    FACEBOOK("Facebook");

    String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

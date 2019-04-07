package me.shamanov.resumedb.model;

/**
 * Author: Mike
 * Date: 30.03.2019
 *
 * Used as a part of {@code Resume} instance to distinguish contact types in EnumMap collection.
 */

public enum ContactType {

    EMAIL("E-mail", InputType.EMAIL),
    MOBILE("Мобильный телефон", InputType.TEL),
    PHONE("Домашний телефон", InputType.TEL),
    SKYPE("Skype", InputType.TEXT),
    HOMEPAGE("Домашняя страница", InputType.URL),
    VK("ВКонтакте", InputType.URL),
    FACEBOOK("Facebook", InputType.URL);

    /**
     * used by edit.jsp page to fill types of the input fields.
     */
    private enum InputType {
        TEXT, EMAIL, TEL, URL
    }

    private String title;
    private InputType inputType;

    ContactType(String title, InputType inputType) {
        this.title = title;
        this.inputType = inputType;
    }

    public String getTitle() {
        return title;
    }

    public InputType getInputType() {
        return inputType;
    }

    public String toLink(String value) {
        switch (this) {
            case EMAIL:
                return "<a href=\"mailto:" + value + "\">" + value + "</a>";
            case MOBILE:
            case PHONE:
                return value;
            case SKYPE:
                return "<a href=\"skype:" + value + "\">" + value + "</a>";
            case HOMEPAGE:
            case VK:
            case FACEBOOK:
                return "<a href=\""+ value + "\">" + value + "</a>";
            default:
                throw new EnumConstantNotPresentException(ContactType.class, this.toString());
        }
    }
}

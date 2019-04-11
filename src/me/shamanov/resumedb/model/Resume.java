package me.shamanov.resumedb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: Mike
 * Date: 30.03.2019
 * <p>
 * This class represents a resume with different fields and sections to form a description of professional skills, experience
 * and education of a concrete person. Each resume has its unique id (randomly assigned on instantiation) to be saved within a storage representative
 * e.g. database, filesystem etc.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;

    //used when a new Resume is getting created when a client opens edit.jsp page
    public static final transient Resume EMPTY = new Resume();

    static {
        //filling EMPTY instance with empty contact values.
        for (ContactType contactType : ContactType.values()) {
            EMPTY.getContacts().put(contactType, "");
        }

        //filling EMPTY instance with empty section values.
        for (SectionType sectionType : SectionType.values()) {
            EMPTY.getSections().compute(sectionType, (k, v) -> {
                if (k == SectionType.EDUCATION || k == SectionType.EXPERIENCE) {
                    return EstablishmentHolder.from(Establishment.of("", Establishment.Period.of(LocalDate.now(), LocalDate.now(), "")));
                } else {
                    return MultipleTextHolder.from();
                }
            });
        }
    }

    private String id;
    private String fullName;
    private String location;
    private int age;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Holder> sections = new EnumMap<>(SectionType.class);

    private Resume() {
    }

    private Resume(String id, String fullName, String location, int age, Map<ContactType, String> contacts, Map<SectionType, Holder> sections) {
        String fn = fullName.trim();
        fullName = fn.isEmpty() ? null : fn;
        Objects.requireNonNull(fullName, "Full name must not be null or empty!");
        Objects.requireNonNull(location, "Location must not be null!");
        if (age < 0 || age > 300) {
            throw new IllegalArgumentException("Age must not be less than 0 or higher than 300!");
        }
        this.id = id != null ? id : generateRandomId();
        this.fullName = fullName;
        this.location = location;
        this.age = age;
        if (contacts != null)
            this.contacts.putAll(contacts);
        if (sections != null)
            this.sections.putAll(sections);
    }

    /**
     * Creates a new instance of a {@code Resume} where full name, location and age must be specified.
     * Full name and location cannot be null. Age shouldn't be less than 0 and greater than 300.
     * Contacts and sections have to be manually added by related methods after an instance will be created.
     *
     * @return {@code Resume} with full name, location and age specified.
     */
    public static Resume of(String fullName, String location, int age) {
        return of(fullName, location, age, null, null);
    }

    /**
     * Creates a new instance of a {@code Resume} where id, full name, location and age must be specified.
     * Full name and location cannot be null. If id is null it will be randomly generated.
     * Age shouldn't be less than 0 and greater than 300.
     * Contacts and sections have to be manually added by related methods after an instance will be created.
     *
     * @return {@code Resume} with id, full name, location and age specified.
     */
    public static Resume of(String id, String fullName, String location, int age) {
        return of(id, fullName, location, age, null, null);
    }

    /**
     * Creates a new instance of a {@code Resume} where all fields must be specified.
     * Full name and location cannot be null. Age shouldn't be less than 0 and greater than 300.
     * Contacts and sections may be null and can be manually added by related methods afterwards.
     *
     * @return {@code Resume} with full name, location, age, contacts and sections specified.
     */
    public static Resume of(String fullName, String location, int age, Map<ContactType, String> contacts, Map<SectionType, Holder> sections) {
        return of(null, fullName, location, age, contacts, sections);
    }

    /**
     * Creates a new instance of a {@code Resume} where all fields must be specified.
     * Full name and location cannot be null. If id is null it will be randomly generated.
     * Age shouldn't be less than 0 and greater than 300.
     * Contacts and sections may be null and can be manually added by related methods afterwards.
     *
     * @return {@code Resume} with full name, location, age, contacts and sections specified.
     */
    public static Resume of(String id, String fullName, String location, int age, Map<ContactType, String> contacts, Map<SectionType, Holder> sections) {
        return new Resume(id, fullName, location, age, contacts, sections);
    }

    /**
     * Returns a randomly generated unique id to be used for {@code Resume} instances.
     *
     * @return id generated by static {@link UUID#randomUUID} method.
     */
    private static String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Indicates whether other Resume object is "equal to" this one using all the fields used to describe a concrete person.
     *
     * @param o the reference object with which to compare.
     * @return true if equals and false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(id, resume.id) && age == resume.age &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(location, resume.location) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    /**
     * @return hashcode upon unique id which is randomly generated on instantiation.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @return unique id for this concrete {@code Resume} instance.
     */
    public String getId() {
        return id;
    }

    /**
     * @return full name (first name and last name concatenated) for this concrete {@code Resume} instance.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return location (place of living) for this concrete {@code Resume} instance.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return age of a person for this concrete {@code Resume} instance.
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns a map of contacts that is represented by {@link EnumMap}.
     *
     * @return {@code Map} which represents groups of {@code ContactType} and {@code String}, each of those is a separate type of person's contact.
     */
    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    /**
     * Returns a map of sections that is represented by {@link EnumMap}.
     *
     * @return {@code Map} which represents groups of {@code SectionType} and {@code Holder}, each of those is a separate section unit.
     */
    public Map<SectionType, Holder> getSections() {
        return sections;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param fullName first name and last name of a person specified for this concrete {@code Resume} instance.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @param location place of living of a person specified for this concrete {@code Resume} instance.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param age person's age specified for this concrete {@code Resume} instance.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @param contactType type of a contact to be added, go to {@link ContactType} to see a list of available types.
     * @param value       a concrete value for this type, e.g. mobile phone: "+7 999 123 44 55".
     */
    public void addContact(ContactType contactType, String value) {
        contacts.put(contactType, value);
    }

    /**
     * @param sectionType type of a holder to be added, go to {@link SectionType} to see a list of available types.
     * @param holder      a concrete representation of {@code Holder} for this type.
     */
    public void addSection(SectionType sectionType, Holder holder) {
        sections.put(sectionType, holder);
    }

    private void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
    }

    private void readObject(ObjectInputStream os) throws IOException, ClassNotFoundException {
        os.defaultReadObject();
    }

    /**
     * Compares two Resume objects by person's full name of each, and if they equal it compares their unique IDs.
     *
     * @param o the reference object with which to compare.
     * @return 0 if fullName.compareTo(o.fullName) returns 0, -1 or 1 if id.compareTo(o.id) returns any (in quite a mysterious theory, it may return 0 as well).
     */
    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);
        return result == 0 ? id.compareTo(o.id) : result;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", location='" + location + '\'' +
                ", age='" + age + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
    }
}

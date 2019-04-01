package me.shamanov.resumebd.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * Author: Mike
 * Date: 30.03.2019
 *
 * This class is used to represent an abstraction of each {@code Section} within {@code Resume} instance.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Section<T> implements Serializable {

    @XmlElement(type = String.class)
    private List<T> values;

    public Section() {
        //used by JAXB
    }

    Section(T... values) {
        this(Arrays.asList(values));
    }

    Section(List<? extends T> values) {
        this.values = new LinkedList<>(values);
    }

    public void addValue(T value) {
        values.add(value);
    }

    public void addValues(T... values) {
        Collections.addAll(this.values, values);
    }

    public void addValues(List<T> values) {
        this.values.addAll(values);
    }

    public T removeValue(int index) {
        return values.remove(index);
    }

    public boolean removeValue(T value) {
        return values.remove(value);
    }

    public boolean removeValueIf(Predicate<? super T> filter) {
        return values.removeIf(filter);
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        return "Section{" +
                "values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || !(o instanceof Section)) return false;
        Section<?> section = (Section<?>) o;
        return Objects.equals(values, section.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}

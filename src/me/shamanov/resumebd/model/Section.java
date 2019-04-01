package me.shamanov.resumebd.model;

import javax.xml.bind.annotation.*;
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
@XmlSeeAlso({TextSection.class, EstablishmentSection.class})
public abstract class Section<T> implements Serializable {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item", type = String.class)
    private List<T> values;

    private Section() {

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
        return values;
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

    @Override
    final public void finalize() {
        //empty
    }
}

package me.shamanov.resumedb.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Mike
 * Date: 30.03.2019
 *
 * Abstract class used to hold values of every section except for contacts within {@code Resume} instance.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({MultipleTextHolder.class, EstablishmentHolder.class})
public abstract class Holder<T> implements Serializable {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item", type = String.class)
    private List<T> values;

    Holder(T... values) {
        this(Arrays.asList(values));
    }

    Holder(List<? extends T> values) {
        this.values = new LinkedList<>(values);
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holder<?> holder = (Holder<?>) o;
        return values.equals(holder.values);
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

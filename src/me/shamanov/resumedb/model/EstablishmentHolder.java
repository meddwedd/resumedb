package me.shamanov.resumedb.model;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Mike
 * Date: 01.04.2019
 *
 * Used to store a list of {@link Establishment} values for such sections as <i>Education</i> and <i>Experience</i>.
 * @see SectionType
 */

public class EstablishmentHolder extends Holder<Establishment> {
    private static final long serialVersionUID = 1L;

    private EstablishmentHolder() {
        super();
    }

    private EstablishmentHolder(List<Establishment> values) {
        super(values);
    }

    public static EstablishmentHolder from(Establishment... values) {
        return from(Arrays.asList(values));
    }

    public static EstablishmentHolder from(List<Establishment> values) {
        return new EstablishmentHolder(values);
    }
}

package me.shamanov.resumedb.model;

import java.util.List;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

public class EstablishmentSection extends Section<Establishment> {
    private static final long serialVersionUID = 1L;

    private EstablishmentSection() {
        super();
    }

    private EstablishmentSection(Establishment... values) {
        super(values);
    }

    private EstablishmentSection(List<Establishment> values) {
        super(values);
    }

    public static EstablishmentSection from(Establishment... values) {
        return new EstablishmentSection(values);
    }

    public static EstablishmentSection from(List<Establishment> values) {
        return new EstablishmentSection(values);
    }
}

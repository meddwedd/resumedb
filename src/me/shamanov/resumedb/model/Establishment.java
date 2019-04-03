package me.shamanov.resumedb.model;

import me.shamanov.resumedb.storage.xml.adapter.LocalDateXMLAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Establishment implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "title", required = true)
    private String title;
//    @XmlElementWrapper(name = "periods")
    @XmlElement(name = "period")
    private List<Period> periods;

    public static final class Period implements Serializable {
        private static final long serialVersionUID = 1L;
        public static final transient LocalDate NOW = LocalDate.MAX;
        private static final transient String datePattern = "dd/MM/yyyy";
        private static final transient DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        @XmlJavaTypeAdapter(LocalDateXMLAdapter.class)
        private LocalDate start;

        @XmlJavaTypeAdapter(LocalDateXMLAdapter.class)
        private LocalDate end;

        @XmlElement
        private String position;
        //        @XmlElementWrapper(name = "descriptions")
        @XmlElement(name = "description")
        private List<String> description;
        private Period() { }

        private Period(LocalDate start, LocalDate end, String position, List<String> description) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.description = description;
        }

        public static Period of(String start, String end, String position) {
            return of(parseDateIfValid(start), parseDateIfValid(end), position, null);
        }

        public static Period of(String start, String end, String position, List<String> description) {
            return of(parseDateIfValid(start), parseDateIfValid(end), position, description);
        }

        public static Period of(String start, String position, List<String> description) {
            return of(parseDateIfValid(start), null, position, description);
        }

        public static Period of(String start, String position) {
            return of(parseDateIfValid(start), null, position, null);
        }

        public static Period of(LocalDate start, LocalDate end, String position, List<String> description) {
            Objects.requireNonNull(start, "at least start date must be specified!");
            Objects.requireNonNull(position, "position must be specified!");
            if (end == null || end.isBefore(start)) {
                end = Period.NOW;
            }
            if (description == null || description.isEmpty()) {
                description = Collections.singletonList("");
            }
            return new Period(start, end, position, description);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return start.equals(period.start) &&
                    end.equals(period.end) &&
                    Objects.equals(position, period.position) &&
                    Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, position);
        }

        private static LocalDate parseDateIfValid(String date) {
            try {
                return LocalDate.parse(date, dateTimeFormatter);
            } catch (DateTimeParseException dtpe) {
                throw new IllegalArgumentException("Incorrect date, it must match this pattern: " + datePattern, dtpe);
            }
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public String getPosition() {
            return position;
        }

        public List<String> getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Period{" +
                    "start=" + start +
                    ", end=" + end +
                    ", position='" + position + '\'' +
                    ", description=" + description +
                    '}';
        }
    }

    private Establishment() { }

    private Establishment(String title, Period... periods) {
        this.title = title;
        this.periods = new LinkedList<>(Arrays.asList(periods));
    }

    public static Establishment of(String title, Period... periods) {
        Objects.requireNonNull(title, "You must specify a title of this establishment!");
        if (periods.length == 0) {
            throw new IllegalArgumentException("At least one position must be specified!");
        }

        return new Establishment(title, periods);
    }

    public String getTitle() {
        return title;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Establishment that = (Establishment) o;
        return title.equals(that.title) &&
                periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public String toString() {
        return "Establishment{" +
                "title='" + title + '\'' +
                ", periods=" + periods +
                '}';
    }
}

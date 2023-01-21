package com.urise.webapp.model;


import com.urise.webapp.util.DateUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.urise.webapp.util.DateUtil.NOW;

public class Organization implements Serializable {
    @Serial
    private static final long serialVersionUID = -313169696235072244L;
    private final Link name;
    private List<Period> periods = new ArrayList<>();

    public Organization(String name, String url, Period... periods) {
        this(new Link(name, url), Arrays.asList(periods));
    }

    public Organization(Link name, List<Period> periods) {
        this.name = name;
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "Organisation{" + name + '\'' + periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return name.equals(that.name) && periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, periods);
    }

    public static class Period implements Serializable {
        @Serial
        private static final long serialVersionUID = -313169696235072244L;
        private final LocalDate startDate;
        private final LocalDate finishDate;
        private final String title;
        private final String description;

        public Period(int startYear, Month startMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), NOW, title, description);
        }

        public Period(int startYear, Month startMonth, int finishYear, Month finishMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(finishYear, finishMonth), title, description);
        }

        public Period(LocalDate startDate, LocalDate finishDate, String title, String description) {
            Objects.requireNonNull(startDate, "Start Date must not be null");
            Objects.requireNonNull(finishDate, "Finish Date must not be null");
            Objects.requireNonNull(title, "Title must not be null");
            this.startDate = startDate;
            this.finishDate = finishDate;
            this.title = title;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Period{" +
                    startDate + " " +
                    finishDate + " " +
                    title + '\'' + " " +
                    description + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return startDate.equals(period.startDate) && finishDate.equals(period.finishDate) && title.equals(period.title) && Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, finishDate, title, description);
        }
    }
}

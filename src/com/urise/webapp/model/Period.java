package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String title;
    private final String description;

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

package com.urise.webapp.model;

import java.time.LocalDate;

public class Period {
    private final LocalDate start;
    private final LocalDate finish;
    private final String title;
    private final String description;

    public Period(LocalDate start, LocalDate finish, String title, String description) {
        this.start = start;
        this.finish = finish;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Period{" +
                start + " " +
                finish + " " +
                title + '\'' + " " +
                description + '\'' +
                '}';
    }
}

package com.urise.webapp.model;

import java.util.Objects;

public class Organisation {
    private final String name;
    private final String dates;
    private final String title;
    private final String content;

    public Organisation(String name, String dates, String title, String content) {
        this.name = name;
        this.dates = dates;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "name='" + name + '\'' +
                ", dates='" + dates + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return Objects.equals(name, that.name) && Objects.equals(dates, that.dates) && Objects.equals(title, that.title) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dates, title, content);
    }
}

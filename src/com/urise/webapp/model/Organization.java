package com.urise.webapp.model;


import java.util.Objects;

public class Organization {
    private final Link name;
    private final Period period;

    public Organization(String name, String url, Period period) {
        this.name = new Link(name, url);
        this.period = period;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "name = '" + name + '\'' + period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return name.equals(that.name) && period.equals(that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, period);
    }
}

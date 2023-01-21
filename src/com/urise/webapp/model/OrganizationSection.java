package com.urise.webapp.model;


import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    @Serial
    private static final long serialVersionUID = -313169696235072244L;
    private final List<Organization> organisations;

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organisations = organizations;
    }

    public OrganizationSection(Organization... organization) {
        this(Arrays.asList(organization));
    }

    public List<Organization> getOrganisations() {
        return organisations;
    }

    @Override
    public String toString() {
        return organisations.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organisations.equals(that.organisations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisations);
    }
}

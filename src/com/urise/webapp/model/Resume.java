package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {

    @Serial
    private static final long serialVersionUID = -313169696235072244L;
    private String uuid;

    private String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public void setContact(ContactType contactType, String content) {
        contacts.put(contactType, content);
    }

    public String getContact(ContactType contact) {
        return contacts.get(contact);
    }

    public void setSections(SectionType sectionType, Section section) {
        sections.put(sectionType, section);
    }

    public Section getSection(SectionType section) {
        return sections.get(section);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "Uuid must not be null");
        Objects.requireNonNull(fullName, "Full name must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume() {
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Resume " +
                "uuid = '" + uuid + '\'' + "\n" +
                "fullName = '" + fullName + '\'' + "\n" +
                "contacts = " + contacts + "\n" +
                "sections = " + sections + "\n"
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) && Objects.equals(contacts, resume.contacts) && Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }
}

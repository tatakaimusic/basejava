package com.urise.webapp.storage.serializtion;

import com.urise.webapp.model.*;
import com.urise.webapp.util.DateUtil;


import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerialization implements StreamSerialization {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();

            writeCollection(dos, contacts.entrySet(), contact -> {
                dos.writeUTF(contact.getKey().name());
                dos.writeUTF(contact.getValue());
            });

            writeCollection(dos, r.getSections().entrySet(), sectionEntry -> {
                SectionType type = sectionEntry.getKey();
                Section section = sectionEntry.getValue();
                dos.writeUTF(type.name());

                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) section).getItems(), listSection -> dos.writeUTF(listSection));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) section).getOrganisations(), organization -> {
                            dos.writeUTF(organization.getLink().getName());
                            dos.writeUTF(organization.getLink().getUrl());

                            writeCollection(dos, organization.getPeriods(), period -> {
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                                writeLocalDate(dos, period.getStartDate());
                                writeLocalDate(dos, period.getFinishDate());
                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readCollection(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {

                    case PERSONAL:
                    case OBJECTIVE:
                        TextSection textSection = new TextSection();
                        textSection.setContent(dis.readUTF());
                        resume.setSection(type, textSection);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = new ArrayList<>();
                        readCollection(dis, () -> items.add(dis.readUTF()));
                        ListSection listSection = new ListSection();
                        listSection.setItems(items);
                        resume.setSection(type, listSection);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = new ArrayList<>();
                        OrganizationSection organizationSection = new OrganizationSection();

                        readCollection(dis, () -> {
                            Organization organization = new Organization();
                            organization.setLink(new Link(dis.readUTF(), dis.readUTF()));

                            readCollection(dis, () -> {
                                Organization.Period period = new Organization.Period();
                                period.setTitle(dis.readUTF());
                                period.setDescription(dis.readUTF());
                                period.setStartDate(DateUtil.of(dis.readInt(), Month.of(dis.readInt())));
                                period.setFinishDate(DateUtil.of(dis.readInt(), Month.of(dis.readInt())));
                                organization.setPeriod(period);
                            });
                            organizations.add(organization);
                        });
                        organizationSection.setOrganisations(organizations);
                        resume.setSection(type, organizationSection);
                        break;
                }
            });
            return resume;
        }
    }

    private interface readItems {
        void read() throws IOException;
    }


    private void readCollection(DataInputStream dis, readItems reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private interface writeItems<T> {
        void write(T entry) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, writeItems<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T entry : collection) {
            writer.write(entry);
        }
    }

    private void writeLocalDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonth().getValue());
    }
}

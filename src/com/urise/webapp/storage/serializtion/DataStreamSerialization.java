package com.urise.webapp.storage.serializtion;

import com.urise.webapp.model.*;
import com.urise.webapp.util.DateUtil;


import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

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
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            writeCollection(dos, ((ListSection) section).getItems(), dos::writeUTF);
                    case EXPERIENCE, EDUCATION ->
                            writeCollection(dos, ((OrganizationSection) section).getOrganisations(), organization -> {
                                dos.writeUTF(organization.getLink().getName());
                                dos.writeUTF(organization.getLink().getUrl());

                                writeCollection(dos, organization.getPeriods(), period -> {
                                    writeLocalDate(dos, period.getStartDate());
                                    writeLocalDate(dos, period.getFinishDate());
                                    dos.writeUTF(period.getTitle());
                                    dos.writeUTF(period.getDescription());
                                });
                            });
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
            readElement(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readElement(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> resume.setSection(type, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            resume.setSection(type, new ListSection(readList(dis, dis::readUTF)));
                    case EXPERIENCE, EDUCATION -> resume.setSection(type, new OrganizationSection(
                            readList(dis, () -> new Organization(
                                    new Link(dis.readUTF(), dis.readUTF()),
                                    readList(dis, () -> new Organization.Period(
                                            readLocalDate(dis), readLocalDate(dis), dis.readUTF(), dis.readUTF()
                                    ))
                            ))
                    ));
                }
            });
            return resume;
        }
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return DateUtil.of(dis.readInt(), Month.of(dis.readInt()));
    }

    private <T> List<T> readList(DataInputStream dis, ElementReader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private interface ElementReader<T> {
        T read() throws IOException;
    }

    private interface ReadItems {
        void read() throws IOException;
    }


    private void readElement(DataInputStream dis, ReadItems reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private interface WriteItems<T> {
        void write(T entry) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, WriteItems<T> writer) throws IOException {
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

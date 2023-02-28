package com.urise.webapp.storage.serializtion;

import com.urise.webapp.model.*;
import com.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerialization implements StreamSerialization {
    private XmlParser xmlParser;

    public XmlStreamSerialization() {
        xmlParser = new XmlParser(
                Resume.class, Organisation.class, Link.class,
                OrganizationSection.class, TextSection.class, ListSection.class,
                Organisation.Period.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unMarshall(r);
        }
    }
}

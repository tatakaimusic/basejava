package com.urise.webapp.util;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.TextSection;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class JsonParserTest {
    @Test
    public void testResume() throws Exception {
        Resume resume = ResumeTestData.createResume(UUID.randomUUID().toString(), "Name1");
        String json = JsonParser.write(resume);
        System.out.println(json);
        Resume resume1 = JsonParser.read(json, Resume.class);
        Assert.assertEquals(resume, resume1);
    }

    @Test
    public void read() {
    }

    @Test
    public void write() {
        Section section = new TextSection("Content");
        String json = JsonParser.write(section, Section.class);
        System.out.println(json);
        Section section1 = JsonParser.read(json, Section.class);
        Assert.assertEquals(section, section1);
    }
}
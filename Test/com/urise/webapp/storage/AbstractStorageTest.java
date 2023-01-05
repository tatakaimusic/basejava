package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractStorageTest {
    protected final Storage storage;
    private final String UUID_1 = "uuid1";
    private final String fullName1 = "Andrey";
    private final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, fullName1);

    private final String UUID_2 = "uuid2";
    private final String fullName2 = "Bob";
    private final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, fullName2);

    private final String UUID_3 = "uuid3";
    private final String fullName3 = "Max";
    private final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, fullName3);

    private final String UUID_NOT_EXIST = "UUID_NOT_EXIST";
    private final String FULL_NAME_NOT_EXIST = "FULL_NAME_NOT_EXIST";
    private final Resume RESUME_4 = ResumeTestData.createResume(UUID_NOT_EXIST, FULL_NAME_NOT_EXIST);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_2);
        storage.save(RESUME_3);
        storage.save(RESUME_1);
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        Resume r1 = new Resume(UUID_1, fullName1);
        storage.save(r1);
    }

    @Test
    public void saveNotExist() {
        Resume r1 = new Resume(UUID_NOT_EXIST, FULL_NAME_NOT_EXIST);
        storage.save(r1);
        assertSize(4);
        assertGet(r1);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_4);
    }

    @Test
    public void updateExist() {
        Resume resume = new Resume(UUID_1, FULL_NAME_NOT_EXIST);
        storage.update(resume);
        Assert.assertSame(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExist() {
        storage.delete(UUID_3);
        assertSize(2);
        assertGet(storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test
    public void getExist() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test
    public void getAllSorted() {
        List<Resume> expectedResumes = storage.getAllSorted();
        Assert.assertEquals(expectedResumes, Arrays.asList(RESUME_1, RESUME_2, RESUME_3));
        assertSize(3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int expectedSize) {
        Assert.assertEquals(expectedSize, storage.size());
    }
}

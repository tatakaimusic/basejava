package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {

    private final Storage storage;
    private final String UUID_1 = "uuid1";
    private final Resume RESUME_1 = new Resume(UUID_1);

    private final String UUID_2 = "uuid2";
    private final Resume RESUME_2 = new Resume(UUID_2);

    private final String UUID_3 = "uuid3";
    private final Resume RESUME_3 = new Resume(UUID_3);

    private final String UUID_4 = "uuid4";
    private final Resume RESUME_4 = new Resume(UUID_4);

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        Resume r1 = new Resume(UUID_1);
        storage.save(r1);
    }

    @Test
    public void saveNotExist() {
        Resume r1 = new Resume(UUID_4);
        storage.save(r1);
        assertSize(4);
        assertGet(r1);

    }

    @Test(expected = StorageException.class)
    public void storageOverFlow() {
        storage.clear();
        try {
            for (int i = 0; i < 10000; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Exception throw ahead of time");
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_4);
    }

    @Test
    public void updateExist() {
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        Assert.assertTrue(resume == storage.get(UUID_1));

    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("uuid4");
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExist() {
        storage.delete(UUID_3);
        assertSize(2);
        assertGet(storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
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
    public void getAll() {
        Resume[] expectedResumes = new Resume[3];
        expectedResumes[0] = RESUME_1;
        expectedResumes[1] = RESUME_2;
        expectedResumes[2] = RESUME_3;
        Assert.assertEquals(expectedResumes, storage.getAll());
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
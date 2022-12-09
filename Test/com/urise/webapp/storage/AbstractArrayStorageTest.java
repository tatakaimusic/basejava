package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {

    private Storage storage = new ArrayStorage();
    private final Resume UUID_1 = new Resume("uuid1");
    private final Resume UUID_2 = new Resume("uuid2");
    private final Resume UUID_3 = new Resume("uuid3");
    private final Resume UUID_4 = new Resume("uuid4");

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(UUID_1);
        storage.save(UUID_2);
        storage.save(UUID_3);
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        Resume r1 = new Resume("uuid1");
        storage.save(r1);
    }

    @Test
    public void saveNotExist() {
        Resume r1 = new Resume("uuid4");
        storage.save(r1);

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
        storage.update(UUID_4);
    }

    @Test
    public void updateExist() {
        storage.update(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("uuid4");
    }

    @Test
    public void deleteExist() {
        storage.delete("uuid3");
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void getExist() {
        storage.get("uuid1");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertEquals(UUID_1, resumes[0]);
        Assert.assertEquals(UUID_2, resumes[1]);
        Assert.assertEquals(UUID_3, resumes[2]);
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}
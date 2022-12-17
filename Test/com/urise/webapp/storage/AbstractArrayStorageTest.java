package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void storageOverFlow() {
        storage.clear();
        try {
            for (int i = 0; i < 10000; i++) {
                storage.save(new Resume("" + i));
            }
        } catch (StorageException e) {
            Assert.fail("Exception throw ahead of time");
        }
        storage.save(new Resume("last"));
    }
}
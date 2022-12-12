package com.urise.webapp.storage;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage extends AbstractStorage {

    protected int size = 0;
    protected final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void saveArrayResume(Resume r, int index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("ERROR: Storage overflow", r.getUuid());
        } else {
            saveResume(r, index);
            size++;
        }
    }

    public void updateArrayResume(Resume r, int index) {
            storage[index] = r;
    }

    public void deleteArrayResume(String uuid, int index) {
            deleteResume(uuid, index);
            size--;
    }


    public Resume getArrayResume(String uuid, int index) {
        return storage[index];
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    protected abstract void saveResume(Resume r, int index);

    protected abstract void deleteResume(String uuid, int index);
}

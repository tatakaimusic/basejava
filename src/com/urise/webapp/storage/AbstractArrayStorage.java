package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage extends AbstractStorage {

    protected int size = 0;
    protected final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected abstract Integer getSearchKey(String uuid);

    protected abstract void saveResume(Resume r, int key);

    protected abstract void deleteResume(String uuid, int key);


    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void doSave(Resume r, Object index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("ERROR: Storage overflow", r.getUuid());
        } else {
            saveResume(r, (Integer) index);
            size++;
        }
    }

    public void doUpdate(Resume r, Object index) {
        storage[(Integer) index] = r;
    }

    public void doDelete(String uuid, Object index) {
        deleteResume(uuid, (Integer) index);
        size--;
    }


    public Resume doGet(String uuid, Object index) {
        return storage[(Integer) index];
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }


    protected boolean isExist(Object index) {
        return (Integer) index >= 0;
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

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

    protected void doSave(Resume r, Integer index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("ERROR: Storage overflow", r.getUuid());
        } else {
            saveResume(r, index);
            size++;
        }
    }

    protected void doUpdate(Resume r, Integer index) {
        storage[index] = r;
    }

    protected void doDelete(String uuid, Integer index) {
        deleteResume(uuid, index);
        size--;
    }

    protected Resume doGet(String uuid, Integer index) {
        return storage[index];
    }

    protected void doCopyAll(List<Resume> resumes) {
        resumes.addAll(Arrays.asList(Arrays.copyOfRange(storage, 0, size)));
    }

    public int size() {
        return size;
    }


    protected boolean isExist(Integer index) {
        return index >= 0;
    }
}

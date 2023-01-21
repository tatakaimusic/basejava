package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void saveResume(Resume r, int key) {
        storage[size] = r;
    }

    public void deleteResume(int key) {
        storage[key] = storage[size - 1];
        storage[size - 1] = null;

    }

    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void saveChild(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            STORAGE[size] = r;
            size++;
        }
    }

    public void deleteChild(String uuid) {
        int index = getIndex(uuid);
        STORAGE[index] = STORAGE[size - 1];
        STORAGE[size - 1] = null;
        size--;

    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (STORAGE[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

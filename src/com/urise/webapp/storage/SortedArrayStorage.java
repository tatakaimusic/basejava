package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void saveResume(Resume r, int key) {
        int insertionIndex = Math.abs(key) - 1;
        System.arraycopy(storage, insertionIndex, storage, insertionIndex + 1, size - insertionIndex);
        storage[insertionIndex] = r;
    }

    public void deleteResume(String uuid, int key) {
        storage[key] = null;
        int numMoved = size - key - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, key + 1, storage, key, numMoved);
        }
    }

    protected Integer getSearchKey(String uuid) {
        Resume key = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, key);
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void saveResume(Resume r, int index) {
        int insertionIndex = Math.abs(index) - 1;
         if (storage[insertionIndex] != null) {
            for (int i = size; i >= insertionIndex; i--) {
                storage[i + 1] = storage[i];
                storage[i] = null;
            }
            storage[insertionIndex] = r;

        }
    }

    public void deleteResume(String uuid, int index) {
        storage[index] = null;
        for (int i = index; i < size; i++) {
            storage[i] = storage[i + 1];
            storage[i + 1] = null;
        }
        size--;
    }

    protected int getIndex(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, key);
    }
}

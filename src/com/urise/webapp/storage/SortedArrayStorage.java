package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void saveResume(Resume r, int index) {
        int insertionIndex = Math.abs(index) - 1;
        System.arraycopy(storage, insertionIndex, storage, insertionIndex + 1, size);
        storage[insertionIndex] = r;
    }

    public void deleteResume(String uuid, int index) {
        storage[index] = null;
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
    }

    protected int getIndex(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, key);
    }
}

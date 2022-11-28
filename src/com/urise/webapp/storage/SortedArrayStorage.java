package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        int currentIndex = Math.abs(index) - 1;
        if (index >= 0) {
            System.out.println("ERROR:" + " resume " + r.getUuid() + " already exist!");
        } else if (STORAGE[currentIndex] == null) {
            STORAGE[currentIndex] = r;
            size++;
        } else if (STORAGE[currentIndex] != null) {
            for (int i = size; i >= currentIndex; i--) {
                STORAGE[i + 1] = STORAGE[i];
                STORAGE[i] = null;
            }
            STORAGE[currentIndex] = r;
            size++;
        }
    }

    protected int getIndex(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);
        return Arrays.binarySearch(STORAGE, 0, size, key);
    }
}

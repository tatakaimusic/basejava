package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        int currentIndex = Math.abs(index) - 1;
        if (index >= 0) {
            System.out.println("ERROR:" + " resume " + r.getUuid() + " already exist!");
        } else if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: storage overflow");
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

    public void delete(String uuid) {
        int index = getIndex(uuid);

        if (index < 0) {
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
        } else {
            STORAGE[index] = null;
            for (int i = index; i < size; i++) {
                STORAGE[i] = STORAGE[i + 1];
                STORAGE[i + 1] = null;
            }
            size--;
        }
    }

    protected int getIndex(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);
        return Arrays.binarySearch(STORAGE, 0, size, key);
    }
}

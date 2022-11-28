package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Scanner;

public abstract class AbstractArrayStorage implements Storage {
    protected int size = 0;

    protected final int STORAGE_LIMIT = 10000;
    protected final Resume[] STORAGE = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(STORAGE, 0, size, null);
        size = 0;
    }

    public abstract void save(Resume r);

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index == -1) {
            System.out.println("ERROR:" + " resume " + r.getUuid() + " doesn't exist!");
        } else {
            System.out.print("Enter updated resume: ");
            Scanner scanner = new Scanner(System.in);
            String newUuid = scanner.nextLine();
            r.setUuid(newUuid);
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
            return null;
        }
        return STORAGE[index];
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
        } else {
            STORAGE[index] = STORAGE[size - 1];
            STORAGE[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(STORAGE, 0, size);
    }

    public int size() {
        return size;
    }


    protected abstract int getIndex(String uuid);
}

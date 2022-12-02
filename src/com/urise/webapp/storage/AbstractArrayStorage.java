package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage implements Storage {
    protected int size = 0;

    protected final int STORAGE_LIMIT = 10000;
    protected final Resume[] STORAGE = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(STORAGE, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > 0) {
            System.out.println("ERROR:" + " resume " + r.getUuid() + " already exist!");
        } else if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: storage overflow");
        } else {
            saveResume(r, index);
        }
    }

    ;

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            System.out.println("ERROR:" + " resume " + r.getUuid() + " doesn't exist!");
        } else {
            STORAGE[index] = r;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
        } else {
            deleteResume(uuid, index);
        }
    }

    ;

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
            return null;
        }
        return STORAGE[index];
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

    protected abstract void saveResume(Resume r, int index);

    protected abstract void deleteResume(String uuid, int index);
}

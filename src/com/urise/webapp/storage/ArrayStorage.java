package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Scanner;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private boolean test = false;
    Resume[] storage = new Resume[10000];

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        check(uuid);
        if (!test) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("ERROR: " + "Resume " + r + " already exist");
        }
        test = false;
    }

    public void update(Resume r) {
        String uuid = r.getUuid();
        check(uuid);
        if (test) {
            System.out.print("Enter updated resume: ");
            Scanner scanner = new Scanner(System.in);
            String newUuid = scanner.nextLine();
            r.setUuid(newUuid);
        } else {
            System.out.println("ERROR: " + "Resume " + uuid + " doesn't exist");
        }
        test = false;
    }

    public Resume get(String uuid) {
        check(uuid);
        if (test) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    return storage[i];
                }
            }
        } else {
            System.out.println("ERROR: " + "Resume " + uuid + " doesn't exist");
        }
        test = false;
        return null;
    }

    public void delete(String uuid) {
        check(uuid);
        if (test) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    storage[i] = storage[size - 1];
                    storage[size - 1] = null;
                    size--;
                }
            }
        } else {
            System.out.println("Resume " + uuid + " doesn't exist");
        }
        test = false;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] temporaryStorage = new Resume[size];
        for (int i = 0; i < size; i++) {
            temporaryStorage[i] = storage[i];
        }
        return temporaryStorage;
    }

    public int size() {
        return size;
    }

    private void check(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                test = true;
            }
        }
    }
}

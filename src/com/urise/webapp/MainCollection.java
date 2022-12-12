package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.AbstractStorage;
import com.urise.webapp.storage.ListStorage;

public class MainCollection {
    static final AbstractStorage LIST_STORAGE = new ListStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1");
        Resume r2 = new Resume("uuid2");
        Resume r3 = new Resume("uuid3");

        LIST_STORAGE.save(r1);
        LIST_STORAGE.save(r2);
        LIST_STORAGE.save(r3);

        printAll();

        LIST_STORAGE.delete("uuid2");
        printAll();


    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : LIST_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}



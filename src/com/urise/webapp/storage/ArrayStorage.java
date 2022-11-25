package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private final Resume[] STORAGE = new Resume[10000];

    public void clear() {
        Arrays.fill(STORAGE, 0, size, null);
      size = 0;
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if(index == -1){
            STORAGE[size] = r;
            size++;
        }else{
            System.out.println("ERROR:" + " resume " + r.getUuid() + " already exist!");
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if(index == -1){
            System.out.println("ERROR:" + " resume " + r.getUuid() + " doesn't exist!");
        }else{
            System.out.print("Enter updated resume: ");
            Scanner scanner = new Scanner(System.in);
            String newUuid = scanner.nextLine();
            r.setUuid(newUuid);
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if(index == -1){
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
            return null;
        }
        return STORAGE[index];
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if(index == -1){
            System.out.println("ERROR:" + " resume " + uuid + " doesn't exist!");
        }else{
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

    private int getIndex(String uuid){
        for(int i = 0; i<size; i++){
            if(STORAGE[i].getUuid().equals(uuid)){
                return i;
            }
        }
        return -1;
    }
}

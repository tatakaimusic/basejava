package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private final List<Resume> LIST_STORAGE = new ArrayList<>();


    @Override
    public void clear() {
        LIST_STORAGE.clear();
    }

    @Override
    public void saveArrayResume(Resume r, int index) {
        LIST_STORAGE.add(r);
    }

    public void updateArrayResume(Resume r, int index) {
        LIST_STORAGE.set(index, r);
    }

    public Resume getArrayResume(String uuid, int index) {
        return LIST_STORAGE.get(index);
    }

    @Override
    public void deleteArrayResume(String uuid, int index) {
        LIST_STORAGE.remove(index);
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[LIST_STORAGE.size()];
        LIST_STORAGE.toArray(resumes);
        return Arrays.copyOfRange(resumes, 0, resumes.length);
    }

    @Override
    public int size() {
        return LIST_STORAGE.size();
    }

    @Override
    protected int getIndex(String uuid) {
        Resume key = new Resume(uuid);
        return LIST_STORAGE.indexOf(key);
    }
}

package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Object searchKey) {
        storage.add(r);
    }

    public void doUpdate(Resume r, Object searchKey) {
        storage.set((Integer) searchKey, r);
    }

    public Resume doGet(String uuid, Object key) {
        return storage.get((Integer) key);
    }

    @Override
    public void doDelete(String uuid, Object searchKey) {
        storage.remove(((Integer) searchKey).intValue());
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        storage.toArray(resumes);
        return Arrays.copyOfRange(resumes, 0, resumes.length);
    }

    @Override
    public int size() {
        return storage.size();
    }
}

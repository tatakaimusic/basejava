package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
    protected boolean isExist(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doSave(Resume r, Integer searchKey) {
        storage.add(r);
    }

    protected void doUpdate(Resume r, Integer searchKey) {
        storage.set(searchKey, r);
    }

    protected Resume doGet(String uuid, Integer key) {
        return storage.get(key);
    }

    @Override
    protected void doDelete(String uuid, Integer searchKey) {
        storage.remove((searchKey).intValue());
    }

    @Override
    protected void doCopyAll(List<Resume> resumes) {
        resumes.addAll(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }
}

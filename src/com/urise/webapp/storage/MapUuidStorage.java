package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doSave(Resume r, String searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, String searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(String uuid, String searchKey) {
        return storage.get(uuid);
    }

    @Override
    protected void doDelete(String uuid, String searchKey) {
        storage.remove(uuid);
    }

    @Override
    protected void doCopyAll(List<Resume> resumes) {
        for (Map.Entry<String, Resume> resume : storage.entrySet()) {
            resumes.add(resume.getValue());
        }
    }

    @Override
    public int size() {
        return storage.size();
    }
}

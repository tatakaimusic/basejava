package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(String uuid, Object searchKey) {
        return storage.get(uuid);
    }

    @Override
    protected void doDelete(String uuid, Object searchKey) {
        storage.remove(uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        for (Map.Entry<String, Resume> resume : storage.entrySet()) {
            resumes.add(resume.getValue());
        }
        resumes.sort(comparator);
        return resumes;
    }

    @Override
    public int size() {
        return storage.size();
    }
}

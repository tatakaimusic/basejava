package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume resume) {
        return resume != null;

    }

    @Override
    protected void doSave(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(String uuid, Resume resume) {
        return resume;
    }

    @Override
    protected void doDelete(Resume resume) {
        storage.remove((resume).getUuid());
    }

    @Override
    public void clear() {
        storage.clear();
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

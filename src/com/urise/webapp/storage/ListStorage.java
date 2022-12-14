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
    public void doSave(Resume r, Object searchKey) {
        LIST_STORAGE.add(r);
    }

    public void doUpdate(Resume r, Object searchKey) {
        LIST_STORAGE.set((Integer) searchKey, r);
    }

    public Resume doGet(String uuid, Object key) {
        return LIST_STORAGE.get((Integer) key);
    }

    @Override
    public void doDelete(String uuid, Object searchKey) {
        LIST_STORAGE.remove(((Integer) searchKey).intValue());
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
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < LIST_STORAGE.size(); i++) {
            if (LIST_STORAGE.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }
}

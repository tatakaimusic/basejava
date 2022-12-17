package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;

public abstract class AbstractStorage implements Storage {

    protected Comparator<Resume> comparator = (o1, o2) -> o1.getFullName().compareTo(o2.getFullName());

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract void doUpdate(Resume r, Object searchKey);

    protected abstract Resume doGet(String uuid, Object searchKey);

    protected abstract void doDelete(String uuid, Object searchKey);

    public void save(Resume r) {
        Object searchKey = getNotExistedSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    public void update(Resume r) {
        Object searchKey = getExistedSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        return doGet(uuid, searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        doDelete(uuid, searchKey);
    }

    private Object getExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}

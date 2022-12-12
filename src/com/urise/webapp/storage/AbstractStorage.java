package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage{
    protected abstract int getIndex(String uuid);

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        saveArrayResume(r, index);
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        }else{
            updateArrayResume(r, index);
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            throw new NotExistStorageException(uuid);
        }
        return getArrayResume(uuid, index);
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            throw new NotExistStorageException(uuid);
        }
        deleteArrayResume(uuid, index);
    }


    protected abstract void saveArrayResume(Resume r, int index);

    protected abstract void updateArrayResume(Resume r, int index);

    protected abstract Resume getArrayResume(String uuid, int index);
    protected abstract void deleteArrayResume(String uuid, int index);

}

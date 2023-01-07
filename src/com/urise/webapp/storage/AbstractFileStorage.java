package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(String uuid, File file) {
        try {
            return doRead(uuid, file);
        } catch (IOException e) {
            throw new StorageException("Resume can't be read", file.getName());
        }
    }


    @Override
    protected void doDelete(String uuid, File file) {
        file.delete();
        if (file.exists()) {
            throw new StorageException("FileNotDeleted error", file.getName());
        }
    }

    @Override
    protected void doCopyAll(List<Resume> resumes) {
        File[] list = directory.listFiles();
        if (list != null) {
            for (File file : list) {
                resumes.add(doGet(file.getName(), file));
            }
        } else {
            throw new StorageException("Directory is null", directory.getName());
        }
    }

    @Override
    public void clear() {
        File[] list = directory.listFiles();
        if (list != null) {
            for (File file : list) {
                doDelete(file.getName(), file);
            }
        } else {
            throw new StorageException("Directory is null", directory.getName());
        }
    }

    @Override
    public int size() {
        return 0;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(String uuid, File file) throws IOException;
}

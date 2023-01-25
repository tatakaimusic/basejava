package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializtion.StreamSerialization;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamSerialization streamSerialization;

    protected FileStorage(File directory, StreamSerialization streamSerialization) {
        Objects.requireNonNull(directory, "Directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.streamSerialization = streamSerialization;
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
        } catch (IOException e) {
            throw new StorageException("File didn't create " + file, file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            streamSerialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File didn't update " + file, file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(String uuid, File file) {
        try {
            return streamSerialization.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Resume can't be read", file.getName());
        }
    }


    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("FileNotDeleted error", file.getName());
        }
    }

    @Override
    protected void doCopyAll(List<Resume> resumes) {
        directoryNotNullCheck();
        for (File file : directory.listFiles()) {
            resumes.add(doGet(file.getName(), file));
        }
    }

    @Override
    public void clear() {
        directoryNotNullCheck();
        for (File file : directory.listFiles()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        directoryNotNullCheck();
        return directory.listFiles().length;
    }

    private void directoryNotNullCheck() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("Directory is null", directory.getName());
        }
    }
}

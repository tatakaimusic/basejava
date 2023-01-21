package com.urise.webapp.storage;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializtion.StreamSerialization;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerialization streamSerialization;

    protected PathStorage(String dir, StreamSerialization streamSerialization) {
        Objects.requireNonNull(dir, "Directory must not be null");
        directory = Paths.get(dir);
        this.streamSerialization = streamSerialization;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return new File(directory.toFile(), uuid).toPath();
    }

    @Override
    protected boolean isExist(Path path) {
        return path.toFile().exists();
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            if (!path.toFile().createNewFile()) {
                throw new StorageException("Path didn't create", path.getFileName().toString());
            }
            streamSerialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            streamSerialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(String uuid, Path path) {
        try {
            return streamSerialization.doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Resume can't be read", path.getFileName().toString());
        }
    }


    @Override
    protected void doDelete(Path path) {
        if (!path.toFile().delete()) {
            throw new StorageException("PathNotDeleted error", path.getFileName().toString());
        }
    }

    @Override
    protected void doCopyAll(List<Resume> resumes) {
        File[] list = directory.toFile().listFiles();
        if (list != null) {
            for (File file : list) {
                resumes.add(doGet(file.getName(), file.toPath()));
            }
        } else {
            throw new StorageException("Directory is null", directory.getFileName().toString());
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(path -> doDelete(path));
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        if (directory.toFile().listFiles() == null) {
            throw new StorageException("Directory is null", directory.getFileName().toString());
        }
        return directory.toFile().listFiles().length;
    }
}

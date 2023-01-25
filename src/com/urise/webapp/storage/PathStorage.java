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
import java.util.stream.Stream;

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
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            path.toFile().createNewFile();
        } catch (IOException e) {
            throw new StorageException("Path didn't create " + path, path.getFileName().toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            streamSerialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Path didn't update " + path, path.getFileName().toString(), e);
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
        getPathList().forEach(path -> resumes.add(doGet(path.getFileName().toString(), path)));
    }


    @Override
    public void clear() {
        getPathList().forEach(path -> doDelete(path));
    }

    @Override
    public int size() {
        return (int) getPathList().count();
    }

    private Stream<Path> getPathList() {
        try {
            Stream<Path> stream = Files.list(directory);
            return stream;
        } catch (IOException e) {
            throw new StorageException("Directory is null", directory.getFileName().toString(), e);
        }
    }
}

package com.urise.webapp.exeption;

public class ExistStorageException extends StorageException {

    public ExistStorageException(String uuid){
        super("ERROR: Resume " + uuid + " already exist" ,uuid);
    }
}

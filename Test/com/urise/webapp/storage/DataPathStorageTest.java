package com.urise.webapp.storage;

import com.urise.webapp.storage.serializtion.DataStreamSerialization;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new DataStreamSerialization()));
    }
}

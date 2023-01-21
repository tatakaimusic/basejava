package com.urise.webapp.storage;

import com.urise.webapp.storage.serializtion.ObjectStreamStorage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamStorage()));
    }
}

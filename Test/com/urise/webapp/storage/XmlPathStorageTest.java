package com.urise.webapp.storage;

import com.urise.webapp.storage.serializtion.XmlStreamSerialization;

public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new XmlStreamSerialization()));
    }
}

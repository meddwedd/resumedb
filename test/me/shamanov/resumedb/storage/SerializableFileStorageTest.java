package me.shamanov.resumedb.storage;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class SerializableFileStorageTest extends AbstractStorageTest {
    private String storageDir = "filestorage/files";

    {
        storage = new SerializableFileStorage(storageDir);
    }
}
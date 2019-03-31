package me.shamanov.resumebd.storage;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class XMLFileStorageTest extends AbstractStorageTest {
    private String storageDir = "filestorage/xml";

    {
        storage = new XMLFileStorage(storageDir);
    }
}
package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public abstract class AbstractStorageTest {
    protected Storage storage;
    private List<Resume> resumes;

    @Before
    public void setUp() {
        resumes = new ArrayList<Resume>() {
            {
                add(Resume.of("Alexander Kovrov", "Moscow", "http://yandex.ru"));
                add(Resume.of("Mikhail Ivanov", "Sain-Petersburg", "http://google.ru"));
                add(Resume.of("Anna Egorova", "Kostroma", "http://mail.ru"));
            }
        };

        storage.clear();

        for (Resume r : resumes) {
            storage.save(r);
        }
    }

    @Test
    public void testUpdate() {
        resumes.get(0).setFullName("Alexander Ershov");
        Resume updated = resumes.get(0);
        storage.update(updated);
        Assert.assertEquals(updated, storage.load(updated.getId()));
    }

    @Test
    public void testSave() {
        Resume toSave = Resume.of("Dmitry Pozov", "Perm", "http://rambler.ru");
        storage.save(toSave);
        Assert.assertEquals(toSave, storage.load(toSave.getId()));
    }

    @Test
    public void testLoad() {
        Resume fromStorage = resumes.get(1);
        Assert.assertEquals(fromStorage, storage.load(fromStorage.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete() {
        Resume toDelete = resumes.get(2);
        storage.delete(toDelete.getId());
        storage.load(toDelete.getId());
    }


    @Test
    public void testSize() {
        int size = storage.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testClear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getSortedResumeList() {
        Collections.sort(resumes);
        List<Resume> sortedFromStorage = storage.getSortedResumeList();
        Assert.assertEquals(resumes, sortedFromStorage);
    }
}
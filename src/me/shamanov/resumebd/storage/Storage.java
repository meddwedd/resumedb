package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.Resume;

import java.util.List;

/**
 * Author: Mike
 * Date: 30.03.2019
 */

public interface Storage {

    void update(Resume resume);

    void save(Resume resume);

    Resume load(String id);

    void delete(String id);

    List<Resume> getSortedResumeList();

    int size();

    void clear();

}
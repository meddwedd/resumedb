package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.Resume;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public abstract class AbstractStorage<T> implements Storage {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    protected abstract T searchKey(String id);

    protected abstract void doUpdate(Resume resume, T key);

    protected abstract void doSave(Resume resume, T key);

    protected abstract Resume doLoad(T key);

    protected abstract void doDelete(T key);

    protected abstract boolean contains(T key);

    @Override
    public abstract int size();

    @Override
    public abstract void clear();

    @Override
    public abstract List<Resume> getSortedResumeList();

    @Override
    public void update(Resume resume) {
        Objects.requireNonNull(resume);
        T key = searchKey(resume.getId());
        errorIfNotContains(key, () -> "Resume doesn't exist in the storage!");
        doUpdate(resume, key);
    }

    @Override
    public void save(Resume resume) {
        Objects.requireNonNull(resume);
        T key = searchKey(resume.getId());
        errorIfContains(key, () -> "Resume already exists in the storage! Use update instead!");
        doSave(resume, key);
    }

    @Override
    public Resume load(String id) {
        Objects.requireNonNull(id);
        T key = searchKey(id);
        errorIfNotContains(key, () -> "Resume doesn't exist in the storage!");
        return doLoad(key);
    }

    private void errorIfNotContains(T key, Supplier<String> supplier) {
        if (!contains(key)) {
            logger.severe(supplier);
            throw new IllegalArgumentException();
        }
    }

    private void errorIfContains(T key, Supplier<String> supplier) {
        if (contains(key)) {
            logger.severe(supplier);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void delete(String id) {
        Objects.requireNonNull(id);
        T key = searchKey(id);
        errorIfNotContains(key, () -> "Resume doesn't exist in the storage!");
        doDelete(key);
    }

}

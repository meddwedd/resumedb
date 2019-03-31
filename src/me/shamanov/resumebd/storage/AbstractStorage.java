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
    final public void update(Resume resume) {
        Objects.requireNonNull(resume);
        T key = searchKey(resume.getId());
        if (!contains(key)) {
            error(() -> "Resume doesn't exist in the storage!", null);
        }
        doUpdate(resume, key);
    }

    @Override
    final public void save(Resume resume) {
        Objects.requireNonNull(resume);
        T key = searchKey(resume.getId());
        if (contains(key)) {
            error(() -> "Resume already exists in the storage! Use update instead!", null);
        }
        doSave(resume, key);
    }

    @Override
    final public Resume load(String id) {
        Objects.requireNonNull(id);
        T key = searchKey(id);
        if (!contains(key)) {
            error(() -> "Resume doesn't exist in the storage!", null);
        }
        return doLoad(key);
    }


    @Override
    final public void delete(String id) {
        Objects.requireNonNull(id);
        T key = searchKey(id);
        if (!contains(key)) {
            error(() -> "Resume doesn't exist in the storage!", null);
        }
        doDelete(key);
    }

    protected void error(Supplier<String> msg, Throwable cause) throws IllegalArgumentException {
        logger.severe(msg);
        throw new IllegalArgumentException(cause);
    }
}

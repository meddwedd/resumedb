package me.shamanov.resumedb.storage;

import me.shamanov.resumedb.model.Resume;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File dir;

    public AbstractFileStorage(Path dir) {
        this(dir.toFile());
    }

    public AbstractFileStorage(String dir) {
        this(new File(dir));
    }

    public AbstractFileStorage(File dir) {
        Objects.requireNonNull(dir, "Directory must be specified!");
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Specified dir is not a directory!");
        else if (!dir.canWrite())
            throw new IllegalArgumentException("Specified dir is not writable!");
        this.dir = dir;
    }

    protected abstract void write(Resume resume, File file);

    protected abstract Resume read(File file);

    @Override
    protected File searchKey(String id) {
        return new File(dir, id);
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        write(resume, file);
    }

    @Override
    protected void doSave(Resume resume, File file) {
        write(resume, file);
    }

    @Override
    protected Resume doLoad(File file) {
        return read(file);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            logger.info(() -> "File " + file.getAbsolutePath() + " couldn't be deleted!");
        }
    }

    @Override
    protected boolean contains(File file) {
        return file.exists();
    }

    @Override
    public int size() {
        return dir.list().length;
    }

    @Override
    public void clear() {
        for (File file : dir.listFiles()) {
            doDelete(file);
        }
    }

    @Override
    public List<Resume> getSortedResumeList() {
        return Stream.of(dir.list()).map(this::load).sorted().collect(Collectors.toList());
    }
}

package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.Resume;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class SerializableFileStorage extends AbstractFileStorage {

    public SerializableFileStorage(Path dir) {
        super(dir);
    }

    public SerializableFileStorage(String dir) {
        super(dir);
    }

    public SerializableFileStorage(File dir) {
        super(dir);
    }

    @Override
    protected void write(Resume resume, File file) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
            os.writeObject(resume);
        } catch (FileNotFoundException e) {
            logger.severe(() -> "File + " + file + " is not found!");
        } catch (IOException e) {
            logger.severe(() -> "IO problems!");
        }
    }

    @Override
    protected Resume read(File file) {
        Resume resume = null;
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            resume = (Resume) is.readObject();
        } catch (IOException e) {
            logger.severe(() -> "IO problems!");
        } catch (ClassNotFoundException e) {
            logger.severe(() -> "Loading is impossible due to class is not found!");
        }
        return Objects.requireNonNull(resume, "Error occurred while loading resume from " + file.getAbsolutePath() + "!");
    }
}

package me.shamanov.resumedb.storage;

import me.shamanov.resumedb.model.Establishment;
import me.shamanov.resumedb.model.Holder;
import me.shamanov.resumedb.model.Resume;
import me.shamanov.resumedb.utils.XMLUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.nio.file.Path;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class XMLFileStorage extends AbstractFileStorage {
    private XMLUtil xmlUtil = new XMLUtil(Resume.class, Holder.class, Establishment.class);

    public XMLFileStorage(Path dir) {
        super(dir);
    }

    public XMLFileStorage(String dir) {
        super(dir);
    }

    public XMLFileStorage(File dir) {
        super(dir);
    }

    @Override
    protected void write(Resume resume, File file) {
        try {
            xmlUtil.marshal(resume, file);
        } catch (JAXBException e) {
            throw new RuntimeException("Error while saving to XML file at " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected Resume read(File file) {
        try {
            return xmlUtil.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException("Error while loading from XML file at " + file.getAbsolutePath(), e);
        }
    }
}

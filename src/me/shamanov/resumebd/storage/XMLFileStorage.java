package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.Establishment;
import me.shamanov.resumebd.model.Resume;
import me.shamanov.resumebd.model.Section;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Path;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class XMLFileStorage extends AbstractFileStorage {

    /**
     * Helping class to initialize JAXB context and do marshalling and unmarshalling of the specified classes.
     */
    private static class XMLUtil {
        private static JAXBContext context;
        private static Marshaller marshaller;
        private static Unmarshaller unmarshaller;

        static {
            try {
                context = JAXBContext.newInstance(Resume.class, Section.class, Establishment.class);
                marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                unmarshaller = context.createUnmarshaller();
            } catch (JAXBException e) {
                throw new RuntimeException("JAXBContext couldn't be instantiated!", e);
            }
        }

        static void marshal(Resume resume, File file) throws JAXBException {
            marshaller.marshal(resume, file);
        }

        static Resume unmarshal(File file) throws JAXBException {
            return (Resume) unmarshaller.unmarshal(file);
        }
    }

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
            XMLUtil.marshal(resume, file);
        } catch (JAXBException e) {
            throw new RuntimeException("Error while saving to XML file at " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected Resume read(File file) {
        try {
            return XMLUtil.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException("Error while loading from XML file at " + file.getAbsolutePath(), e);
        }
    }
}

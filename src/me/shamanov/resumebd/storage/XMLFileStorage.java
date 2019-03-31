package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.ContactType;
import me.shamanov.resumebd.model.Resume;
import me.shamanov.resumebd.model.Section;
import me.shamanov.resumebd.model.SectionType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public class XMLFileStorage extends AbstractFileStorage {

    /**
     * Helping class to initialize JAXB context and do marshalling and unmarshalling of specified class.
     * In this case - {@link XMLResume}.
     *
     * @see XMLResume
     */
    private static class XMLUtil {
        private static JAXBContext context;
        private static Marshaller marshaller;
        private static Unmarshaller unmarshaller;

        static {
            try {
                context = JAXBContext.newInstance(XMLResume.class);
                marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                unmarshaller = context.createUnmarshaller();
            } catch (JAXBException e) {
                throw new RuntimeException("JAXBContext couldn't be instantiated!", e);
            }
        }

        static void marshal(XMLResume resume, File file) throws JAXBException {
            marshaller.marshal(resume, file);
        }

        static XMLResume unmarshal(File file) throws JAXBException {
            return (XMLResume) unmarshaller.unmarshal(file);
        }
    }

    /**
     * This class is intended to be a mockery for {@link Resume} class so that the real class would have no public constructors
     * as it is needed for JAXB library and there would be no direct access to the final id field of {@link Resume} class.
     * It has only one method {@link XMLResume#asResume()} which returns an instance of {@link Resume} upon the loaded fields from
     * XML file.
     *
     * @see XMLUtil
     */
    @XmlRootElement(name = "resume")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class XMLResume {
        @XmlID
        private String id;
        private String fullName;
        private String location;
        private String homepage;
        private Map<ContactType, String> contacts;
        private Map<SectionType, Section> sections;

        XMLResume() {
            //used by JAXB
        }

        XMLResume(Resume resume) {
            this.id = resume.getId();
            this.fullName = resume.getFullName();
            this.location = resume.getLocation();
            this.homepage = resume.getHomepage();
            this.contacts = resume.getContacts();
            this.sections = resume.getSections();
        }

        Resume asResume() {
            Resume resume = Resume.of(this.fullName, this.location, this.homepage);

            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                resume.addContact(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                resume.addSection(entry.getKey(), entry.getValue());
            }

            Field field;
            try {
                field = resume.getClass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(resume, this.id);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return resume;
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
            XMLUtil.marshal(new XMLResume(resume), file);
        } catch (JAXBException e) {
            throw new RuntimeException("Error while saving to XML file at " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected Resume read(File file) {
        try {
            XMLResume xmlResume = XMLUtil.unmarshal(file);
            return xmlResume.asResume();
        } catch (JAXBException e) {
            throw new RuntimeException("Error while loading from XML file at " + file.getAbsolutePath(), e);
        }
    }
}

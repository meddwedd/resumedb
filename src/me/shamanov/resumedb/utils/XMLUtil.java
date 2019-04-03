package me.shamanov.resumedb.utils;

/**
 * Author: Mike
 * Date: 02.04.2019
 */

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Helping class to initialize JAXB context and do marshalling and unmarshalling of the specified classes.
 */
public class XMLUtil {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XMLUtil(Class<?>... classes) {
        try {
            JAXBContext context = JAXBContext.newInstance(classes);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("JAXBContext couldn't be instantiated!", e);
        }
    }

    public <T> void marshal(T object, File file) throws JAXBException {
        marshaller.marshal(object, file);
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshal(File file) throws JAXBException {
        return (T) unmarshaller.unmarshal(file);
    }
}
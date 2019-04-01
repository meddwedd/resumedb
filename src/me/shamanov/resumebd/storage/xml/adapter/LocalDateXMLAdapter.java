package me.shamanov.resumebd.storage.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Author: Mike
 * Date: 01.04.2019
 */

public class LocalDateXMLAdapter extends XmlAdapter<String, LocalDate> {

    protected LocalDateXMLAdapter() {
        super();
    }

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}

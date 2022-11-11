package org.legaldtax.gst2a.dto.xml;

import org.legaldtax.gst2a.dto.xmlenum.Tallyrequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HEADER")
public class Header {
    private Tallyrequest tallyrequest = Tallyrequest.IMPORT_DATA;
    @XmlElement(name = "TALLYREQUEST")
    public Tallyrequest getTallyrequest() {
        return tallyrequest;
    }

    public void setTallyrequest(Tallyrequest value) {
        this.tallyrequest = value;
    }
}

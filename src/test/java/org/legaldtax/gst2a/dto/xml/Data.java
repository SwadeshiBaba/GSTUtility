package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DATA")
public class Data {
    private Envelope[] envelope;
    private String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";
    @XmlElement(name = "ENVELOPE")
    public Envelope[] getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope[] value) {
        this.envelope = value;
    }
    @XmlAttribute(name = "xmlns:xsi")
    public String getXmlnsXsi() {
        return xmlnsXsi;
    }

    public void setXmlnsXsi(String value) {
        this.xmlnsXsi = value;
    }
}

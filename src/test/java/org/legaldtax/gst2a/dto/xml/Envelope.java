package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;

@XmlRootElement(name = "ENVELOPE")
@XmlType(propOrder={"header", "body"})
public class Envelope {


    private String partyGSTNumber;
    private Header header = new Header();
    private Body body = new Body();

    public Envelope() throws IOException {
    }

    @XmlElement(name = "HEADER")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        this.header = value;
    }

    @XmlTransient
    public String getPartyGSTNumber() {
        return partyGSTNumber;
    }

    public void setPartyGSTNumber(String partyGSTNumber) {
        this.partyGSTNumber = partyGSTNumber;
    }

    @XmlElement(name = "BODY")
    public Body getBody() {
        return body;
    }

    public void setBody(Body value) {
        this.body = value;
    }


}

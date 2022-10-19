package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;

@XmlRootElement(name="REQUESTDATA")
public class Requestdata {
    private Tallymessage tallymessage = new Tallymessage();

    public Requestdata() throws IOException {
    }

    @XmlElement(name = "TALLYMESSAGE")
    public Tallymessage getTallymessage() {
        return tallymessage;
    }

    public void setTallymessage(Tallymessage value) {
        this.tallymessage = value;
    }
}

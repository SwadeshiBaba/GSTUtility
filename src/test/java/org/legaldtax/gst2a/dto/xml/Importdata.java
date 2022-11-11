package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;

@XmlRootElement(name="IMPORTDATA")
@XmlType(propOrder = {"requestdesc","requestdata"})
public class Importdata {

    private Requestdesc requestdesc = new Requestdesc();
    private Requestdata requestdata = new Requestdata();

    public Importdata() throws IOException {
    }

    @XmlElement(name = "REQUESTDESC")
    public Requestdesc getRequestdesc() {
        return requestdesc;
    }

    public void setRequestdesc(Requestdesc value) {
        this.requestdesc = value;
    }

    @XmlElement(name = "REQUESTDESC")
    public Requestdata getRequestdata() {
        return requestdata;
    }

    public void setRequestdata(Requestdata value) {
        this.requestdata = value;
    }
}

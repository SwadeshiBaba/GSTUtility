package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

@XmlRootElement(name = "BODY")
public class Body {
    private Importdata importdata = new Importdata();

    public Body() throws IOException {
    }

    @XmlElement(name = "IMPORTDATA")
    public Importdata getImportdata() {
        return importdata;
    }

    public void setImportdata(Importdata value) {
        this.importdata = value;
    }
}

package org.legaldtax.gst2a.dto.xml;

import org.legaldtax.gst2a.GSTR2AExcelToTallyXml;
import org.legaldtax.gst2a.dto.xmlenum.Svcurrentcompany;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

@XmlRootElement(name="STATICVARIABLES")
public class Staticvariables {
    private String svcurrentcompany = GSTR2AExcelToTallyXml.properties.getProperty("svCurrentCompany");

    public Staticvariables() throws IOException {
    }

    @XmlElement(name = "SVCURRENTCOMPANY")
    public String getSvcurrentcompany() {
        return svcurrentcompany;
    }

    public void setSvcurrentcompany(String value) {
        this.svcurrentcompany = value;
    }
}

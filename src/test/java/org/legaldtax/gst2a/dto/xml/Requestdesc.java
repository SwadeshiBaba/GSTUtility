package org.legaldtax.gst2a.dto.xml;

import org.legaldtax.gst2a.dto.xmlenum.Reportname;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;

@XmlRootElement(name="REQUESTDESC")
@XmlType(propOrder = {"reportname","staticvariables"})
public class Requestdesc {
    private Reportname reportname = Reportname.VOUCHERS;
    private Staticvariables staticvariables = new Staticvariables();

    public Requestdesc() throws IOException {
    }

    @XmlElement(name = "REPORTNAME")
    public Reportname getReportname() {
        return reportname;
    }

    public void setReportname(Reportname value) {
        this.reportname = value;
    }

    @XmlElement(name = "STATICVARIABLES")
    public Staticvariables getStaticvariables() {
        return staticvariables;
    }

    public void setStaticvariables(Staticvariables value) {
        this.staticvariables = value;
    }
}

package org.legaldtax.gst2a.dto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;

@XmlRootElement(name="REQUESTDATA")
@XmlType(propOrder = {"voucher"})
public class Tallymessage {
    private Voucher voucher;

    public Tallymessage() throws IOException {
    }

    @XmlElement(name = "VOUCHER")
    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher value) {
        this.voucher = value;
    }
}

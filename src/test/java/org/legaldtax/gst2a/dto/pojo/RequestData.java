package org.legaldtax.gst2a.dto.pojo;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "REQUESTDATA")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestData{

    @XmlElement(name = "VOUCHERTYPENAME")
    private String voucherTypeName;
}

package org.legaldtax.gst2a.dto.xml;

import org.legaldtax.gst2a.dto.xmlenum.BooleanEnum;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ALLLEDGERENTRIES.LIST")
@XmlType(propOrder = {"ledgername","removezeroentries","ledgerfromitem","isdeemedpositive","amount"})
public class AllledgerentriesList {
    private String ledgername;
    private BooleanEnum removezeroentries = BooleanEnum.YES;
    private BooleanEnum ledgerfromitem = BooleanEnum.NO;
    private BooleanEnum isdeemedpositive;
    private Double amount;

    public AllledgerentriesList(){

    }

    public AllledgerentriesList(String ledgername, BooleanEnum isdeemedpositive, Double amount) {
        this.ledgername = ledgername;
        this.isdeemedpositive = isdeemedpositive;
        this.amount = amount;
    }
    @XmlElement(name = "LEDGERNAME")
    public String getLedgername() {
        return ledgername;
    }

    public void setLedgername(String value) {
        this.ledgername = value;
    }

    @XmlElement(name = "REMOVEZEROENTRIES")
    public BooleanEnum getRemovezeroentries() {
        return removezeroentries;
    }

    public void setRemovezeroentries(BooleanEnum value) {
        this.removezeroentries = value;
    }

    @XmlElement(name = "LEDGERFROMITEM")
    public BooleanEnum getLedgerfromitem() {
        return ledgerfromitem;
    }

    public void setLedgerfromitem(BooleanEnum value) {
        this.ledgerfromitem = value;
    }
    @XmlElement(name = "ISDEEMEDPOSITIVE")
    public BooleanEnum getIsdeemedpositive() {
        return isdeemedpositive;
    }

    public void setIsdeemedpositive(BooleanEnum value) {
        this.isdeemedpositive = value;
    }

    @XmlElement(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double value) {
        this.amount = value;
    }
}

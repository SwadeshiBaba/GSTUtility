package org.legaldtax.gst2a.dto.pojo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "VOUCHER")
@XmlAccessorType(XmlAccessType.FIELD)
public class Voucher {

    @XmlAttribute(name = "ACTION")
    private String action = "Create";

    @XmlAttribute(name = "VCHTYPE")
    private String vchType = "Purchase";

    @XmlElement(name = "VOUCHERTYPENAME")
    private String voucherTypeName;

    @XmlElement(name = "ISINVOICE")
    private String isInvoice = "YES";

    @XmlElement(name = "DATE")
    private String date;

    @XmlElement(name = "REFERENCEDATE")
    private String referenceDate;

    @XmlElement(name = "REFERENCE")
    private String reference;

    @XmlElement(name = "PARTYLEDGERNAME")
    private String partyLedgerName;

    @XmlElement(name = "NARRATION")
    private String narration = "Utils Import:Goods Purchase";

    @XmlElement(name = "EFFECTIVEDATE")
    private String effectiveDate;

    @XmlElement(name = "ALLLEDGERENTRIES.LIST")
    List<AllLedgerentries> allLedgerentriesList;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVchType() {
        return vchType;
    }

    public void setVchType(String vchType) {
        this.vchType = vchType;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPartyLedgerName() {
        return partyLedgerName;
    }

    public void setPartyLedgerName(String partyLedgerName) {
        this.partyLedgerName = partyLedgerName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "action='" + action + '\'' +
                ", vchType='" + vchType + '\'' +
                ", voucherTypeName='" + voucherTypeName + '\'' +
                ", isInvoice='" + isInvoice + '\'' +
                ", date='" + date + '\'' +
                ", referenceDate='" + referenceDate + '\'' +
                ", reference='" + reference + '\'' +
                ", partyLedgerName='" + partyLedgerName + '\'' +
                ", narration='" + narration + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                '}';
    }
}

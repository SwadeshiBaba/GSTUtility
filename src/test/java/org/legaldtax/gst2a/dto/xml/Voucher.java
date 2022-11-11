package org.legaldtax.gst2a.dto.xml;

import org.legaldtax.gst2a.GSTR2AExcelToTallyXml;
import org.legaldtax.gst2a.dto.xmlenum.*;

import javax.xml.bind.annotation.*;
import java.io.IOException;

@XmlRootElement(name = "VOUCHER")
@XmlType(propOrder = {"vouchertypename","isinvoice","date","referencedate","reference","partyledgername","narration","effectivedate","allledgerentriesList"})
public class Voucher {

    private Action action = Action.CREATE;
    private Vchtype vchtype = Vchtype.forValue(GSTR2AExcelToTallyXml.properties.getProperty("vchType"));
    private Vouchertypename vouchertypename = Vouchertypename.forValue(GSTR2AExcelToTallyXml.properties.getProperty("voucherTypeName"));
    private BooleanEnum isinvoice = BooleanEnum.YES;
    private String date;
    private String referencedate;
    private String reference;
    private String partyledgername;
    private String narration = GSTR2AExcelToTallyXml.properties.getProperty("narration");
    private String effectivedate;
    private AllledgerentriesList[] allledgerentriesList;

    private String placeOfService;

    public Voucher() throws IOException {
    }

    @XmlAttribute(name = "ACTION")
    public Action getAction() {
        return action;
    }

    public void setAction(Action value) {
        this.action = value;
    }
    @XmlAttribute(name = "VCHTYPE")
    public Vchtype getVchtype() {
        return vchtype;
    }

    public void setVchtype(Vchtype value) {
        this.vchtype = value;
    }

    @XmlElement(name = "VOUCHERTYPENAME")
    public Vouchertypename getVouchertypename() {
        return vouchertypename;
    }

    public void setVouchertypename(Vouchertypename value) {
        this.vouchertypename = value;
    }

    @XmlElement(name = "ISINVOICE")
    public BooleanEnum getIsinvoice() {
        return isinvoice;
    }

    public void setIsinvoice(BooleanEnum value) {
        this.isinvoice = value;
    }

    @XmlElement(name = "DATE")
    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

    @XmlElement(name = "REFERENCEDATE")
    public String getReferencedate() {
        return referencedate;
    }

    public void setReferencedate(String value) {
        this.referencedate = value;
    }

    @XmlElement(name = "REFERENCE")
    public String getReference() {
        return reference;
    }

    public void setReference(String value) {
        this.reference = value;
    }

    @XmlElement(name = "PARTYLEDGERNAME")
    public String getPartyledgername() {
        return partyledgername;
    }

    public void setPartyledgername(String value) {
        this.partyledgername = value;
    }

    @XmlElement(name = "NARRATION")
    public String getNarration() {
        return narration;
    }

    public void setNarration(String value) {
        this.narration = value;
    }

    @XmlElement(name = "EFFECTIVEDATE")
    public String getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(String value) {
        this.effectivedate = value;
    }

    @XmlElement(name = "ALLLEDGERENTRIES.LIST")
    public AllledgerentriesList[] getAllledgerentriesList() {
        return allledgerentriesList;
    }

    public void setAllledgerentriesList(AllledgerentriesList[] value) {
        this.allledgerentriesList = value;
    }


    @XmlTransient
    public String getPlaceOfService() {
        return placeOfService;
    }

    public void setPlaceOfService(String placeOfService) {
        this.placeOfService = placeOfService;
    }
}

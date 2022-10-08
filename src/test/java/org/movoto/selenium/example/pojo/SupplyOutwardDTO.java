package org.movoto.selenium.example.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SupplyOutwardDTO {

    String gstr1Section;
    String gstr3BSection;
    String party;
    String gstn;
    String placeOfSupply;
    String docNo;
    Date docDate;
    Double invoiceValue;

    List<GstCalculationDTO> gstCalculationDTOList;

    String errorDescription;

    public SupplyOutwardDTO(){
        this.setGstCalculationList(new ArrayList<>());
    };

    public String getGstr1Section() {
        return gstr1Section;
    }

    public void setGstr1Section(String gstr1Section) {
        this.gstr1Section = gstr1Section;
    }

    public String getGstr3BSection() {
        return gstr3BSection;
    }

    public void setGstr3BSection(String gstr3BSection) {
        this.gstr3BSection = gstr3BSection;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<GstCalculationDTO> getGstCalculationList() {
        return gstCalculationDTOList;
    }

    public void setGstCalculationList(List<GstCalculationDTO> gstCalculationDTOList) {
        this.gstCalculationDTOList = gstCalculationDTOList;
    }

    @Override
    public String toString() {
        return "SupplyOutwardRegisterBase{" +
                "gstr1Section='" + gstr1Section + '\'' +
                ", gstr3BSection='" + gstr3BSection + '\'' +
                ", party='" + party + '\'' +
                ", gstn='" + gstn + '\'' +
                ", placeOfSupply='" + placeOfSupply + '\'' +
                ", docNo='" + docNo + '\'' +
                ", docDate='" + docDate + '\'' +
                ", invoiceValue=" + invoiceValue +
                ", gstCalculationList=" + gstCalculationDTOList +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}

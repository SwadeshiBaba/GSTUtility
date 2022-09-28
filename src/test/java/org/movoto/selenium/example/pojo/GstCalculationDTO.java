package org.movoto.selenium.example.pojo;


public class GstCalculationDTO {

    Double taxableValue;
    Double taxRate;
    Double igstVal;
    Double cgstVal;
    Double sgstVal;
    Double cessVal;

    public Double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(Double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getIgstVal() {
        return igstVal;
    }

    public void setIgstVal(Double igstVal) {
        this.igstVal = igstVal;
    }

    public Double getCgstVal() {
        return cgstVal;
    }

    public void setCgstVal(Double cgstVal) {
        this.cgstVal = cgstVal;
    }

    public Double getSgstVal() {
        return sgstVal;
    }

    public void setSgstVal(Double sgstVal) {
        this.sgstVal = sgstVal;
    }

    public Double getCessVal() {
        return cessVal;
    }

    public void setCessVal(Double cessVal) {
        this.cessVal = cessVal;
    }

    @Override
    public String toString() {
        return "GstCalculation{" +
                "taxableValue=" + taxableValue +
                ", taxRate=" + taxRate +
                ", igstVal=" + igstVal +
                ", cgstVal=" + cgstVal +
                ", sgstVal=" + sgstVal +
                ", cessVal=" + cessVal +
                '}';
    }
}

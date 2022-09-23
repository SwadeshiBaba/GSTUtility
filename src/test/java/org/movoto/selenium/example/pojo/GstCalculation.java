package org.movoto.selenium.example.pojo;

public class GstCalculation {

    Integer taxableValue;
    Integer taxRate;
    Integer igstVal;
    Integer cgstVal;
    Integer sgstVal;
    Integer cessVal;

    public Integer getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(Integer taxableValue) {
        this.taxableValue = taxableValue;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getIgstVal() {
        return igstVal;
    }

    public void setIgstVal(Integer igstVal) {
        this.igstVal = igstVal;
    }

    public Integer getCgstVal() {
        return cgstVal;
    }

    public void setCgstVal(Integer cgstVal) {
        this.cgstVal = cgstVal;
    }

    public Integer getSgstVal() {
        return sgstVal;
    }

    public void setSgstVal(Integer sgstVal) {
        this.sgstVal = sgstVal;
    }

    public Integer getCessVal() {
        return cessVal;
    }

    public void setCessVal(Integer cessVal) {
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

package org.legaldtax.gst2a.dto.pojo;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ALLLEDGERENTRIES.LIST")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllLedgerentries {

    @XmlElement(name = "LEDGERNAME")
    private String ledgerName;

    @XmlElement(name = "REMOVEZEROENTRIES")
    private String removeZeroEntries = "YES";

    @XmlElement(name = "LEDGERFROMITEM")
    private String ledgerFromItem = "NO";

    @XmlElement(name = "ISDEEMEDPOSITIVE")
    private String isDeemedPositive;

    @XmlElement(name = "AMOUNT")
    private Double amount;

    public AllLedgerentries(){

    }

    public AllLedgerentries(String ledgerName, String isDeemedPositive, Double amount){
        this.ledgerName = ledgerName;
        this.isDeemedPositive = isDeemedPositive;
        this.amount = amount;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getRemoveZeroEntries() {
        return removeZeroEntries;
    }

    public void setRemoveZeroEntries(String removeZeroEntries) {
        this.removeZeroEntries = removeZeroEntries;
    }

    public String getLedgerFromItem() {
        return ledgerFromItem;
    }

    public void setLedgerFromItem(String ledgerFromItem) {
        this.ledgerFromItem = ledgerFromItem;
    }

    public String getIsDeemedPositive() {
        return isDeemedPositive;
    }

    public void setIsDeemedPositive(String isDeemedPositive) {
        this.isDeemedPositive = isDeemedPositive;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AllLedgerentries{" +
                "ledgerName='" + ledgerName + '\'' +
                ", removeZeroEntries='" + removeZeroEntries + '\'' +
                ", ledgerFromItem='" + ledgerFromItem + '\'' +
                ", isDeemedPositive='" + isDeemedPositive + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

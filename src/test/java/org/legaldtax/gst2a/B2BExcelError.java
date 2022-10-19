package org.legaldtax.gst2a;

public enum B2BExcelError {
    UNEXPECTED("Unexpected error"),
    INVALID_ROW("Invalid Row"),

    GST_PARTY_LEDGER_NAME_EMPTY("GST party ledger name is empty"),

    INVOICE_NO_EMPTY("Invoice number is empty"),
    INVOICE_DATE_EMPTY("Invoice date is empty"),
    INVOICE_REFERENCE_DATE_EMPTY("Invoice reference date is empty"),
    INVOICE_EFFECTIVE_DATE_EMPTY("Invoice effective date is empty"),

    PLACE_OF_SUPPY_IS_EMPTY("Place of Supply is empty"),
    GST_NUMBER_EMPTY("GST Number is empty"),
    GSTN_TOO_LENGHTY("GSTN exeeds maximum length of 15 characters"),
    PARTY_IS_EMPTY("Party is empty"),
    INVOICE_VALUE_EMPTY("Invoice value is empty"),
    TAXABLE_VALUE_IS_EMPTY("Taxable Value is empty"),

    TAX_RATE_IS_EMPTY("Tax Rate is empty");

    private String message;

    private B2BExcelError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

package org.movoto.selenium.example.bulkUploadSupplyOutward;

public enum BulkSupplyOutwardError {
    UNEXPECTED("Unexpected error"),
    INVALID_ROW("Invalid Row"),

    GSTR_1_SECTION_IS_EMPTY("GSTR_1 Section is empty"),

    GSTR_3B_SECTION_IS_EMPTY("GSTR_3B Section is empty"),
    GSTN_IS_EMPTY("GSTN is empty"),

    PLACE_OF_SUPPY_IS_EMPTY("Place of Supply is empty"),
    GSTN_TOO_LENGHTY("GSTN exeeds maximum length of 15 characters"),
    PARTY_IS_EMPTY("Party is empty"),
    DOC_NO_IS_EMPTY("Doc No is empty"),
    TAXABLE_VALUE_IS_EMPTY("Taxable Value is empty"),

    TAX_RATE_IS_EMPTY("Tax Rate is empty");

    private String message;

    private BulkSupplyOutwardError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

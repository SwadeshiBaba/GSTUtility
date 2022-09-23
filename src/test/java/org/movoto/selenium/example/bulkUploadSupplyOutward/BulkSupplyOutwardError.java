package org.movoto.selenium.example.bulkUploadSupplyOutward;

public enum BulkSupplyOutwardError {
    UNEXPECTED("Unexpected error"),
    INVALID_ROW("Invalid Row"),
    GSTN_IS_EMPTY("GSTN is empty"),
    GSTN_TOO_LENGHTY("GSTN exeeds maximum length of 15 characters"),
    PARTY_IS_EMPTY("Party is empty"),
    DOC_NO_IS_EMPTY("Doc No is empty");

    private String message;

    private BulkSupplyOutwardError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

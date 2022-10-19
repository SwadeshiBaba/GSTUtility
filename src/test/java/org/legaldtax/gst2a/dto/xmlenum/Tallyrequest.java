package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Tallyrequest {
    IMPORT_DATA;

    public String toValue() {
        switch (this) {
            case IMPORT_DATA:
                return "Import Data";
        }
        return null;
    }

    public static Tallyrequest forValue(String value) throws IOException {
        if (value.equals("Import Data")) return IMPORT_DATA;
        throw new IOException("Cannot deserialize Tallyrequest");
    }
}

package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Vouchertypename {
    PURCHASE;

    public String toValue() {
        switch (this) {
            case PURCHASE: return "Purchase";
        }
        return null;
    }

    public static Vouchertypename forValue(String value) throws IOException {
        if (value.equals("Purchase")) return PURCHASE;
        throw new IOException("Cannot deserialize Vouchertypename");
    }
}
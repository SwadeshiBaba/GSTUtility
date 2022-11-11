package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Vchtype {
    PURCHASE;

    public String toValue() {
        switch (this) {
            case PURCHASE: return "Purchase";
        }
        return null;
    }

    public static Vchtype forValue(String value) throws IOException {
        if (value.equals("Purchase")) return PURCHASE;
        throw new IOException("Cannot deserialize Vchtype");
    }
}
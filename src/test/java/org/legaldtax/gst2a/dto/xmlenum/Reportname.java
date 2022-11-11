package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Reportname {
    VOUCHERS;

    public String toValue() {
        switch (this) {
            case VOUCHERS:
                return "Vouchers";
        }
        return null;
    }

    public static Reportname forValue(String value) throws IOException {
        if (value.equals("Vouchers")) return VOUCHERS;
        throw new IOException("Cannot deserialize Reportname");
    }
}

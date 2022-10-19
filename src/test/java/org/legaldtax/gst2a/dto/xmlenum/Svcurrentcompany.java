package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Svcurrentcompany {
    SUPER_MEGA_STORE_1819_FROM_1_APR_2020;

    public String toValue() {
        switch (this) {
            case SUPER_MEGA_STORE_1819_FROM_1_APR_2020:
                return "Super Mega Store (18-19) - (from 1-Apr-2020)";
        }
        return null;
    }

    public static Svcurrentcompany forValue(String value) throws IOException {
        if (value.equals("Super Mega Store (18-19) - (from 1-Apr-2020)")) return SUPER_MEGA_STORE_1819_FROM_1_APR_2020;
        throw new IOException("Cannot deserialize Svcurrentcompany");
    }

}

package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum Action {
    CREATE;

    public String toValue() {
        switch (this) {
            case CREATE: return "Create";
        }
        return null;
    }

    public static Action forValue(String value) throws IOException {
        if (value.equals("Create")) return CREATE;
        throw new IOException("Cannot deserialize Action");
    }
}


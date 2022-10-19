package org.legaldtax.gst2a.dto.xmlenum;

import java.io.IOException;

public enum BooleanEnum {
    NO, YES;

    public String toValue() {
        switch (this) {
            case NO: return "NO";
            case YES: return "YES";
        }
        return null;
    }

    public static BooleanEnum forValue(String value) throws IOException {
        if (value.equals("NO")) return NO;
        if (value.equals("YES")) return YES;
        throw new IOException("Cannot deserialize BooleanEnum");
    }
}

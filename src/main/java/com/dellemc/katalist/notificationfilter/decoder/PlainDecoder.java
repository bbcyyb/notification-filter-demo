package com.dellemc.katalist.notificationfilter.decoder;

import java.util.Map;

public class PlainDecoder implements Decode {
    public Map<String, Object> decode(final String message) {
        return createDefaultEvent(message);
    }
}

package com.dellemc.katalist.notificationfilter.decoder;

import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;

public interface Decode {
    Map<String, Object> decode(String message);

    default Map<String, Object> createDefaultEvent(String message) {
        return new HashMap<String, Object>() {
            put("message", message);
            put("@timestamp", DateTime.now());
        };
    }
}

package com.dellemc.katalist.notificationfilter.decoder;

import java.util.Map;

import org.json.simple.JSONValue;


public class JsonDecoder implements Decode {

    @Override
    public Map<String, Object> decode(String message) {
        Map<String, Object> event = null;
        try {
            event = (Map<String, Object>) JSONValue.parseWithException(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (event == null) {
                event = createDefaultEvent(message);
            }
            return event;
        }
    }
}

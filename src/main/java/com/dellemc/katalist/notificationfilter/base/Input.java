package com.dellemc.katalist.notificationfilter.base;
import com.dellemc.katalist.notificationfilter.decoder.Decode;

import java.util.Map;

public abstract class Input implements Base {

    public Input() {
        init();
    }

    public void process(Decode decoder, String message) {
        try {
            Map<String, Object> event = decoder.decode(message);
            preprocess(event);
            doProcess(event);
            postprocess(event);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Map<String, Object> preprocess(Map<String, Object> event) {
        return event;
    }

    protected Map<String, Object> postprocess(Map<String, Object> event) {
        return event;
    }


    protected abstract void init();
    protected abstract void doProcess(Map<String, Object> event);
}

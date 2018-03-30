package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.decoder.Decode;

import java.util.Map;

public abstract class Input implements Job {

    private Filter filterProcessor;

    public Filter getFilterProcessor() {
        return filterProcessor;
    }

    public void setFilterProcessor(Filter filterProcessor) {
        this.filterProcessor = filterProcessor;
    }

    public Input() {
        init();
    }

    public void process(Decode decoder, String message, Context context) {
        try {
            Map<String, Object> event = decoder.decode(message);
            doProcess(event, context);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void init();

    protected abstract void doProcess(Map<String, Object> event, Context context);

    public abstract void emit();
}

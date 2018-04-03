package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.decoder.Decode;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Input implements Job {

    private Filter filterProcessor;
    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public Filter getFilterProcessor() {
        return filterProcessor;
    }

    public void setFilterProcessor(Filter filterProcessor) {
        this.filterProcessor = filterProcessor;
    }

    public void process(Decode decoder, String message, Context context) {
        try {
            Map<String, Object> event = decoder.decode(message);
            doProcess(event, context);
        } catch (OutOfMemoryError ex) {
            logger.error("OutOfMemoryError: ", ex);
            System.exit(1);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    protected abstract void doProcess(Map<String, Object> event, Context context);

    public abstract void emit();
}

package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Output implements Job {

    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public void process(Map<String, Object> event, Context context) {
        try {
            doProcess(event, context);
        } catch (OutOfMemoryError ex) {
            logger.error("OutOfMemoryError: ", ex);
            System.exit(1);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    protected abstract void doProcess(Map<String, Object> event, Context context);

}

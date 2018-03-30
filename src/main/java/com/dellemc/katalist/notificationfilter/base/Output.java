package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import java.util.Map;

public abstract class Output implements Job {

    @Override
    public void shutdown() {

    }

    public void process(Map<String, Object> event, Context context) {
        try {
            doProcess(event, context);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doProcess(Map<String, Object> event, Context context);

}

package com.dellemc.katalist.notificationfilter.filter;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.job.JobStatus;

import java.util.Map;

public class CrsFilter extends Filter {
    @Override
    protected void doProcess(Map<String, Object> event, Context context) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    protected void doRollBack(Map<String, Object> event, Context context) {

    }
}

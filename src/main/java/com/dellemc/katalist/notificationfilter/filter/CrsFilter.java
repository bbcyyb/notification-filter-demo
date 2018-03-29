package com.dellemc.katalist.notificationfilter.filter;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.job.JobStatus;

public class CrsFilter extends Filter {
    @Override
    public void shutdown() {

    }

    @Override
    protected void doHandle(Context context) throws Exception {

    }

    @Override
    protected void doRollBack(Context context) {

    }

    @Override
    public JobStatus call() throws Exception {
        return null;
    }
}

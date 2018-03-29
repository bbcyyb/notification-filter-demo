package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.job.JobChainHandler;
import com.dellemc.katalist.notificationfilter.job.JobStatus;

import java.util.concurrent.Callable;

public abstract class Filter extends JobChainHandler implements Job, Callable<JobStatus>, Comparable<Filter> {
    private int priority = 0;

    public int getPriority() {
        return priority;
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public int compareTo(Filter filter) {
        return getPriority() - filter.getPriority();
    }

    public void process(Context context) {
        handle(context);
    }
}

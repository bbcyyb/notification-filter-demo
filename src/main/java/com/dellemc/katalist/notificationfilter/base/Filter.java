package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.job.AbstractJobHandler;
import com.dellemc.katalist.notificationfilter.job.JobStatus;
import java.util.concurrent.Callable;

public abstract class Filter extends AbstractJobHandler implements Base, Callable<JobStatus>, Comparable<Filter> {
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
}

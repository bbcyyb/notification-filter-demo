package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.job.JobChainHandler;
import com.dellemc.katalist.notificationfilter.job.JobStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Filter extends JobChainHandler implements Job, Comparable<Filter> {
    private int priority = 0;
    private List<Output> outputProcessors;

    public int getPriority() {
        return priority;
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Output> getOutputProcessors() {
        return outputProcessors;
    }

    public void setOutputProcessors(List<Output> outputProcessors) {
        this.outputProcessors = outputProcessors;
    }

    public int compareTo(Filter filter) {
        return getPriority() - filter.getPriority();
    }

    public void process(Map<String, Object> event, Context context) {
        handle(event, context);
    }

    protected void doHandle(Map<String, Object> event, Context context) {
        doProcess(event, context);
    }

    protected abstract void doProcess(Map<String, Object> event, Context context);
}

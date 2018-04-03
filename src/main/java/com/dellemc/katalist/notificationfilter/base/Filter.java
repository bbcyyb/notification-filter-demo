package com.dellemc.katalist.notificationfilter.base;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.job.JobChainHandler;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Filter extends JobChainHandler implements Job, Comparable<Filter> {
    protected Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
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

    protected void doHandle(Map<String, Object> event, Context context, boolean lastOne) {
        doProcess(event, context, lastOne);
    }

    protected abstract void doProcess(Map<String, Object> event, Context context, boolean lastOne);
}

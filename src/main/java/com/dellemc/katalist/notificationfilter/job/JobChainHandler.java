package com.dellemc.katalist.notificationfilter.job;

import com.dellemc.katalist.notificationfilter.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class JobChainHandler {
    private JobChainHandler preHandler;
    private JobChainHandler nextHandler;
    private Logger logger = LoggerFactory.getLogger(JobChainHandler.class);

    public JobChainHandler getPreHandler() {
        return preHandler;
    }

    public void setPreHandler(JobChainHandler preHandler) {
        this.preHandler = preHandler;
    }

    public JobChainHandler getNextHandler() {
        return nextHandler;
    }

    public JobChainHandler setNextHandler(JobChainHandler nextHandler) {
        this.nextHandler = nextHandler;
        nextHandler.setPreHandler(this);
        return nextHandler;
    }

    public void handle(Map<String, Object> event, Context context) {
        try {
            doHandle(event, context);
        } catch (Exception ex) {
            logger.error("Error occur, start to rollback logic, Exception from " + this.getClass().getName() + " - exiting: " + ex.getMessage());
            rollBack(event, context);
            return;
        }
        if (nextHandler != null) {
            nextHandler.handle(event, context);
        }
    }

    public void rollBack(Map<String, Object> event, Context context) {
        doRollBack(event, context);

        if (preHandler != null) {
            preHandler.rollBack(event, context);
        }
    }

    protected abstract void doHandle(Map<String, Object> event, Context context) throws Exception;

    protected abstract void doRollBack(Map<String, Object> event, Context context);
}

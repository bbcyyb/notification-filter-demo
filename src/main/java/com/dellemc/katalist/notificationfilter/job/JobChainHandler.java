package com.dellemc.katalist.notificationfilter.job;

import com.dellemc.katalist.notificationfilter.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void handle(Context context) {
        try {
            doHandle(context);
        } catch (Exception ex) {
            logger.error("Error occur, start to rollback logic, Exception from " + this.getClass().getName() + " - exiting: " + ex.getMessage());
            rollBack(context);
            return;
        }
        if (nextHandler != null) {
            nextHandler.handle(context);
        }
    }

    public void rollBack(Context context) {
        doRollBack(context);

        if (preHandler != null) {
            preHandler.rollBack(context);
        }
    }

    protected abstract void doHandle(Context context) throws Exception;

    protected abstract void doRollBack(Context context);
}

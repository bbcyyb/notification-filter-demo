package com.dellemc.katalist.notificationfilter;

import com.dellemc.katalist.notificationfilter.job.JobDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String KAFKA_CONSUMER_SHUTDOWN_THREAD = "kafka-indexer-shutdown-thread";
    private static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public static void main(String[] args) {
        logger.info("Filter start ....");

        JobDriver driver = new JobDriver();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Running Shutdown Hook .... ");
            try {
                driver.stopAll();
            } catch (Exception ex) {
                logger.error("Error stopping the Consumer from the ShutdownHook: ", ex);
            }
        }, KAFKA_CONSUMER_SHUTDOWN_THREAD));

        try {
            driver.init();
            driver.startAll();
        } catch (Exception ex) {
            logger.error("Exception from main() - exiting: ", ex);
        }
    }
}

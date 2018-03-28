package com.dellemc.katalist.notificationfilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String KAFKA_CONSUMER_SHUTDOWN_THREAD = "kafka-indexer-shutdown-thread";

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() ->  = 0{
            logger.info("Running Shutdown Hook .... ");
            try {
                // driver.stop();
            } catch (Exception ex) {
                logger.error("Error stopping the Consumer from the ShutdownHook: " + ex.getMessage());
            }
        }, KAFKA_CONSUMER_SHUTDOWN_THREAD));

        try {
            // driver.init(args);
            // driver.start();
        } catch (Exception ex) {
            logger.error("Exception from main() - exiting: " + ex.getMessage());
        }


    }
}

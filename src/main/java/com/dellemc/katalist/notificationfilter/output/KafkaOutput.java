package com.dellemc.katalist.notificationfilter.output;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Output;

import java.util.Map;

public class KafkaOutput extends Output {
    @Override
    protected void doProcess(Map<String, Object> event, Context context) {
        System.out.println("Output message to Kafka");
    }
}

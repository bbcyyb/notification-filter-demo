package com.dellemc.katalist.notificationfilter.filter;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.base.Output;
import com.dellemc.katalist.notificationfilter.utils.HttpRequest;
import scala.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CrsFilter extends Filter {

    private final String host = "10.52.59.194";
    private Properties props;
    private final int numOfRetries = 3;

    public CrsFilter() {
        super();
        props = new Properties();
        props.setProperty("Accept", "application/json");
    }

    @Override
    protected void doProcess(Map<String, Object> event, Context context, boolean lastOne) {
        /*
        HttpRequest request = new HttpRequest(host, props);
        for (int i = 0; i < numOfRetries; i++) {
            Tuple2<String, Integer> t = request.get();
            if (t._2() < 300 && t._2() >= 200) {
                boolean passed = context.getFilterStatus().isPassed() || (t._1().toUpperCase().equals("TRUE"));
                context.getFilterStatus().setPassed(passed);
                break;
            }
        }
        */
        context.getFilterStatus().setPassed(true);
        if(lastOne && context.getFilterStatus().isPassed()) {
            List<Output> outputProcessors = getOutputProcessors();
            outputProcessors.parallelStream().forEach(o -> {
                o.process(event, context);
            });
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    protected void doRollBack(Map<String, Object> event, Context context) {

    }
}

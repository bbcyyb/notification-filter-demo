package com.dellemc.katalist.notificationfilter.filter;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.base.Output;
import com.dellemc.katalist.notificationfilter.utils.HttpRequest;
import org.json.simple.JSONValue;
import scala.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CrsFilter extends Filter {

    private final String host = "http://10.62.59.179:7081/v1";
    private final String evaluator = "/evaluator";
    private final int numOfRetries = 3;
    private Properties props;

    @Override
    protected void doProcess(Map<String, Object> event, Context context, boolean lastOne) {
        logger.info("Start calling CRS server to filter event ....");

        String url = host + evaluator;
        HttpRequest request = new HttpRequest(url, props);
        String json = generatePayload(event);
        for (int i = 0; i < numOfRetries; i++) {
            try {
                Tuple2<String, Integer> t = request.put(json);
                if (t._2() < 300 && t._2() >= 200) {
                    boolean result = Boolean.parseBoolean(((Map) JSONValue.parseWithException(t._1())).get("result").toString());
                    boolean passed = context.getFilterStatus().isPassed() || result;
                    context.getFilterStatus().setPassed(passed);
                    logger.debug("component: {}, value: {}, result: {}", event.get("metric").toString(), event.get("value"), result);
                } else {
                    logger.error("http error: status is {}, message is {}", t._2(), t._1());
                }
                break;
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        }

        // context.getFilterStatus().setPassed(true);
        if (lastOne && context.getFilterStatus().isPassed()) {
            List<Output> outputProcessors = getOutputProcessors();
            outputProcessors.parallelStream().forEach(o -> o.process(event, context));
        }
    }

    @Override
    public void init() {
        props = new Properties();
        props.setProperty("Accept", "application/json");
        props.setProperty("Content-Type", "application/json");
        // props.setProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTIzNjExNDcwfQ.LMB4TI0pP_YqbhoWCsRit1BrD67URCvY5Rxmu0TSJ1uQuNQKIt17afRaLhf0LzLZ6-EpFfVPcEoqWn-fRgJ7sQ");
    }

    @Override
    public void dispose() {

    }

    @Override
    protected void doRollBack(Map<String, Object> event, Context context) {

    }

    private String generatePayload(Map<String, Object> event) {
        String result = String.format("{\"components\": [\"drools\", \"notify\"], \"facts\": \"%s=%s\" }", event.get("metric").toString(), event.get("value").toString());
        return result;
    }
}

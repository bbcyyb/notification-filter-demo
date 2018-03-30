package com.dellemc.katalist.notificationfilter.job;

import com.dellemc.katalist.notificationfilter.base.Job;
import com.dellemc.katalist.notificationfilter.utils.Utils;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.base.Input;
import com.dellemc.katalist.notificationfilter.base.Output;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobDriver {

    private List<Input> inputList;
    private List<Filter> filterList;
    private List<Output> outputList;

    protected List<Input> createInputProcessors() {
        return Utils.createInputProcessors();
    }

    protected List<Filter> createFilterProcessors() {
        return Utils.createFilterProcessors();
    }

    protected List<Output> createOutputProcessors() {
        return Utils.createOutputProcessors();
    }

    private Tuple2<JobChainHandler, JobChainHandler> generateJobChain(List<? extends JobChainHandler> list) {
        JobChainHandler start = null, current = null;
        for (JobChainHandler job : list) {
            if (start == null) {
                start = current = job;
            } else {
                current.setNextHandler(job);
                current = job;
            }
        }
        return new Tuple2<>(start, current);
    }

    public void init() {
        inputList = createInputProcessors();
        filterList = createFilterProcessors();
        outputList = createOutputProcessors();
        Collections.sort(filterList);
        Tuple2<JobChainHandler, JobChainHandler> t2 = generateJobChain(filterList);
        Filter filterChainStart =  (Filter) t2._1();
        Filter filterChainEnd = (Filter) t2._2();
        filterChainEnd.setOutputProcessors(outputList);
        inputList.stream().forEach(input -> input.setFilterProcessor(filterChainStart));
    }

    public void startAll() {
        inputList.stream().forEach(Input::emit);
    }

    public void stopAll() {
        inputList.stream().forEach(Job::shutdown);

    }
}

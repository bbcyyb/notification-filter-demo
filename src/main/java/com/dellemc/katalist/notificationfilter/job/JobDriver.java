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
        JobChainHandler start = null, end = null;
        for (JobChainHandler job : list) {
            if (start == null) {
                start = end = job;
            } else {
                end.setNextHandler(job);
                end = job;
            }
        }
        return new Tuple2<>(start, end);
    }

    public void init() {
        inputList = createInputProcessors();
        filterList = createFilterProcessors();
        outputList = createOutputProcessors();
        inputList.stream().forEach(Job::init);
        filterList.stream().forEach(Job::init);
        outputList.stream().forEach(Job::init);

        Collections.sort(filterList);
        Tuple2<JobChainHandler, JobChainHandler> t2 = generateJobChain(filterList);
        Filter filterChainStart = (Filter) t2._1();
        Filter filterChainEnd = (Filter) t2._2();
        filterChainEnd.setOutputProcessors(outputList);
        inputList.stream().forEach(input -> input.setFilterProcessor(filterChainStart));
    }

    public void startAll() {
        inputList.stream().forEach(Input::emit);
    }

    public void stopAll() {
        inputList.stream().forEach(Job::dispose);
        filterList.stream().forEach(Job::dispose);
        outputList.stream().forEach(Job::dispose);
    }
}

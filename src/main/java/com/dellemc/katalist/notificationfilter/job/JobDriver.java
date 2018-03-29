package com.dellemc.katalist.notificationfilter.job;

import com.dellemc.katalist.notificationfilter.utils.Utils;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.base.Input;
import com.dellemc.katalist.notificationfilter.base.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobDriver {

    private List<Input> inputList = new ArrayList<>();
    private List<Filter> filterList = new ArrayList<>();
    private List<Output> outputList = new ArrayList<>();

    protected List<Input> createInputProcessors() {
        return Utils.createInputProcessors();
    }

    protected List<Filter> createFilterProcessors() {
        return Utils.createFilterProcessors();
    }

    protected List<Output> createOutputProcessors() {
        return Utils.createOutputProcessors();
    }

    private JobChainHandler generateJobChain(List<? extends JobChainHandler> list) {
        JobChainHandler start = null, current = null;
        for(JobChainHandler job: list) {
            if (start == null) {
                start = current = job;
            } else {
                current.setNextHandler(job);
                current = job;
            }
        }
        return start;
    }

    public void init() {
        inputList = createInputProcessors();
        filterList = createFilterProcessors();
        outputList = createOutputProcessors();
        Collections.sort(filterList);
        Filter filterChain = (Filter) generateJobChain(filterList);
        inputList.stream().forEach(input -> input.setFilterProcessor(filterChain));
    }

    public void startAll() {
        inputList.stream().forEach(input -> input.emit());
    }

    public void stopAll() {
        inputList.stream().forEach(input -> {
            input.shutdown();
        });

    }
}

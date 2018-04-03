package com.dellemc.katalist.notificationfilter.utils;

import com.dellemc.katalist.notificationfilter.base.Job;
import com.dellemc.katalist.notificationfilter.base.Filter;
import com.dellemc.katalist.notificationfilter.base.Input;
import com.dellemc.katalist.notificationfilter.base.Output;

import java.lang.reflect.Constructor;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    private static <T extends Job> List<T> createProcessors(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        String packageName = "com.dellemc.katalist.notificationfilter." + clazz.getSimpleName().toLowerCase();
        Set<Class<?>> subClassSet = ClassUtils.getClasses(packageName);

        subClassSet.forEach(subClass -> {
            logger.info("begin to build processor " + subClass.getSimpleName());
            try {
                Constructor<?> ctor = subClass.getConstructor();
                logger.debug("build " + subClass.getSimpleName() + " done");
                result.add((T) ctor.newInstance());
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
                System.exit(1);
            }
        });

        return result;
    }

    public static List<Input> createInputProcessors() {
        return createProcessors(Input.class);
    }

    public static List<Filter> createFilterProcessors() {
        return createProcessors(Filter.class);
    }

    public static List<Output> createOutputProcessors() {
        return createProcessors(Output.class);
    }
}

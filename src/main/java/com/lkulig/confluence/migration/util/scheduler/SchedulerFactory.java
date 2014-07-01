package com.lkulig.confluence.migration.util.scheduler;

import com.lkulig.confluence.migration.util.scheduler.exception.SchedulerCreationException;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class SchedulerFactory {

    public Scheduler create() {
        try {
            return new Scheduler(new StdSchedulerFactory().getScheduler());
        } catch (SchedulerException e) {
            throw new SchedulerCreationException(e.getCause());
        }
    }
}

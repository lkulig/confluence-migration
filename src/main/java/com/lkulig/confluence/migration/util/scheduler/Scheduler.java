package com.lkulig.confluence.migration.util.scheduler;

import com.lkulig.confluence.migration.util.scheduler.exception.SchedulerScheduleJobException;
import com.lkulig.confluence.migration.util.scheduler.exception.SchedulerStartException;
import com.lkulig.confluence.migration.util.scheduler.exception.SchedulerStopException;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public class Scheduler {

    private org.quartz.Scheduler scheduler;

    public Scheduler(org.quartz.Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new SchedulerStartException(e.getCause());
        }
    }

    public void stop() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new SchedulerStopException(e.getCause());
        }
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger) {
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new SchedulerScheduleJobException(e.getCause());
        }
    }
}

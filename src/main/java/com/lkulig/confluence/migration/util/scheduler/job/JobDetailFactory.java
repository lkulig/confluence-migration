package com.lkulig.confluence.migration.util.scheduler.job;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.impl.JobDetailImpl;

public class JobDetailFactory {

    public static JobDetail create(Class<? extends Job> clazz, String name) {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName(name);
        jobDetail.setJobClass(clazz);

        return jobDetail;
    }
}

package com.lkulig.confluence.migration.util;

import com.lkulig.confluence.client.ConfluenceClient;
import com.lkulig.confluence.client.page.summary.ConfluencePageSummary;
import com.lkulig.confluence.migration.util.progress.ProgressLogger;
import com.lkulig.confluence.migration.util.scheduler.Scheduler;
import com.lkulig.confluence.migration.util.scheduler.SchedulerFactory;
import com.lkulig.confluence.migration.util.scheduler.job.JobDetailFactory;
import com.lkulig.confluence.migration.util.scheduler.trigger.TriggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

public class LoggingAspect {

    @Autowired
    private SchedulerFactory schedulerFactory;
    @Value("${confluence.from.space.name}")
    private String confluenceFromSpaceName;
    @Autowired
    @Qualifier(value = "confluenceFromClient")
    private ConfluenceClient source;
    @Value("${trigger.expression}")
    private String expression;

    public Object monitorExportProgress(ProceedingJoinPoint point) throws Throwable {
        Scheduler scheduler = schedulerFactory.create();
        JobDetail jobDetail = JobDetailFactory.create(ProgressLogger.class, "exportProgressLogging");
        CronTrigger trigger = TriggerFactory.create("exportProgressLoggingTrigger", expression);

        source.login();
        List<ConfluencePageSummary> sourcePages = source.getAllPagesOf(confluenceFromSpaceName);
        source.logout();

        ProgressLogger.setTotalPageCount(sourcePages.size());

        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        Object toReturn = point.proceed();

        scheduler.stop();
        return toReturn;
    }
}

package com.lkulig.confluence.migration.util.progress;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgressLogger implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ProgressLogger.class);
    private static AtomicInteger processedPages = new AtomicInteger();
    private static int totalPages;

    public static void setTotalPageCount(int pageCount) {
        totalPages = pageCount;
    }

    public static void incrementProcessedPages() {
        processedPages.incrementAndGet();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Export status: exported {}/{} pages... overall progress: {}%", processedPages.get(), totalPages,
            processedPages.get() * 100 / totalPages);
    }
}

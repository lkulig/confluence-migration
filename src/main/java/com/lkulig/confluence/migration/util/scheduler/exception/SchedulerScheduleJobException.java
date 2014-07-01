package com.lkulig.confluence.migration.util.scheduler.exception;

public class SchedulerScheduleJobException extends RuntimeException {

    private static final String MESSAGE = "Failed to schedule job";

    public SchedulerScheduleJobException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

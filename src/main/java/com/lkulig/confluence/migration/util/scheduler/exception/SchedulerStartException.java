package com.lkulig.confluence.migration.util.scheduler.exception;

public class SchedulerStartException extends RuntimeException{

    private static final String MESSAGE = "Failed to start scheduler";

    public SchedulerStartException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

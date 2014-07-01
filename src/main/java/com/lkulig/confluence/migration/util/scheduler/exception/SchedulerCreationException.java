package com.lkulig.confluence.migration.util.scheduler.exception;

public class SchedulerCreationException extends RuntimeException {

    private static final String MESSAGE = "Failed to create scheduler";

    public SchedulerCreationException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

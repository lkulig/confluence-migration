package com.lkulig.confluence.migration.util.scheduler.exception;

public class SchedulerStopException extends RuntimeException {

    private static final String MESSAGE = "Failed to stop scheduler";

    public SchedulerStopException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

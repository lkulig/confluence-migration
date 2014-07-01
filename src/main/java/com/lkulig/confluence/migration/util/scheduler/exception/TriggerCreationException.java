package com.lkulig.confluence.migration.util.scheduler.exception;

public class TriggerCreationException extends RuntimeException {

    private static final String MESSAGE = "Failed to create trigger";

    public TriggerCreationException(Throwable cause) {
        super(MESSAGE, cause);
    }
}

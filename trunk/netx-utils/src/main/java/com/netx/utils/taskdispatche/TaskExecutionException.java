package com.netx.utils.taskdispatche;

/**
 *
 * @author Runshine
 * @since 2015-10-22
 * @version 1.0.0
 *
 */
public class TaskExecutionException extends RuntimeException {
    private static final long serialVersionUID = 1804096557098063676L;

    public static final String timeOutMsg = "任务执行超时";

    public TaskExecutionException() {
        super(null, null, false, false);
    }

    public TaskExecutionException(String message) {
        super(message, null, false, false);
    }

    public TaskExecutionException(Throwable cause) {
        super(null, cause, false, false);
    }

    public TaskExecutionException(String message, Throwable cause) {
        super(message, cause, false, false);
    }
}

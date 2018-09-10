package com.netx.utils.taskdispatche;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Runshine
 * @since 2014-11-4
 * @version 1.0.0
 *
 */
public class Result<T> {
    private final Future<T> f;
    private final long timeOut;
    private static final Log logger = LogFactory.getLog(Result.class);

    public Result(long timeOut, Future<T> f) {
        this.f = f;
        this.timeOut = timeOut;
    }

    public Result(Future<T> f) {
        this.f = f;
        timeOut = 0;
    }

    /**
     得到实际结果，此方法会阻塞至结果返回或超时（如果设置了超时时间）。
     @return 真实的结果。如果设置了超时时间，超过指定时间时会终止任务并返回null。
     */
    public T getResult() {
        try {
            if (timeOut <= 0) {
                return f.get();
            } else {
                return f.get(timeOut * 980, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new TaskExecutionException(ex);
        } catch (TimeoutException ex) {
            logger.error(TaskExecutionException.timeOutMsg);
            return null;
        } finally {
            f.cancel(true);
        }
    }

    public boolean isDone() {
        return f.isDone();
    }
}

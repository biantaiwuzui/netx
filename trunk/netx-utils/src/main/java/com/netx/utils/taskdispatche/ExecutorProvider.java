package com.netx.utils.taskdispatche;

import com.netx.utils.checker.Assert;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 归一线程池的使用
 * @author Runshine
 * @since 2015-10-22
 * @version 1.0.0
 *
 */
public class ExecutorProvider {
    private final long timeOut;
    private final int threadNums;
    private ExecutorService executorService;

    public ExecutorProvider(long timeOut, int threadNums) {
        Assert.gtZero(timeOut);

        this.timeOut = timeOut;
        this.threadNums = threadNums;
    }

    public void init() {
        if (threadNums <= 0) {
            executorService = Executors.newCachedThreadPool();
        } else {
            executorService = Executors.newFixedThreadPool(threadNums);
        }
    }

    public void destroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public long getTimeOut() {
        return timeOut;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getThreadNums() {
        return threadNums;
    }
}

package com.netx.utils.taskdispatche;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 归一线程池的使用
 * @author Runshine
 * @since 2015-10-22
 * @version 1.0.0
 *
 */
public class TaskExecution {
    private final ExecutorProvider executorProvider;

    public TaskExecution(ExecutorProvider executorProvider) {
        this.executorProvider = executorProvider;
    }

    /**
     执行指定任务，并阻塞等待结果返回。如果超时(ExecutorProvider配置时间)会终止任务并返回null。
     @param <T>    
     @param task 需要执行的任务
     @return 
     */
    public <T> T execTask(Task<T> task) {
        return TaskExecutionUtil.execTask(executorProvider, task);
    }

    /**
     执行指定任务，并阻塞等待结果返回。如果超时会终止任务并返回null。
     @param <T>
     @param timeOut 超时时间，单位：秒
     @param task 需要执行的任务
     @return
     */
    public <T> T execTask(long timeOut, Task<T> task) {
        return TaskExecutionUtil.execTask(executorProvider, timeOut, task);
    }

    /**
     执行指定任务，阻塞直至结果返回
     @param <T>
     @param task 需要执行的任务
     @return
     */
    public <T> T execTaskWithoutTimeOut(Task<T> task) {
        return TaskExecutionUtil.execTaskWithoutTimeOut(executorProvider, task);
    }

    /**
     执行指定对象的方法，并阻塞等待结果返回。如果超时(ExecutorProvider配置时间)会终止任务并返回null。
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public Object execTask(Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.execTask(executorProvider, targetObject, targetMethod, args);
    }

    /**
     执行指定对象的方法，并阻塞等待结果返回。如果超时会终止任务并返回null。
     @param timeOut 超时时间，单位：秒
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public Object execTask(long timeOut, Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.execTask(executorProvider, timeOut, targetObject, targetMethod, args);
    }

    /**
     执行指定对象的方法，阻塞直至结果返回。
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public Object execTaskWithoutTimeOut(Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.execTaskWithoutTimeOut(executorProvider, targetObject, targetMethod, args);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时(ExecutorProvider配置时间)会终止任务并返回null）。
     @param <T>
     @param task
     @return 
     */
    public <T> Result<T> createAsyncTask(Task<T> task) {
        return TaskExecutionUtil.createAsyncTask(executorProvider, task);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时会终止任务并返回null）。
     @param <T>
     @param timeOut 超时时间，单位：秒
     @param task
     @return 
     */
    public <T> Result<T> createAsyncTask(long timeOut, Task<T> task) {
        return TaskExecutionUtil.createAsyncTask(executorProvider, timeOut, task);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞直至任务完成返回结果）。
     @param <T>
     @param task
     @return 
     */
    public <T> Result<T> createAsyncTaskWithoutTimeOut(Task<T> task) {
        return TaskExecutionUtil.createAsyncTaskWithoutTimeOut(executorProvider, task);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时(ExecutorProvider配置时间)会终止任务并返回null）。
     @param <T>
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public <T> Result<T> createAsyncTask(Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.createAsyncTask(executorProvider, targetObject, targetMethod, args);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时会终止任务并返回null）。
     @param <T>
     @param timeOut 超时时间，单位：秒
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public <T> Result<T> createAsyncTask(long timeOut, Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.createAsyncTask(executorProvider, timeOut, targetObject, targetMethod, args);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞直至任务完成返回结果）。
     @param <T>
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public <T> Result<T> createAsyncTaskWithoutTimeOut(Object targetObject, Method targetMethod, Object... args) {
        return TaskExecutionUtil.createAsyncTaskWithoutTimeOut(executorProvider, targetObject, targetMethod, args);
    }

    /**
     异步执行一个过程。
     @param procedure 这里Runnable只是一个无参无返回值的函数（过程）。
     */
    public void startAsyncProcedure(Runnable procedure) {
        TaskExecutionUtil.startAsyncProcedure(executorProvider, procedure);
    }

    /**
     异步执行一个过程。执行完成后继续执行下一个continuation。
     @param procedure 这里Runnable只是一个无参无返回值的函数（过程）。
     @param continuation 其实也是一个无参无返回值的函数（过程）。
     */
    public void startAsyncProcedure(Runnable procedure, Continuation continuation) {
        TaskExecutionUtil.startAsyncProcedure(executorProvider, procedure, continuation);
    }

    /**
     异步执行一个任务。执行完成后将这个任务的结果作为回调的参数继续执行下个任务
     * @param <T>     
     * @param task     
     * @param callback     
     */
    public <T> void startAsyncProcedure(Task<T> task, Callback<T> callback) {
        TaskExecutionUtil.startAsyncProcedure(executorProvider, task, callback);
    }

    /**
     生产者消费者模式（多生产者多消费者，共用构造器线程池。如果生产者多于消费者，多出的产品会被第一个消费者消费掉；如果生产者少于消费者，多出的消费者会被丢弃）。
     @param <T>
     @param producers 生产者队列
     @param consumers 消费者队列
     */
    public <T> void Producer_Consumer(List<Task<T>> producers, List<Callback<T>> consumers) {
        TaskExecutionUtil.Producer_Consumer(executorProvider, producers, consumers);
    }

    /**
     生产者消费者模式（单生产者单消费者，共用构造器线程池）。建议使用startAsyncProcedure代替。
     @param <T>
     @param producer
     @param consumer
     */
    public <T> void Producer_Consumer(Task<T> producer, Callback<T> consumer) {
        TaskExecutionUtil.Producer_Consumer(executorProvider, producer, consumer);
    }

    /**
     生产者消费者模式（多生产者单消费者，共用一个线程池。这个消费者会消费所有产品）
     @param <T>
     @param producers 生产者队列
     @param consumer 消费者
     */
    public <T> void Producer_Consumer(List<Task<T>> producers, Callback<T> consumer) {
        TaskExecutionUtil.Producer_Consumer(executorProvider, producers, consumer);
    }

    /**
     生产者消费者模式（单生产者多消费者，共用线程池，生产者会多次生产（消费者数量个）产品给消费者使用）。
     @param <T>
     @param producer 生产者
     @param consumers 消费者队列
     */
    public <T> void Producer_Consumer_Repeat(Task<T> producer, List<Callback<T>> consumers) {
        TaskExecutionUtil.Producer_Consumer_Repeat(executorProvider, producer, consumers);
    }
    
    /**
     生产者消费者模式（单生产者多消费者，共用线程池，一个产品会被多个消费者共同使用，产品本身的线程安全性需要调用者自己保证）。
     @param <T>
     @param producer 生产者
     @param consumers 消费者队列
     */
    public <T> void Producer_Consumer_Share(Task<T> producer, List<Callback<T>> consumers) {
        TaskExecutionUtil.Producer_Consumer_Share(executorProvider, producer, consumers);
    }
}

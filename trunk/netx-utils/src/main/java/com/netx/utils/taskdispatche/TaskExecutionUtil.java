package com.netx.utils.taskdispatche;

import com.netx.utils.checker.Assert;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Runshine
 * @since 2015-10-24
 * @version 1.0.0
 *
 */
public class TaskExecutionUtil {
    private static final Log logger = LogFactory.getLog(TaskExecutionUtil.class);

    /**
     执行指定任务，并阻塞等待结果返回。如果超时(ExecutorProvider配置时间)会终止任务并返回null。
     @param <T>    
     @param executorProvider  线程池  
     @param task 需要执行的任务
     @return 
     */
    public static <T> T execTask(ExecutorProvider executorProvider, Task<T> task) {
        return execTask(executorProvider, executorProvider.getTimeOut(), task);
    }

    /**
     执行指定任务，并阻塞等待结果返回。如果超时会终止任务并返回null。
     @param <T>
     @param executorProvider  线程池
     @param timeOut 超时时间，单位：秒
     @param task 需要执行的任务
     @return
     */
    public static <T> T execTask(ExecutorProvider executorProvider, long timeOut, Task<T> task) {
        Assert.gtZero(timeOut);
        return doFutureTask(executorProvider, timeOut, () -> task.doTask());
    }

    /**
     执行指定任务，阻塞直至结果返回
     @param <T>
     @param executorProvider  线程池
     @param task 需要执行的任务
     @return
     */
    public static <T> T execTaskWithoutTimeOut(ExecutorProvider executorProvider, Task<T> task) {
        return doFutureTask(executorProvider, () -> task.doTask());
    }

    /**
     执行指定对象的方法，并阻塞等待结果返回。如果超时(ExecutorProvider配置时间)会终止任务并返回null。
     @param executorProvider  线程池
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public static Object execTask(ExecutorProvider executorProvider, Object targetObject, Method targetMethod, Object... args) {
        return execTask(executorProvider, executorProvider.getTimeOut(), targetObject, targetMethod, args);
    }

    /**
     执行指定对象的方法，并阻塞等待结果返回。如果超时会终止任务并返回null。
     @param executorProvider  线程池
     @param timeOut 超时时间，单位：秒
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public static Object execTask(ExecutorProvider executorProvider, long timeOut, Object targetObject, Method targetMethod, Object... args) {
        Assert.gtZero(timeOut);
        Callable callable = (Callable<Object>)() -> targetMethod.invoke(targetObject, args);
        return doFutureTask(executorProvider, timeOut, callable);
    }

    /**
     执行指定对象的方法，阻塞直至结果返回。
     @param executorProvider  线程池
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return
     */
    public static Object execTaskWithoutTimeOut(ExecutorProvider executorProvider, Object targetObject, Method targetMethod, Object... args) {
        Callable callable = (Callable<Object>)() -> targetMethod.invoke(targetObject, args);
        return doFutureTask(executorProvider, callable);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时(ExecutorProvider配置时间)会终止任务并返回null）。
     @param <T>
     @param executorProvider  线程池
     @param task
     @return 
     */
    public static <T> Result<T> createAsyncTask(ExecutorProvider executorProvider, Task<T> task) {
        return createAsyncTask(executorProvider, executorProvider.getTimeOut(), task);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时会终止任务并返回null）。
     @param <T>
     @param executorProvider  线程池
     @param timeOut 超时时间，单位：秒
     @param task
     @return 
     */
    public static <T> Result<T> createAsyncTask(ExecutorProvider executorProvider, long timeOut, Task<T> task) {
        Assert.gtZero(timeOut);
        Future<T> submit = executorProvider.getExecutorService().submit(() -> task.doTask());
        return new Result<>(timeOut, submit);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞直至任务完成返回结果）。
     @param <T>
     @param executorProvider  线程池
     @param task
     @return 
     */
    public static <T> Result<T> createAsyncTaskWithoutTimeOut(ExecutorProvider executorProvider, Task<T> task) {
        Future<T> submit = executorProvider.getExecutorService().submit(() -> task.doTask());
        return new Result<>(submit);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时(ExecutorProvider配置时间)会终止任务并返回null）。
     @param <T>
     @param executorProvider  线程池
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public static <T> Result<T> createAsyncTask(ExecutorProvider executorProvider, Object targetObject, Method targetMethod, Object... args) {
        return createAsyncTask(executorProvider, executorProvider.getTimeOut(), targetObject, targetMethod, args);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞，如果超时会终止任务并返回null）。
     @param <T>
     @param executorProvider  线程池
     @param timeOut 超时时间，单位：秒
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public static <T> Result<T> createAsyncTask(ExecutorProvider executorProvider, long timeOut, Object targetObject, Method targetMethod, Object... args) {
        Assert.gtZero(timeOut);
        Callable callable = (Callable<Object>)() -> targetMethod.invoke(targetObject, args);
        Future<T> submit = executorProvider.getExecutorService().submit(callable);
        return new Result<>(timeOut, submit);
    }

    /**
     异步创建一个任务（并执行），方法会立即返回一个包装了最终结果的Result对象。
     由调用者在合适的点来get得到实际结果（如果异步任务尚未完成，此时会阻塞直至任务完成返回结果）。
     @param <T>
     @param executorProvider  线程池
     @param targetObject 如果执行的方法是静态方法则传入null
     @param targetMethod
     @param args 待执行方法的参数
     @return 
     */
    public static <T> Result<T> createAsyncTaskWithoutTimeOut(ExecutorProvider executorProvider, Object targetObject, Method targetMethod, Object... args) {
        Callable callable = (Callable<Object>)() -> targetMethod.invoke(targetObject, args);
        Future<T> submit = executorProvider.getExecutorService().submit(callable);
        return new Result<>(submit);
    }

    /**
     异步执行一个过程。
     @param executorProvider  线程池
     @param procedure 这里Runnable只是一个无参无返回值的函数（过程）。
     */
    public static void startAsyncProcedure(ExecutorProvider executorProvider, Runnable procedure) {
        executorProvider.getExecutorService().execute(procedure);
    }

    /**
     异步执行一个过程。执行完成后继续执行下一个continuation。
     @param executorProvider  线程池
     @param procedure 这里Runnable只是一个无参无返回值的函数（过程）。
     @param continuation 其实也是一个无参无返回值的函数（过程）。
     */
    public static void startAsyncProcedure(ExecutorProvider executorProvider, Runnable procedure, Continuation continuation) {
        executorProvider.getExecutorService().execute(() -> {
            procedure.run();
            continuation.doContinuation();
        });
    }

    /**
     异步执行一个任务。执行完成后将这个任务的结果作为回调的参数继续执行下个任务
     * @param <T>     
     * @param executorProvider  线程池
     * @param task     
     * @param callback     
     */
    public static <T> void startAsyncProcedure(ExecutorProvider executorProvider, Task<T> task, Callback<T> callback) {
        executorProvider.getExecutorService().execute(() -> {
            T t = task.doTask();
            callback.doCallback(t);
        });
    }

    private static <T> void Producer_LessThan_Consumer(BlockingQueue<T> abq, ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        Iterator<Callback<T>> consumerIterator = consumers.iterator();

        producers.forEach(p -> {
            produceEnviroment.getExecutorService().execute(() -> {
                T t = p.doTask();
                try {
                    abq.put(t);
                } catch (InterruptedException ex) {
                    logger.error(t.toString() + " 因任务中断未能添加");
                }
            });

            Callback<T> consumer = consumerIterator.next();

            consumeEnviroment.getExecutorService().execute(() -> {
                try {
                    T t = abq.take();
                    consumer.doCallback(t);
                } catch (InterruptedException ex) {
                    logger.error("有任务因中断未能执行");
                }
            });
        });
    }

    private static <T> void Producer_MoreThan_Consumer(BlockingQueue<T> abq, ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        Iterator<Task<T>> producerIterator = producers.iterator();

        consumers.forEach(c -> {
            Task<T> producer = producerIterator.next();
            produceEnviroment.getExecutorService().execute(() -> {
                T t = producer.doTask();
                try {
                    abq.put(t);
                } catch (InterruptedException ex) {
                    logger.error(t.toString() + " 因任务中断未能添加");
                }
            });

            consumeEnviroment.getExecutorService().execute(() -> {
                try {
                    T t = abq.take();
                    c.doCallback(t);
                } catch (InterruptedException ex) {
                    logger.error("有任务因中断未能执行");
                }
            });
        });

        Callback<T> firstConsume = consumers.get(0);
        while (producerIterator.hasNext()) {
            Task<T> producer = producerIterator.next();

            consumeEnviroment.getExecutorService().execute(() -> {
                T doTask = producer.doTask();
                firstConsume.doCallback(doTask);
            });
        }
    }

    private static <T> void Producer_Equal_Consumer(BlockingQueue<T> abq, ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        producers.forEach(p -> produceEnviroment.getExecutorService().execute(() -> {
            T t = p.doTask();
            try {
                abq.put(t);
            } catch (InterruptedException ex) {
                logger.error(t.toString() + " 因任务中断未能添加");
            }
        }));

        consumers.forEach(c -> consumeEnviroment.getExecutorService().execute(() -> {
            try {
                T t = abq.take();
                c.doCallback(t);
            } catch (InterruptedException ex) {
                logger.error("有任务因中断未能执行");
            }
        }));
    }

    /**
     生产者消费者模式（多生产者多消费者。如果生产者多于消费者，多出的产品会被第一个消费者多线程消费掉；如果生产者少于消费者，多出的消费者会被丢弃）。P.S.生产者线程池、消费者线程池可以使用同一个池。
     @param <T>
     @param produceEnviroment 生产者线程池
     @param producers 生产者队列
     @param consumeEnviroment 消费者线程池
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer(ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        int producersSize = producers.size();
        int consumersSize = consumers.size();

        int switcher = 0;

        if (producersSize > consumersSize) {
            consumersSize = consumersSize + 1;
            switcher = 1;
        } else if (producersSize < consumersSize) {
            consumersSize = producersSize;
            switcher = -1;
        }

        BlockingQueue<T> abq = getBlockingQueue(produceEnviroment.getThreadNums(),
                                                consumeEnviroment.getThreadNums(),
                                                produceEnviroment == consumeEnviroment,
                                                producersSize,
                                                consumersSize);

        switch (switcher) {
            case 1: Producer_MoreThan_Consumer(abq, produceEnviroment, producers, consumeEnviroment, consumers);
                break;
            case 0: Producer_Equal_Consumer(abq, produceEnviroment, producers, consumeEnviroment, consumers);
                break;
            case -1: Producer_LessThan_Consumer(abq, produceEnviroment, producers, consumeEnviroment, consumers);
                break;
        }
    }

    /**
     生产者消费者模式（多生产者多消费者，共用一个线程池。如果生产者多于消费者，多出的产品会被第一个消费者消费掉；如果生产者少于消费者，多出的消费者会被丢弃）。
     @param <T>
     @param enviroment 生产者消费者执行任务的线程池
     @param producers 生产者队列
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer(ExecutorProvider enviroment, List<Task<T>> producers, List<Callback<T>> consumers) {
        Producer_Consumer(enviroment, producers, enviroment, consumers);
    }

    /**
     生产者消费者模式（单生产者单消费者）。P.S.生产者线程池、消费者线程池可以使用同一个池。建议使用startAsyncProcedure代替。
     @param <T>
     @param produceEnviroment 生产者线程池
     @param producer
     @param consumeEnviroment 消费者线程池
     @param consumer
     */
    public static <T> void Producer_Consumer(ExecutorProvider produceEnviroment, Task<T> producer, ExecutorProvider consumeEnviroment, Callback<T> consumer) {
        BlockingQueue<T> abq = new SynchronousQueue<>();

        produceEnviroment.getExecutorService().execute(() -> {
            T t = producer.doTask();
            try {
                abq.put(t);
            } catch (InterruptedException ex) {
                logger.error(t.toString() + " 因任务中断未能添加");
            }
        });

        consumeEnviroment.getExecutorService().execute(() -> {
            try {
                T t = abq.take();
                consumer.doCallback(t);
            } catch (InterruptedException ex) {
                logger.error("有任务因中断未能执行");
            }
        });
    }

    /**
     生产者消费者模式（单生产者单消费者，共用线程池）。建议使用startAsyncProcedure代替。
     @param <T>
     @param enviroment 执行任务的线程池
     @param producer
     @param consumer
     */
    public static <T> void Producer_Consumer(ExecutorProvider enviroment, Task<T> producer, Callback<T> consumer) {
        Producer_Consumer(enviroment, producer, enviroment, consumer);
    }

    /**
     生产者消费者模式（多生产者单消费者，这个消费者会消费所有产品）。P.S.生产者线程池、消费者线程池可以使用同一个池。
     @param <T>
     @param produceEnviroment 生产者线程池
     @param producers 生产者队列
     @param consumeEnviroment 消费者线程池
     @param consumer 消费者
     */
    public static <T> void Producer_Consumer(ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, Callback<T> consumer) {
        BlockingQueue<T> abq = getBlockingQueue(produceEnviroment.getThreadNums(),
                                                consumeEnviroment.getThreadNums(),
                                                produceEnviroment == consumeEnviroment,
                                                producers.size(),
                                                producers.size());

        producers.forEach(p -> {
            produceEnviroment.getExecutorService().execute(() -> {
                T t = p.doTask();
                try {
                    abq.put(t);
                } catch (InterruptedException ex) {
                    logger.error(t.toString() + " 因任务中断未能添加");
                }
            });

            consumeEnviroment.getExecutorService().execute(() -> {
                try {
                    T t = abq.take();
                    consumer.doCallback(t);
                } catch (InterruptedException ex) {
                    logger.error("有任务因中断未能执行");
                }
            });
        });
    }

    /**
     生产者消费者模式（多生产者单消费者，共用一个线程池。这个消费者会消费所有产品）
     @param <T>
     @param produceEnviroment 执行任务的线程池
     @param producers 生产者队列
     @param consumer 消费者
     */
    public static <T> void Producer_Consumer(ExecutorProvider produceEnviroment, List<Task<T>> producers, Callback<T> consumer) {
        Producer_Consumer(produceEnviroment, producers, produceEnviroment, consumer);
    }

    /**
     生产者消费者模式（单生产者多消费者，生产者会多次生产（消费者数量个）产品给消费者使用）。P.S.生产者线程池、消费者线程池可以使用同一个池。
     @param <T>
     @param produceEnviroment 生产者线程池
     @param producer 生产者
     @param consumeEnviroment 消费者线程池
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer_Repeat(ExecutorProvider produceEnviroment, Task<T> producer, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        BlockingQueue<T> abq = getBlockingQueue(produceEnviroment.getThreadNums(),
                                                consumeEnviroment.getThreadNums(),
                                                produceEnviroment == consumeEnviroment,
                                                consumers.size(),
                                                consumers.size());

        consumers.forEach(c -> {
            produceEnviroment.getExecutorService().execute(() -> {
                T t = producer.doTask();
                try {
                    abq.put(t);
                } catch (InterruptedException ex) {
                    logger.error(t.toString() + " 因任务中断未能添加");
                }
            });

            consumeEnviroment.getExecutorService().execute(() -> {
                try {
                    T t = abq.take();
                    c.doCallback(t);
                } catch (InterruptedException ex) {
                    logger.error("有任务因中断未能执行");
                }
            });
        });
    }

    /**
     生产者消费者模式（单生产者多消费者，共用线程池，生产者会多次生产（消费者数量个）产品给消费者使用）。
     @param <T>
     @param enviroment 生产者线程池
     @param producer 生产者
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer_Repeat(ExecutorProvider enviroment, Task<T> producer, List<Callback<T>> consumers) {
        Producer_Consumer_Repeat(enviroment, producer, enviroment, consumers);
    }

    /**
     生产者消费者模式（单生产者多消费者，一个产品会被多个消费者共同使用，产品本身的线程安全性需要调用者自己保证）。P.S.生产者线程池、消费者线程池可以使用同一个池。
     @param <T>
     @param produceEnviroment 生产者线程池
     @param producer 生产者
     @param consumeEnviroment 消费者线程池
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer_Share(ExecutorProvider produceEnviroment, Task<T> producer, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        BlockingQueue<T> abq = getBlockingQueue(produceEnviroment.getThreadNums(),
                                                consumeEnviroment.getThreadNums(),
                                                produceEnviroment == consumeEnviroment,
                                                consumers.size(),
                                                consumers.size());

        int loop = consumers.size();
        produceEnviroment.getExecutorService().execute(() -> {
            T t = producer.doTask();
            try {
                for (int i = 0; i < loop; i++) {
                    abq.put(t);
                }
            } catch (InterruptedException ex) {
                logger.error(t.toString() + " 因任务中断未能添加");
            }
        });

        consumers.forEach(c -> {
            consumeEnviroment.getExecutorService().execute(() -> {
                try {
                    T t = abq.take();
                    c.doCallback(t);
                } catch (InterruptedException ex) {
                    logger.error("有任务因中断未能执行");
                }
            });
        });
    }

    /**
     生产者消费者模式（单生产者多消费者，共用线程池，一个产品会被多个消费者共同使用，产品本身的线程安全性需要调用者自己保证）。
     @param <T>
     @param enviroment 生产者线程池
     @param producer 生产者
     @param consumers 消费者队列
     */
    public static <T> void Producer_Consumer_Share(ExecutorProvider enviroment, Task<T> producer, List<Callback<T>> consumers) {
        Producer_Consumer_Share(enviroment, producer, enviroment, consumers);
    }

    private static <T> BlockingQueue<T> getBlockingQueue(int produceEnviromentThreadNums, int consumeEnviromentThreadNums, boolean isEnviromentEqual, int producersSize, int consumersSize) {
        if (isEnviromentEqual) {
            return new LinkedBlockingQueue<>(producersSize);
        }

        BlockingQueue<T> abq;
        int line = producersSize;

        if (line <= 30) {
            if (line <= 0 && logger.isWarnEnabled()) {
                logger.warn("消费者数量多于生产者，存在内存泄露与线程饥饿风险");
            }
            abq = new SynchronousQueue<>();
        } else {
            long threadNums1 = produceEnviromentThreadNums + consumeEnviromentThreadNums;
            long threadNums2 = producersSize + consumersSize;

            long threadNums3 = threadNums1 <= threadNums2 ? threadNums1 : threadNums2;

            int activityThreadNums = threadNums3 <= Integer.MAX_VALUE ? (int)threadNums3 : Integer.MAX_VALUE;

            if (activityThreadNums > 20) {
                abq = new LinkedBlockingQueue<>(line);
            } else {
                abq = new SynchronousQueue<>();
            }
        }
        return abq;
    }

    private static <T> T doFutureTask(ExecutorProvider executorProvider, long timeOut, Callable<T> callable) {
        Future<T> submit = executorProvider.getExecutorService().submit(callable);

        try {
            return submit.get(timeOut, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException ex) {
            throw new TaskExecutionException(ex);
        } catch (TimeoutException ex) {
            logger.error(TaskExecutionException.timeOutMsg);
            return null;
        } finally {
            submit.cancel(true);
        }
    }

    private static <T> T doFutureTask(ExecutorProvider executorProvider, Callable<T> callable) {
        Future<T> submit = executorProvider.getExecutorService().submit(callable);

        try {
            return submit.get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new TaskExecutionException(ex);
        } finally {
            submit.cancel(true);
        }
    }

    public static class Lbq<T> extends LinkedBlockingQueue<T> {

        public Lbq() {
        }

        public Lbq(int capacity) {
            super(capacity);
        }

        public Lbq(Collection c) {
            super(c);
        }

        @Override
        public void finalize() throws Throwable {
            System.out.println("finalize");
            super.finalize();
        }
    }

    public static <T> void Producer_Consumer_test(ExecutorProvider produceEnviroment, List<Task<T>> producers, ExecutorProvider consumeEnviroment, List<Callback<T>> consumers) {
        BlockingQueue<T> abq = getBlockingQueue(produceEnviroment.getThreadNums(), consumeEnviroment.getThreadNums(), produceEnviroment == consumeEnviroment, producers.size(), consumers.size());

        producers.forEach(p -> produceEnviroment.getExecutorService().execute(() -> {
            T t = p.doTask();
            try {
                abq.put(t);
            } catch (InterruptedException ex) {
                logger.error(t.toString() + " 因任务中断未能添加");
            }
        }));

        consumers.forEach(c -> consumeEnviroment.getExecutorService().execute(() -> {
            try {
                System.out.println("--Sleep--");
                Thread.sleep(5000);
                T t = abq.take();
                c.doCallback(t);
                System.out.println("++Sleep++");
            } catch (InterruptedException ex) {
                logger.error("有任务因中断未能执行");
            }
        }));

        System.out.println("return");
    }

    private static void test1() throws InterruptedException {
        ExecutorProvider executorProvider = new ExecutorProvider(1, 2);
        ExecutorProvider executorProvider2 = new ExecutorProvider(1, 2);

        executorProvider.init();
        executorProvider2.init();

        List<Task<Integer>> l = new LinkedList<>();
        l.add(() -> {
            System.out.println(1);
            return 1;
        });
        l.add(() -> {
            System.out.println(2);
            return 2;
        });
        l.add(() -> {
            System.out.println(3);
            return 3;
        });
        l.add(() -> {
            System.out.println(4);
            return 4;
        });

        List<Callback<Integer>> l2 = new LinkedList<>();
        l2.add(i -> {
            System.out.println(Thread.currentThread().toString());
            System.out.println(i);
        });
        l2.add(i -> {
            System.out.println(Thread.currentThread().toString());
            System.out.println(i * 10);
        });

        Producer_Consumer_test(executorProvider, l, executorProvider2, l2);
        System.out.println("return2");

        Thread.sleep(15000);
        executorProvider2.getExecutorService().execute(() -> {
            System.out.println(Thread.currentThread().toString());
        });

        System.gc();
        System.out.println("gc2");
    }

    public static void main(String[] args) throws InterruptedException {
        test1();

    }
}

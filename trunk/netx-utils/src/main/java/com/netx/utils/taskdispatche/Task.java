package com.netx.utils.taskdispatche;

/**
 *
 * @author Runshine
 * @since 2014-11-3
 * @version 1.0.0
 *
 */
@FunctionalInterface
public interface Task<T> {
    T doTask();
}

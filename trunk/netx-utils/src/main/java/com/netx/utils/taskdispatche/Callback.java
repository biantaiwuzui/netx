package com.netx.utils.taskdispatche;

/**
 *
 * @author Runshine
 * @since 2015-10-22
 * @version 1.0.0
 *
 */
@FunctionalInterface
public interface Callback<T> {
    void doCallback(T t);
}

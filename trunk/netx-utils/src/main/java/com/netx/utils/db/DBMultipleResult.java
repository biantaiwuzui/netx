package com.netx.utils.db;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Runshine
 * @since 2014-12-23
 * @version 1.0.0
 *
 */
public class DBMultipleResult<T> {
    private boolean success;
    private Throwable dbException;
    private String resultmsg = "";
    private List<? extends T> result;

    public DBMultipleResult() {
        setDBSuccess(true);
    }

    public DBMultipleResult(boolean dbsuccess, Throwable dbException) {
        setDBSuccess(dbsuccess);
        setDBException(dbException);
    }

    public boolean isDBSuccess() {
        return success;
    }

    public boolean isResultNotEmpty() {
        return result != null && !result.isEmpty();
    }

    public Throwable getDBException() {
        return dbException;
    }

    private void setDBSuccess(boolean success) {
        this.success = success;
    }

    private void setDBException(Throwable throwable) {
        dbException = throwable;
    }

    public String getResultmsg() {
        return resultmsg;
    }

    public DBMultipleResult<T> setResultmsg(String msg) {
        if (msg != null) {
            this.resultmsg = msg;
        } else {
            this.resultmsg = "";
        }
        return this;
    }

    public List<T> getResult() {
        if (result == null) {
            return Collections.emptyList();
        }
        return (List<T>)result;
    }

    public DBMultipleResult<T> setResult(List<? extends T> result) {
        this.result = result;
        return this;
    }

    public void forEach(Consumer<? super T> action) {
        if (isResultNotEmpty()) {
            result.forEach(action);
        }
    }
}

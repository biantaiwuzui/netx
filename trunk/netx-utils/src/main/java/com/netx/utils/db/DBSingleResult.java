package com.netx.utils.db;

/**
 *
 * @author Runshine
 * @since 2014-12-23
 * @version 1.0.0
 *
 */
public class DBSingleResult<T> {
    private boolean success;
    private Throwable dbException;
    private String resultmsg = "";
    private T result;

    public DBSingleResult() {
        setDBSuccess(true);
    }

    public DBSingleResult(boolean dbsuccess, String msg) {
        setDBSuccess(dbsuccess);
        setResultmsg(msg);
    }

    public DBSingleResult(boolean dbsuccess, Throwable dbException) {
        setDBSuccess(dbsuccess);
        setDBException(dbException);
    }

    public boolean isDBSuccess() {
        return success;
    }

    public boolean isResultNotEmpty() {
        return result != null;
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

    public DBSingleResult<T> setResultmsg(String msg) {
        if (msg != null) {
            this.resultmsg = msg;
        } else {
            this.resultmsg = "";
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public DBSingleResult<T> setResult(T result) {
        this.result = result;
        return this;
    }
}

package com.common.utils;

/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-5-16
 * desc:
 */
public class CountOrder {

    private int start=0;
    private int limit;
    private int end;
    private String orderby;
    private String dir;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}

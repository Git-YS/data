package com.hrms.data.bean;

/**
 * @author ys
 */
public class ExcelQuery {
    /**
     * 字段行数，默认为首行
     */
    private int header = 0;
    /**
     * 数据开始行数，默认为第二行
     */
    private int first = 1;
    /**
     * 数据结束行数，默认为0，不限制
     */
    private int last = 0;

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }
}

package com.netx.common.vo.common;

public class StatPageRequestDto {

    private int current = 1;

    private int size = 100;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if(current!=null){
            this.current = current;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if(size!=null){
            this.size = size;
        }
    }
}

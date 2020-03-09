package com.shenzhen.recurit.enums;

public enum NumberEnum {

    ZERO(0),
    ONE(0),
    TWO(0),
    THREE(0),
    FOUR(0);

    private int value;
    private NumberEnum(int value){
        this.value=value;
    }

    public int getValue(){
        return this.value;
    }
}

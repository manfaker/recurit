package com.shenzhen.recurit.enums;

public enum NumberEnum {

    NEGATIVE_ONE(-1),
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    TEN(10),
    SIXTEEN(16),
    THIRTY_TWO(32),
    NINETY_SEVEN(97),
    SIXTY_FIVE(65),
    TWENTY_SIX(26);

    private int value;
    private NumberEnum(int value){
        this.value=value;
    }

    public int getValue(){
        return this.value;
    }
}

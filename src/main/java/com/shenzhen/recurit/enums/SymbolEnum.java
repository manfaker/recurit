package com.shenzhen.recurit.enums;

public enum SymbolEnum {
    COMMA(",");

    private String value;
    private SymbolEnum(String value){
        this.value=value;
    }

    public String getValue(){
        return this.value;
    }
}

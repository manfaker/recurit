package com.shenzhen.recurit.vo;

import lombok.Data;

@Data
public class LabelVO extends BaseVO{
    private int id ;
    private String labelName;
    private String category;
    private String  relationId;

}

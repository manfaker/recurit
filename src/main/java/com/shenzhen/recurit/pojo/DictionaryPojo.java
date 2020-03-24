package com.shenzhen.recurit.pojo;

import com.shenzhen.recurit.vo.BaseVO;
import lombok.Data;

@Data
public class DictionaryPojo extends BaseVO{


    private int id;
    private String dictNum;   //编号
    private String category;  //类别
    private String dictName;  //名称
    private String content;   //内容
    private String superNum;  //上级编码

}

package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.utils.EmptyUtils;
import lombok.Data;

@Data
public class LabelVO extends BaseVO{
    private int id ;
    private String labelName;
    private String category;
    private int  relationId;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LabelVO){
            LabelVO labelVO = (LabelVO) obj;
            if(EmptyUtils.isEmpty(labelVO)){
                return false;
            }
            if(EmptyUtils.isNotEmpty(labelVO.getCategory())
                &&EmptyUtils.isNotEmpty(labelVO.getLabelName())
                    &&labelVO.getRelationId()> NumberEnum.ZERO.getValue()){
                if(labelVO.getCategory().equals(this.category)
                        &&labelVO.getLabelName().equals(this.labelName)
                        &&labelVO.getRelationId()==relationId){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
    }
}

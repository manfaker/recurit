package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.LabelService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.LabelVO;
import com.shenzhen.recurit.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "label")
public class LabelController {

    @Resource
    private LabelService labelService;

    @RequestMapping(value = "saveBatchLabel",method = RequestMethod.POST)
    public ResultVO saveBatchLabel(@RequestBody String jsonData){
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        List<LabelVO> listLabel = new ArrayList<>();
        jsonArray.forEach(item->{
            LabelVO labelVO = JSON.parseObject(item.toString(), LabelVO.class);
            if(EmptyUtils.isNotEmpty(labelVO)){
                listLabel.add(labelVO);
            }
        });
        return ResultVO.success(labelService.saveBatchLabel(listLabel));
    }

    @RequestMapping(value = "saveLabel",method = RequestMethod.POST)
    public ResultVO saveLabel(@RequestBody String jsonData){
        LabelVO labelVO = JSON.parseObject(jsonData, LabelVO.class);
        return ResultVO.success(labelService.saveLabel(labelVO));
    }

    @RequestMapping(value = "deleteLabelById",method = RequestMethod.DELETE)
    public ResultVO deleteLabelById(int id){
        if(id== NumberEnum.ZERO.getValue()){
            ResultVO.error("id 不能为空");
        }
        return ResultVO.success(labelService.deleteLabelById(id));
    }

    @RequestMapping(value = "deleteLabelByRelationId",method = RequestMethod.DELETE)
    public ResultVO deleteLabelByRelationId(int relationId,String category){
        return ResultVO.success(labelService.deleteLabelByRelationId(category,relationId));
    }

    @RequestMapping(value = "updateLabel",method = RequestMethod.PUT)
    public ResultVO updateLabel(@RequestBody String jsonData){
        LabelVO labelVO = JSON.parseObject(jsonData, LabelVO.class);
        return ResultVO.success(labelService.updateLabel(labelVO));
    }

    @RequestMapping(value = "queryById",method = RequestMethod.GET)
    public ResultVO queryById(int relationId,String category,int id){
        return ResultVO.success(labelService.queryById(id,category,relationId));
    }

    @RequestMapping(value = "queryByRelationId",method = RequestMethod.GET)
    public ResultVO queryByRelationId(int relationId,String category){
        return ResultVO.success(labelService.queryByRelationId(category,relationId));
    }

}

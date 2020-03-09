package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.PositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "position")
public class PositionController {

    @Resource
    private PositionService positionService;

    @RequestMapping(value="getAllPosition",method = RequestMethod.GET)
    public Object getAllPosition(){
        List<PositionVO> listPosition = positionService.getAllPosition();
        return ResultVO.success(listPosition);
    }

    @RequestMapping(value="getAllPosition",method = RequestMethod.POST)
    public Object getAllPositionByCondition(@RequestBody String jsonData){
        PositionVO positionVO = JSONObject.parseObject(jsonData, PositionVO.class);
        List<PositionVO> listPosition = positionService.getAllPositionByCondition(positionVO);
        return ResultVO.success(listPosition);
    }

    @RequestMapping(value="savePosition",method = RequestMethod.POST)
    public Object savePosition(@RequestBody String jsonData){
        PositionVO positionVO = JSONObject.parseObject(jsonData, PositionVO.class);
        positionService.savePosition(positionVO);
        return ResultVO.success(positionVO);
    }

    @RequestMapping(value="getByCompanyId",method = RequestMethod.GET)
    public Object getByCompanyId( String companyId){
        List<PositionVO> listPosition = positionService.getByCompanyId(companyId);
        return ResultVO.success(listPosition);
    }

    @RequestMapping(value="updatePosition",method = RequestMethod.PUT)
    public Object updatePosition(@RequestBody String jsonData){
        PositionVO positionVO = JSONObject.parseObject(jsonData, PositionVO.class);
        int result = positionService.updatePosition(positionVO);
        return ResultVO.success("修改成功");
    }
}

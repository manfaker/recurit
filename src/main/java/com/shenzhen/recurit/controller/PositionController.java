package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.PositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = {"职位信息"})
@RestController
@RequestMapping(value = "position")
public class PositionController {

    @Resource
    private PositionService positionService;

    @RequestMapping(value = "savePosition",method = RequestMethod.POST)
    @PermissionVerification
    public Object savePosition(@RequestBody String jsonData){
        PositionVO positionVO = JSON.parseObject(jsonData, PositionVO.class);
        return ResultVO.success(positionService.savePosition(positionVO));
    }
    @RequestMapping(value = "deletePosition",method = RequestMethod.DELETE)
    @PermissionVerification
    public Object deletePosition(int id){
        return ResultVO.success(positionService.deletePositionById(id));
    }
    @RequestMapping(value = "updatePosition",method = RequestMethod.PUT)
    @PermissionVerification
    public Object updatePosition(@RequestBody String jsonData){
        PositionVO positionVO = JSON.parseObject(jsonData, PositionVO.class);
        return positionService.updatePosition(positionVO);
    }

    @RequestMapping(value = "getByCompanyCode",method = RequestMethod.GET)
    @PermissionVerification
    public Object getByCompanyCode(){
        UserVO userVO = ThreadLocalUtils.getUser();
        return ResultVO.success(positionService.getByCompanyCode(userVO.getCompanyCode(),userVO.getUserCode()));
    }

    @RequestMapping(value = "getByPositionId",method = RequestMethod.GET)
    @PermissionVerification
    public Object getByPositionId(int id){
        return ResultVO.success(positionService.getByPositionId(id));
    }

    @RequestMapping(value = "getAllPositions",method = RequestMethod.POST)
    @PermissionVerification
    @ApiOperation(value = "获取所有的或者已关注或者申请过的所有职位信息")
    public Object getAllPositions(@RequestBody @ApiParam PositionVO positionVO){
        return ResultVO.success(positionService.getAllPositions(positionVO));
    }

    @RequestMapping(value = "getPopularPositions",method = RequestMethod.GET)
    @ApiOperation(value = "获取热门职位")
    public Object getPopularPositions(){
        return ResultVO.success(positionService.getPopularPositions());
    }

    @RequestMapping(value = "getRecentlyPositions",method = RequestMethod.GET)
    @ApiOperation(value = "最近浏览职位")
    public Object getRecentlyPositions(){
        return ResultVO.success(positionService.getRecentlyPositions());
    }


}

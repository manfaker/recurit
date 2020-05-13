package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.PositionVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "获取个人发布职位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "初始页",name = "pageNum" ,required = false),
            @ApiImplicitParam(value = "初始页",name = "pageSize" ,required = false)
    })
    @RequestMapping(value = "getByCompanyCode",method = RequestMethod.GET)
    @PermissionVerification
    public Object getByCompanyCode(Integer pageNum, Integer pageSize){
        if(EmptyUtils.isEmpty(pageNum)||EmptyUtils.isEmpty(pageSize)||
                pageNum==NumberEnum.ZERO.getValue()||pageSize==NumberEnum.ZERO.getValue()){
            pageNum =NumberEnum.ONE.getValue();
            pageSize=NumberEnum.TWENTY.getValue();
        }
        UserVO userVO = ThreadLocalUtils.getUser();
        return ResultVO.success(positionService.getByCompanyCode(userVO.getCompanyCode(),userVO.getUserCode(),pageNum,pageSize));
    }

    @RequestMapping(value = "getByPositionId",method = RequestMethod.GET)
    @PermissionVerification
    public Object getByPositionId(int id){
        return ResultVO.success(positionService.getByPositionId(id));
    }

    @RequestMapping(value = "getAllPositions",method = RequestMethod.POST)
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "初始页",name = "pageNum" ,required = false),
            @ApiImplicitParam(value = "初始页",name = "pageSize" ,required = false)
    })
    @ApiOperation(value = "获取所有的或者已关注或者申请过的所有职位信息")
    public Object getAllPositions(Integer pageNum, Integer pageSize,@RequestBody @ApiParam PositionVO positionVO ){
        if(EmptyUtils.isEmpty(pageNum)||EmptyUtils.isEmpty(pageSize)||
                pageNum==NumberEnum.ZERO.getValue()||pageSize==NumberEnum.ZERO.getValue()){
            pageNum =NumberEnum.ONE.getValue();
            pageSize=NumberEnum.TWENTY.getValue();
        }
        return ResultVO.success(positionService.getAllPositions(positionVO,pageNum,pageSize));
    }

    @RequestMapping(value = "getNewAllPositions",method = RequestMethod.POST)
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "初始页",name = "pageNum" ,required = false),
            @ApiImplicitParam(value = "初始页",name = "pageSize" ,required = false)
    })
    @ApiOperation(value = "获取求职页面的所有职位信息")
    public Object getNewAllPositions(Integer pageNum, Integer pageSize,@RequestBody @ApiParam PositionVO positionVO ){
        if(EmptyUtils.isEmpty(pageNum)||EmptyUtils.isEmpty(pageSize)||
                pageNum==NumberEnum.ZERO.getValue()||pageSize==NumberEnum.ZERO.getValue()){
            pageNum =NumberEnum.ONE.getValue();
            pageSize=NumberEnum.TWENTY.getValue();
        }
        return ResultVO.success(positionService.getNewAllPositions(positionVO,pageNum,pageSize));
    }

    @GetMapping(value = "getPopularPositions")
    @ApiOperation(value = "获取热门职位")
    public Object getPopularPositions(){
        return ResultVO.success(positionService.getPopularPositions());
    }

    @GetMapping (value = "getRecentlyPositions")
    @ApiOperation(value = "最近浏览职位")
    @ApiImplicitParam(value = "用户code",name="userCode",required = false)
    public Object getRecentlyPositions(String userCode){
        return ResultVO.success(positionService.getRecentlyPositions(userCode));
    }

    @GetMapping (value = "getBulletinBoardPosition")
    @ApiOperation(value = "公告栏信息")
    public Object getBulletinBoardPosition(){
        return ResultVO.success(positionService.getBulletinBoardPosition());
    }


}

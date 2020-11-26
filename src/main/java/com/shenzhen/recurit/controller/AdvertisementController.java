package com.shenzhen.recurit.controller;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.AdvertisementPojo;
import com.shenzhen.recurit.pojo.NoticePojo;
import com.shenzhen.recurit.service.AdvertisementService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.AdvertisementVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "advertisement")
@Api(tags = {"广告信息"})
public class AdvertisementController {

    @Resource
    private AdvertisementService advertisementService;

    @PostMapping("addAdvertisement")
    @PermissionVerification
    @ApiImplicitParam(value = "新增广告" ,name = "advertisementVO",required = true)
    public ResultVO addAdvertisement(@RequestBody @ApiParam AdvertisementVO advertisementVO){
        AdvertisementPojo advertisementPojo = advertisementService.addAdvertisement(advertisementVO);
        return ResultVO.success(advertisementPojo);
    }

    @PutMapping("updateAdvertisement")
    @PermissionVerification
    @ApiImplicitParam(value = "修改广告" ,name = "advertisementVO",required = true)
    public ResultVO updateAdvertisement(@RequestBody @ApiParam AdvertisementVO advertisementVO){
        AdvertisementPojo advertisementPojo = advertisementService.updateAdvertisement(advertisementVO);
        return ResultVO.success(advertisementPojo);
    }

    @DeleteMapping("batchDeleteAdvertisement")
    @PermissionVerification
    @ApiImplicitParam(value = "批量删除" ,name = "idList",required = true)
    public ResultVO batchDeleteAdvertisement(@RequestBody @ApiParam List<Integer> idList){
        if(EmptyUtils.isEmpty(idList) || idList.isEmpty()){
            return ResultVO.error("请选择要删除得广告");
        }
        int result = advertisementService.batchDeleteAdvertisement(idList);
        if (result > NumberEnum.ZERO.getValue()) {
            return ResultVO.success("删除成功！");
        } else {
            return ResultVO.success("当前广告不存在！");
        }

    }

    @GetMapping(value = "getAdvertisements")
    @ApiOperation(value = "公告查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "每页个数", name = "pageSize", required = true),
            @ApiImplicitParam(value = "当前页面", name = "pageNum", required = true)
    })
    @PermissionVerification
    public ResultVO getAdvertisements(int pageSize, int pageNum) {
        if (pageNum == NumberEnum.ZERO.getValue()) {
            pageNum = NumberEnum.ONE.getValue();
        }
        if (pageSize == NumberEnum.ZERO.getValue()) {
            pageSize = NumberEnum.TEN.getValue();
        }
        PageInfo<AdvertisementPojo> pageInfo = advertisementService.getAdvertisementsByPage(pageNum, pageSize);
        return ResultVO.success(pageInfo);

    }
}

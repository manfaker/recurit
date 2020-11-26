package com.shenzhen.recurit.controller;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.NoticePojo;
import com.shenzhen.recurit.service.NoticeService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.NoticeVO;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "notice")
@Api(tags = {"公告信息"})
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @PostMapping(value = "addNotice")
    @ApiOperation(value = "新增公告信息")
    @ApiImplicitParam(value = "公告对象", name = "noticeVO",required = true)
    @PermissionVerification
    public ResultVO addNotice(@RequestBody @ApiParam NoticeVO noticeVO) {
        NoticePojo noticePojo = noticeService.addNotice(noticeVO);
        return ResultVO.success(noticePojo);
    }

    @PutMapping(value = "updateNotice")
    @ApiOperation(value = "修改公告信息")
    @ApiImplicitParam(value = "公告对象", name = "noticeVO",required = true)
    @PermissionVerification
    public ResultVO updateNotice(@RequestBody @ApiParam NoticeVO noticeVO) {
        NoticePojo noticePojo = noticeService.updateNotice(noticeVO);
        return ResultVO.success(noticePojo);
    }

    @DeleteMapping(value = "deleteNotice")
    @ApiOperation(value = "批量删除公告信息")
    @ApiImplicitParam(value = "主键集合", name = "idList")
    @PermissionVerification
    public ResultVO batchDeleteNotice(@RequestBody @ApiParam List<Integer> idList) {
        if (EmptyUtils.isEmpty(idList) || idList.isEmpty()) {
            return ResultVO.error("请先选择要删除得内容！");
        }
        int result = noticeService.batchDeleteNotice(idList);
        if (result > NumberEnum.ZERO.getValue()) {
            return ResultVO.success("删除成功！");
        } else {
            return ResultVO.success("当前公告不存在！");
        }
    }

    @PutMapping(value = "topNotice")
    @ApiOperation(value = "置顶公告信息")
    @ApiImplicitParam(value = "主键", name = "id", required = true)
    @PermissionVerification
    public ResultVO topNotice(int id) {
        int result = noticeService.topNotice(id);
        if (result > NumberEnum.ZERO.getValue()) {
            return ResultVO.success("置顶成功！");
        } else {
            return ResultVO.error("置顶失败！");
        }
    }

    @GetMapping(value = "getNotices")
    @ApiOperation(value = "公告查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "每页个数", name = "pageSize", required = true),
            @ApiImplicitParam(value = "当前页面", name = "pageNum", required = true)
    })
    @PermissionVerification
    public ResultVO getNotices(int pageSize, int pageNum) {
        if (pageNum == NumberEnum.ZERO.getValue()) {
            pageNum = NumberEnum.ONE.getValue();
        }
        if (pageSize == NumberEnum.ZERO.getValue()) {
            pageSize = NumberEnum.TEN.getValue();
        }
        PageInfo<NoticePojo> pageInfo = noticeService.getNoticeByPage(pageNum, pageSize);
        return ResultVO.success(pageInfo);

    }

}

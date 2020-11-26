package com.shenzhen.recurit.controller;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.service.DocumentService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.FileCommonUtils;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("document")
@Api(tags = {"文档"})
public class DocumentController {

    @Resource
    private DocumentService documentService;

    @PostMapping("upload/idCard")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件" ,name = "file",required = true),
            @ApiImplicitParam(value = "文件" ,name = "file",required = true),
            @ApiImplicitParam(value = "是否是需要显示得文件" ,name = "isImage",required = true)
    })
    public ResultVO saveIDCard(@ApiParam MultipartFile file,String category, boolean isImage){
        DocumentPojo documentPojo = FileCommonUtils.saveFile(category, file, isImage);
        if(EmptyUtils.isNotEmpty(documentPojo)){
            return ResultVO.success(documentPojo);
        }else{
            return ResultVO.error("文件异常");
        }
    }

    @DeleteMapping("deleteFile")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件id" ,name = "id",required = true),
            @ApiImplicitParam(value = "是否是需要显示得文件" ,name = "isImage",required = true)
    })
    public ResultVO deleteFile(int id, boolean isImage){
        int result = documentService.deleteDocument(id, isImage);
        if(result > NumberEnum.ZERO.getValue()){
            return ResultVO.success("删除成功");
        }else{
            return ResultVO.error("文件不存在");
        }
    }
}

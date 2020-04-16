package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.PositionUserRelationMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.*;
import com.shenzhen.recurit.utils.EmailUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.PositionUserRelationVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PositionUserRelationServiceImpl implements PositionUserRelationService {
    @Resource
    private PositionUserRelationMapper positionUserRelationMapper;
    @Resource
    private PositionService positionService;
    @Resource
    private UserService userService;
    @Resource
    private ResumeService resumeService;

    @Override
    public ResultVO saveBatchRelation(PositionUserRelationVO positionUserRelationVO) {
        if(EmptyUtils.isEmpty(positionUserRelationVO.getPositionIds())){
            return ResultVO.error("关联职位信息不能为空");
        }
        List<Integer> listPositionId = new ArrayList<>();
        for(String str : positionUserRelationVO.getPositionIds().split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(str)){
                listPositionId.add(Integer.valueOf(str));
            }
        }
        UserVO user = ThreadLocalUtils.getUser();
        List<PositionUserRelationVO> relationByPostionAndUser = positionUserRelationMapper.getRelationByPostionAndUser(listPositionId, user.getUserCode());
        List<PositionUserRelationVO> relations = new ArrayList<>();

        if(EmptyUtils.isNotEmpty(relationByPostionAndUser)&&relationByPostionAndUser.size()> NumberEnum.ZERO.getValue()){
            if(relationByPostionAndUser.size()==listPositionId.size()){
                return ResultVO.error("已添加，请勿重复操作");
            }
            for(int positionId : listPositionId){
                boolean flag = false;
                for(PositionUserRelationVO relation :relationByPostionAndUser ){
                    if(positionId==relation.getPositionId()){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    PositionUserRelationVO relation = new PositionUserRelationVO();
                    relation.setPositionId(positionId);
                    relation.setStatus(NumberEnum.ONE.getValue());
                    relation.setFollow(positionUserRelationVO.getFollow());
                    relation.setApply(positionUserRelationVO.getApply());
                    relation.setUserCode(user.getUserCode());
                    setRelationBaseInfo(relation,true);
                    relations.add(relation);
                }
            }
        }else{
            for(int positionId : listPositionId){
                PositionUserRelationVO relation = new PositionUserRelationVO();
                relation.setPositionId(positionId);
                relation.setStatus(NumberEnum.ONE.getValue());
                relation.setFollow(positionUserRelationVO.getFollow());
                relation.setApply(positionUserRelationVO.getApply());
                relation.setUserCode(user.getUserCode());
                setRelationBaseInfo(relation,true);
                relations.add(relation);
            }
        }
        positionUserRelationMapper.saveBatchRelation(relations);
        return ResultVO.success("保存成功");
    }

    /**
     *
     * @param relation
     * @param flag true
     */
    private void setRelationBaseInfo(PositionUserRelationVO relation,boolean flag){
        if(EmptyUtils.isEmpty(relation)){
            return;
        }
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            relation.setCreater(user.getUserName());
            relation.setCreateDate(new Date());
        }
        relation.setUpdater(user.getUserName());
        relation.setUpdateDate(new Date());
    }

    @Override
    public ResultVO deleteBatchRelation(PositionUserRelationVO positionUserRelationVO) {
        if(EmptyUtils.isEmpty(positionUserRelationVO.getPositionIds())){
            return ResultVO.error("移除对象不能为空");
        }
        List<Integer> listPositionId = new ArrayList<>();
        for(String str : positionUserRelationVO.getPositionIds().split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(str)){
                listPositionId.add(Integer.valueOf(str));
            }
        }
        UserVO user = ThreadLocalUtils.getUser();
        int result = positionUserRelationMapper.deleteBatchRelation(listPositionId, user.getUserCode());
        if(result>NumberEnum.ZERO.getValue()){
            return ResultVO.success("移除成功");
        }else{
            return ResultVO.success("移除失败");
        }
    }

    @Override
    public ResultVO updateRelation(PositionUserRelationVO positionUserRelationVO) {
        setRelationBaseInfo(positionUserRelationVO,false);
        int result = positionUserRelationMapper.updateRelation(positionUserRelationVO);
        if(result>NumberEnum.ZERO.getValue()){
            return ResultVO.success("修改成功");
        }
        return ResultVO.success("修改失败");
    }

    @Override
    public ResultVO createOrUpdateRelation(PositionUserRelationVO positionUserRelationVO) {
        if(EmptyUtils.isEmpty(positionUserRelationVO.getPositionId())){
            return ResultVO.error("职位信息不能为空");
        }
        UserVO user = ThreadLocalUtils.getUser();
        PositionUserRelationVO relation = positionUserRelationMapper.getRelationByPositionIdAndUserCode(positionUserRelationVO.getPositionId(), user.getUserCode());
        if(EmptyUtils.isNotEmpty(relation)){
            positionUserRelationVO.setId(relation.getId());
            setRelationBaseInfo(positionUserRelationVO,false);
            int result = positionUserRelationMapper.updateRelation(positionUserRelationVO);
            if(result>NumberEnum.ZERO.getValue()){
                return ResultVO.success("修改成功");
            }
            return ResultVO.success("修改失败");
        }else{
            setRelationBaseInfo(positionUserRelationVO,true);
            positionUserRelationVO.setUserCode(user.getUserCode());
            positionUserRelationMapper.saveRelation(positionUserRelationVO);
            return ResultVO.success("添加成功");
        }

    }
    
    public ResultVO sendResumeEmail(String userCode){
        UserVO userVO = ThreadLocalUtils.getUser();
        UserPojo userPojo = resumeService.getResumeInfoByUserCode(userCode);
        if(EmptyUtils.isNotEmpty(userVO.getEmail())){
            EmailUtils.sendResume(userVO.getEmail(),userPojo);
        }
       return ResultVO.success();
    }
}

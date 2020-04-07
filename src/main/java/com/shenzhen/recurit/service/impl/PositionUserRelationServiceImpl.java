package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.PositionUserRelationMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.PositionUserRelationService;
import com.shenzhen.recurit.service.UserService;
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

    @Override
    public ResultVO saveBatchRelation(String positionIds, int relationStatus) {
        if(EmptyUtils.isEmpty(positionIds)){
            return ResultVO.error("关联职位信息不能为空");
        }
        List<Integer> listPositionId = new ArrayList<>();
        for(String str : positionIds.split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(str)){
                listPositionId.add(Integer.valueOf(str));
            }
        }
        UserVO user = ThreadLocalUtils.getUser();
        List<PositionUserRelationVO> relationByPostionAndUser = positionUserRelationMapper.getRelationByPostionAndUser(listPositionId, user.getUserCode(), relationStatus);
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
                    relation.setRelationStatus(relationStatus);
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
                relation.setRelationStatus(relationStatus);
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
    public ResultVO deleteBatchRelation(String positionIds, int relationStatus) {
        if(EmptyUtils.isEmpty(positionIds)){
            return ResultVO.error("移除对象不能为空");
        }
        List<Integer> listPositionId = new ArrayList<>();
        for(String str : positionIds.split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(str)){
                listPositionId.add(Integer.valueOf(str));
            }
        }
        UserVO user = ThreadLocalUtils.getUser();
        int result = positionUserRelationMapper.deleteBatchRelation(listPositionId, user.getUserCode(), relationStatus);
        if(result>NumberEnum.ZERO.getValue()){
            return ResultVO.success("移除成功");
        }else{
            return ResultVO.success("移除失败");
        }
    }
}

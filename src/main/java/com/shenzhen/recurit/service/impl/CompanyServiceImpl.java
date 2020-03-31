package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.CompanyMapper;
import com.shenzhen.recurit.dao.UserMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.service.CompanyService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.CompanyVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2020/3/29
 * LiXiaoWei
 */
@Service
public class CompanyServiceImpl implements CompanyService {


    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private UserMapper userMapper;

    @Transactional
    @Override
    public ResultVO saveCompany(CompanyVO companyVO) {
        UserVO userVO = ThreadLocalUtils.getUser();
        ResultVO resultVO = isEmpty(companyVO);
        if(!resultVO.getFlag()){
            return resultVO;
        }
        CompanyVO companyPojo = companyMapper.getCompanyByCode(companyVO.getCompanyCode());
        if(EmptyUtils.isNotEmpty(companyPojo)&&companyPojo.getCompanyName().equals(companyVO.getCompanyName())){
            return ResultVO.error("公司已存在");
        }
        companyVO.setUserCode(userVO.getUserCode());
        companyVO.setStatus(NumberEnum.THREE.getValue());
        int saveCompanyResult = companyMapper.saveCompany(companyVO);
        if(saveCompanyResult> NumberEnum.ZERO.getValue()){
            userVO.setCompanyCode(companyVO.getCompanyCode());
            int updateUserResult = userMapper.updateUser(userVO);
            if(updateUserResult> NumberEnum.ZERO.getValue()){
               return ResultVO.success("公司添加成功,公司信息待审核");
            }
        }
        return ResultVO.error("公司添加失败");
    }
    public ResultVO isEmpty(CompanyVO companyVO){
        try {
            if (EmptyUtils.isEmpty(companyVO.getCompanyName())){
                throw new Exception("公司名字不能为空");
            }
            if (EmptyUtils.isEmpty(companyVO.getCompanyCode())){
                throw new Exception("公司营业证编号不能为空");
            }
            if (EmptyUtils.isEmpty(companyVO.getAddress())){
                throw new Exception("公司地址不能为空");
            }
            if (EmptyUtils.isEmpty(companyVO.getLegalRepresentative())){
                throw new Exception("公司法人代表不能为空");
            }
        }catch (Exception e){
            return ResultVO.error(e.getMessage());
        }
        return ResultVO.success();
    }
    @Override
    public CompanyVO getByCompany(CompanyVO companyVO) {
        if (EmptyUtils.isNotEmpty(companyVO)){
            if (EmptyUtils.isNotEmpty(companyVO.getCompanyCode())){
                return companyMapper.getCompanyByCode(companyVO.getCompanyCode());
            }
            if (EmptyUtils.isNotEmpty(companyVO.getLegalRepresentative())){
                return companyMapper.getCompanyByLegalRepresentative(companyVO.getLegalRepresentative());
            }
        }
        return null;
    }

    @Override
    public ResultVO getCompanyById(int CompanyId) {
        if (EmptyUtils.isEmpty(CompanyId)){
            return ResultVO.error("companyName不能为空");
        }
        CompanyVO companyVO = new CompanyVO();
        companyVO.setId(CompanyId);
        CompanyVO getCompanyResult = getByCompany(companyVO);
        if (EmptyUtils.isEmpty(getCompanyResult)){
            return ResultVO.success("没有找到该公司");
        }
        return ResultVO.success("查询成功",getCompanyResult);
    }

    @Override
    public ResultVO getCompanyByCode(String companyCode) {
        if (EmptyUtils.isEmpty(companyCode)){
            return ResultVO.error("companyCode不能为空");
        }
        CompanyVO companyVO = new CompanyVO();
        companyVO.setCompanyCode(companyCode);
        CompanyVO getCompanyResult = getByCompany(companyVO);
        if (EmptyUtils.isEmpty(getCompanyResult)){
            return ResultVO.success("没有找到该公司");
        }
        return ResultVO.success("查询成功",getCompanyResult);
    }
    @Override
    public ResultVO getCompanyByName(String companyName) {
        if (EmptyUtils.isEmpty(companyName)){
            return ResultVO.error("companyName不能为空");
        }
        List <CompanyVO> list = companyMapper.getCompanyByName(companyName);
        return ResultVO.success("查询成功",list);
    }

    @Override
    public ResultVO updateCompany(CompanyVO companyVO) {
        UserVO userVO = ThreadLocalUtils.getUser();
        if(userVO.getUserCode().equals(getByCompany(companyVO).getUserCode())){
            ResultVO resultVO = isEmpty(companyVO);
            if (!resultVO.getFlag()){
                return resultVO;
            }
            CompanyVO companyPojo = companyMapper.getCompanyByCode(companyVO.getCompanyCode());
            if(EmptyUtils.isNotEmpty(companyPojo)&&companyPojo.getCompanyName().equals(companyVO.getCompanyName())){
                return ResultVO.error("公司已存在");
            }
            companyVO.setStatus(NumberEnum.THREE.getValue());
            int updateCompanyResult = companyMapper.updateCompany(companyVO);
            if(updateCompanyResult> NumberEnum.ZERO.getValue()){
                userVO.setCompanyCode(companyVO.getCompanyCode());
                int updateUserResult = userMapper.updateUser(userVO);
                if(updateUserResult> NumberEnum.ZERO.getValue()){
                    return ResultVO.success("公司添加成功,公司信息待审核");
                }
            }
        }

        return ResultVO.error("添加失败，不是该公司的第一个创建人");
    }


}

package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.SocialSecurityInfoMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.pojo.SocialSecurityInfoPojo;
import com.shenzhen.recurit.pojo.SocialStandardPojo;
import com.shenzhen.recurit.service.ActivityPackageService;
import com.shenzhen.recurit.service.SocialSecurityInfoService;
import com.shenzhen.recurit.service.SocialStandardService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SocialSecurityInfoServiceImpl implements SocialSecurityInfoService {
    @Resource
    private SocialStandardService socialStandardService;
    @Resource
    private ActivityPackageService activityPackageService;
    @Resource
    private SocialSecurityInfoMapper socialSecurityInfoMapper;

    @Override
    public Object calculatePrice(String jsonData) {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        //社保比例 id  套餐 id 基数
        int standardId = jsonObject.getInteger("standardId");
        String packageIds = jsonObject.getString("packageIds");
        int cardinality = jsonObject.getInteger("cardinality");
        JSONObject socilObject = new JSONObject();
        SocialStandardPojo socialStandardPojo = socialStandardService.getSocialStandardById(standardId);
        if(socialStandardPojo.getLowCardinality()>cardinality||socialStandardPojo.getHightCardinality()<cardinality){
            if(socialStandardPojo.getLowCardinality()==socialStandardPojo.getHightCardinality()){
                return ResultVO.error("社保基数应该是"+socialStandardPojo.getLowCardinality());
            }else{
                return ResultVO.error("社保基数应该在"+socialStandardPojo.getLowCardinality()+"-"+socialStandardPojo.getHightCardinality()+"之间");
            }
        }
        List<Integer> listPackageId = strToList(packageIds);
        List<ActivityPackagePojo> listPackage = activityPackageService.getAllActivityPackageByIds(listPackageId);
        calculateProjectPrice(socialStandardPojo,cardinality,socilObject,listPackage);
        return ResultVO.success(socilObject);
    }

    private List<Integer> strToList(String packageIds){
        if(EmptyUtils.isEmpty(packageIds)){
            return null;
        }
        List<Integer> listPackageId = new ArrayList<>();
        for(String packageId:packageIds.split(OrdinaryConstant.SYMBOL_4)){
            if(EmptyUtils.isNotEmpty(packageId)){
                listPackageId.add(Integer.valueOf(packageId));
            }
        }
        return listPackageId;
    }

    private void calculateProjectPrice(SocialStandardPojo socialStandardPojo,int cardinality,JSONObject socilObject,List<ActivityPackagePojo> listPackage){
        JSONArray socilArray = new JSONArray();
        JSONObject pensionJson = new JSONObject();
        int enterprisePension = cardinality*socialStandardPojo.getEnterprisePension()/10000;
        int personPension = cardinality*socialStandardPojo.getPersonPension()/10000;
        int countPension = enterprisePension+personPension;
        pensionJson.put("contributedItems","养老保险");
        pensionJson.put("proportion",socialStandardPojo.getEnterprisePension());
        pensionJson.put("enterprisePension",enterprisePension);
        pensionJson.put("personProportion",socialStandardPojo.getPersonPension());
        pensionJson.put("personPension",personPension);
        pensionJson.put("subtotal",countPension);
        socilArray.add(pensionJson);
        JSONObject medicalJson = new JSONObject();
        int enterpriseMedical = cardinality*socialStandardPojo.getEnterpriseMedical()/10000;
        int personMedical = cardinality*socialStandardPojo.getPersonMedical()/10000;
        int countMedical = enterpriseMedical+personMedical;
        medicalJson.put("contributedItems","医疗保险");
        medicalJson.put("proportion",socialStandardPojo.getEnterpriseMedical());
        medicalJson.put("enterprisePension",enterpriseMedical);
        medicalJson.put("personProportion",socialStandardPojo.getPersonMedical());
        medicalJson.put("personPension",personMedical);
        medicalJson.put("subtotal",countMedical);
        socilArray.add(medicalJson);
        JSONObject unemploymentJson = new JSONObject();
        int enterpriseUnemployment = cardinality*socialStandardPojo.getEnterpriseUnemployment()/10000;
        int personUnemployment = cardinality*socialStandardPojo.getPersonUnemployment()/10000;
        int countUnemployment = enterpriseUnemployment+personUnemployment;
        unemploymentJson.put("contributedItems","失业保险");
        unemploymentJson.put("proportion",socialStandardPojo.getEnterpriseUnemployment());
        unemploymentJson.put("enterprisePension",enterpriseUnemployment);
        unemploymentJson.put("personProportion",socialStandardPojo.getPersonUnemployment());
        unemploymentJson.put("personPension",personUnemployment);
        unemploymentJson.put("subtotal",countUnemployment);
        socilArray.add(unemploymentJson);
        JSONObject injuryJson = new JSONObject();
        int enterpriseInjury = cardinality*socialStandardPojo.getEnterpriseInjury()/10000;
        int personInjury = cardinality*socialStandardPojo.getPersonInjury()/10000;
        int countInjury = enterpriseUnemployment+personUnemployment;
        injuryJson.put("contributedItems","工伤保险");
        injuryJson.put("proportion",socialStandardPojo.getEnterpriseInjury());
        injuryJson.put("enterprisePension",enterpriseInjury);
        injuryJson.put("personProportion",socialStandardPojo.getPersonInjury());
        injuryJson.put("personPension",personInjury);
        injuryJson.put("subtotal",countInjury);
        socilArray.add(injuryJson);
        JSONObject childbirthJson = new JSONObject();
        int enterpriseChildbirth = cardinality*socialStandardPojo.getEnterpriseChildbirth()/10000;
        int personChildbirth = cardinality*socialStandardPojo.getPersonChildbirth()/10000;
        int countChildbirth = enterpriseUnemployment+personUnemployment;
        childbirthJson.put("contributedItems","生育保险");
        childbirthJson.put("proportion",socialStandardPojo.getEnterpriseChildbirth());
        childbirthJson.put("enterprisePension",enterpriseChildbirth);
        childbirthJson.put("personProportion",socialStandardPojo.getPersonChildbirth());
        childbirthJson.put("personPension",personChildbirth);
        childbirthJson.put("subtotal",countChildbirth);
        socilArray.add(childbirthJson);
        JSONObject insuranceJson = new JSONObject();
        int enterpriseInsurance = cardinality*socialStandardPojo.getDisabilityInsurance()/10000;
        int personInsurance = 0;
        int countInsurance = enterpriseUnemployment+personUnemployment;
        insuranceJson.put("contributedItems","残保金");
        insuranceJson.put("proportion",socialStandardPojo.getDisabilityInsurance());
        insuranceJson.put("enterprisePension",enterpriseInsurance);
        insuranceJson.put("personProportion",0);
        insuranceJson.put("personPension",personInsurance);
        insuranceJson.put("subtotal",countInsurance);
        socilArray.add(insuranceJson);
        socilObject.put("socialSecurity",socilArray);
        int enterpriseCount = enterprisePension+enterpriseMedical+enterpriseUnemployment+enterpriseInjury+enterpriseChildbirth+enterpriseInsurance;
        int personCountEnterprise = personPension+personUnemployment+personMedical+personInjury+personChildbirth+personInsurance;
        JSONObject countJson = new JSONObject();
        countJson.put("count","小计");
        countJson.put("enterpriseCount",enterpriseCount);
        countJson.put("personCountEnterprise",personCountEnterprise);
        socilObject.put("socialCount",countJson);
        int countMont = getAllMonth(listPackage);
        socilObject.put("socilMoney",(enterpriseCount+personCountEnterprise)*countMont);
        JSONArray jsonArray  = new JSONArray();
        int serviceFee = getServiceFee(listPackage,false,false,jsonArray);
        socilObject.put("package",jsonArray);
        socilObject.put("serviceFee",serviceFee);
        socilObject.put("totalCount",(enterpriseCount+personCountEnterprise)*countMont+serviceFee);
    }

    private int getAllMonth(List<ActivityPackagePojo> listPackage){
        if(EmptyUtils.isEmpty(listPackage)||listPackage.size()==NumberEnum.ZERO.getValue()){
            return NumberEnum.ZERO.getValue();
        }
        int countMonth=NumberEnum.ZERO.getValue();
        for(ActivityPackagePojo packagePojo:listPackage){
            countMonth=countMonth+packagePojo.getAmount();
        }
        return countMonth;

    }

    /**
     *
     * @param listPackage
     * @param flag  是否是团队，企业购买
     * @param isGroup 是否是团队
     * @return
     */
    private int getServiceFee(List<ActivityPackagePojo> listPackage,boolean flag,boolean isGroup,JSONArray jsonArray){
        if(EmptyUtils.isEmpty(listPackage)){
            return NumberEnum.ZERO.getValue();
        }
        int countPrice = NumberEnum.ZERO.getValue();
        for(ActivityPackagePojo activityPackage :listPackage ){
            JSONObject jsonObject = new JSONObject();
            int serviceFee=activityPackage.getAmount();
            if(flag){
                if(isGroup){
                    if(activityPackage.getGroupPrice()>0){
                        serviceFee=activityPackage.getGroupPrice();
                    }
                }else{
                    if(activityPackage.getEnterprisePrice()>0){
                        serviceFee=activityPackage.getEnterprisePrice();
                    }
                }
            }
            if(activityPackage.getDiscount()>0){
                serviceFee = serviceFee*activityPackage.getDiscount()/100;
            }
            if(activityPackage.getPromotePrice()>0){
                serviceFee = serviceFee*activityPackage.getPromotePrice()/100;
            }

            jsonObject.put("id",activityPackage.getId());
            jsonObject.put("packageName",activityPackage.getServicePeriod());
            jsonObject.put("serviceFee",serviceFee);
            countPrice=countPrice+serviceFee;
            jsonArray.add(jsonObject);
        }

        return countPrice;
    }

    @Override
    public SocialSecurityInfoPojo saveSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO) {
        setSocialSecurityInfo(socialSecurityInfoVO,true);
        socialSecurityInfoMapper.saveSocialSecurityInfo(socialSecurityInfoVO);
        return getSocialSecuritInfoById(socialSecurityInfoVO.getId());
    }

    private void setSocialSecurityInfo (SocialSecurityInfoVO socialSecurityInfoVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            socialSecurityInfoVO.setCreateDate(new Date());
            socialSecurityInfoVO.setCreater(user.getUserName());
        }
        socialSecurityInfoVO.setUpdateDate(new Date());
        socialSecurityInfoVO.setUpdater(user.getUserName());
    }

    @Override
    public int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO) {
        setSocialSecurityInfo(socialSecurityInfoVO,false);
        return socialSecurityInfoMapper.updateSocialSecuritInfo(socialSecurityInfoVO);
    }

    @Override
    public SocialSecurityInfoPojo getSocialSecuritInfoById(int id) {
        return socialSecurityInfoMapper.getSocialSecuritInfoById(id);
    }

    @Override
    public int deleteSecuritInfoById(int id) {
        return socialSecurityInfoMapper.deleteSecuritInfoById(id);
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSocialSecuritInfoByIds(List<Integer> ids) {
        if(EmptyUtils.isEmpty(ids)||ids.size()==NumberEnum.ZERO.getValue()){
            return null;
        }
        return socialSecurityInfoMapper.getAllSocialSecuritInfoByIds(ids);
    }

    @Override
    public int batchUpdateSocialSecuritInfo(List<Integer> ids,int orderInfoId) {
        if(EmptyUtils.isEmpty(ids)||ids.size()==NumberEnum.ZERO.getValue()){
            return 0;
        }
        return socialSecurityInfoMapper.batchUpdateSocialSecuritInfo(ids, orderInfoId);
    }

    @Override
    public int deleteByOrderInfoId(int orderInfoId) {
        return socialSecurityInfoMapper.deleteByOrderInfoId(orderInfoId);
    }

    @Override
    public int batchRemoveOrderInfoIds(List<Integer> ids) {
        return socialSecurityInfoMapper.batchRemoveOrderInfoIds(ids);
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecuritInfo() {
        UserVO user = ThreadLocalUtils.getUser();
        return socialSecurityInfoMapper.getAllSecuritInfo(user.getUserCode());
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecuritInfoByOrderInfoId(int orderInfoId) {
        return socialSecurityInfoMapper.getAllSecuritInfoByOrderInfoId(orderInfoId);
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecuritInfoByIdCard(String idCard) {
        return socialSecurityInfoMapper.getAllSecuritInfoByIdCard(idCard);
    }



}

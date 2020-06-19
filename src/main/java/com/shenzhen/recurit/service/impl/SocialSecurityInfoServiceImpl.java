package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.SocialSecurityInfoMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.*;
import com.shenzhen.recurit.service.*;
import com.shenzhen.recurit.utils.ApplyConfigUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ImageBase64Utils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.utils.excel.ExportUtils;
import com.shenzhen.recurit.vo.OrderInfoVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SocialSecurityInfoVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SocialSecurityInfoServiceImpl implements SocialSecurityInfoService {
    @Resource
    private SocialStandardService socialStandardService;
    @Resource
    private ActivityPackageService activityPackageService;
    @Resource
    private SocialSecurityInfoMapper socialSecurityInfoMapper;
    @Resource
    private DocumentService documentService;
    @Resource
    private OrderInfoService orderInfoService;

    
    @Override
    public Object calculatePrice(String jsonData) {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        //社保比例 id  套餐 id 基数
        int standardId = jsonObject.getInteger("standardId");
        String packageIds = jsonObject.getString("feePackageIds");
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
        List<Integer> listPackageId = new ArrayList<>();
        Map<Integer, Integer> mapAmount = new HashMap<>();
        addJsonToCollection(packageIds,mapAmount,listPackageId);
        List<ActivityPackagePojo> listPackage = activityPackageService.getAllActivityPackageByIds(listPackageId);
        calculateProjectPrice(socialStandardPojo,cardinality,socilObject,listPackage,mapAmount);
        return ResultVO.success(socilObject);
    }

    private void addJsonToCollection(String packageIds,Map<Integer, Integer> mapAmount,List<Integer> listPackageId){
        if(EmptyUtils.isEmpty(packageIds)){
            return;
        }
        JSONArray jsonArray = JSON.parseArray(packageIds);
        for(int index=NumberEnum.ZERO.getValue();index<jsonArray.size();index++){
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            int packageId = jsonObject.getInteger("packageId");
            int amount = jsonObject.getInteger("amount");
            mapAmount.put(packageId,amount);
            listPackageId.add(packageId);
        }
    }

    private void calculateProjectPrice(SocialStandardPojo socialStandardPojo,int cardinality,JSONObject socilObject,List<ActivityPackagePojo> listPackage,Map<Integer, Integer> mapAmount){
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
        int countMont = getAllMonth(listPackage,mapAmount);
        socilObject.put("socilMoney",(enterpriseCount+personCountEnterprise)*countMont);
        JSONArray jsonArray  = new JSONArray();
        int serviceFee = getServiceFee(listPackage,false,false,jsonArray);
        socilObject.put("package",jsonArray);
        socilObject.put("serviceFee",serviceFee);
        socilObject.put("totalCount",(enterpriseCount+personCountEnterprise)*countMont+serviceFee);
    }

    private int getAllMonth(List<ActivityPackagePojo> listPackage,Map<Integer, Integer> mapAmount){
        if(EmptyUtils.isEmpty(listPackage)||listPackage.size()==NumberEnum.ZERO.getValue()){
            return NumberEnum.ZERO.getValue();
        }
        int countMonth=NumberEnum.ZERO.getValue();
        for(ActivityPackagePojo packagePojo:listPackage){
            countMonth=countMonth+packagePojo.getAmount()*mapAmount.get(packagePojo.getId());
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
        setSocialSecurityEndDate(socialSecurityInfoVO);
        setSocialSecurityMoney(socialSecurityInfoVO);
        socialSecurityInfoMapper.saveSocialSecurityInfo(socialSecurityInfoVO);
        return getSocialSecuritInfoById(socialSecurityInfoVO.getId());
    }

    private void setSocialSecurityMoney(SocialSecurityInfoVO socialSecurityInfoVO){
        int standId = socialSecurityInfoVO.getId();
        SocialStandardPojo socialStandardPojo = socialStandardService.getSocialStandardById(standId);
        //社保基数
        int cardinality=socialSecurityInfoVO.getCardinality();
        int socialSecurityPrice =cardinality*socialStandardPojo.getEnterprisePension()/10000+
                                    cardinality*socialStandardPojo.getPersonPension()/10000+
                                    cardinality*socialStandardPojo.getEnterpriseMedical()/10000+
                                    cardinality*socialStandardPojo.getPersonMedical()/10000+
                                    cardinality*socialStandardPojo.getEnterpriseUnemployment()/10000+
                                    cardinality*socialStandardPojo.getPersonUnemployment()/10000+
                                    cardinality*socialStandardPojo.getEnterpriseChildbirth()/10000+
                                    cardinality*socialStandardPojo.getPersonChildbirth()/10000+
                                    cardinality*socialStandardPojo.getEnterpriseInjury()/10000+
                                    cardinality*socialStandardPojo.getPersonInjury()/10000+
                                    cardinality*socialStandardPojo.getDisabilityInsurance()/10000;
        socialSecurityInfoVO.setSocialSecurityPrice(socialSecurityPrice);
        String activityJson = socialSecurityInfoVO.getFeePackageIds();
        if(EmptyUtils.isNotEmpty(activityJson)){
            JSONArray jsonArray = JSON.parseArray(activityJson);
            List<ActivityPackagePojo> allActivityPackage = activityPackageService.getAllActivityPackage();
            Map<Integer,ActivityPackagePojo> mapPackage = new HashMap<>();
            listToMap(allActivityPackage,mapPackage);
            int countMount = NumberEnum.ZERO.getValue();
            int countMoney = NumberEnum.ZERO.getValue();
            for(int index=0;index<jsonArray.size();index++){
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                int packageId = jsonObject.getInteger("packageId");
                int amount = jsonObject.getInteger("amount");
                ActivityPackagePojo activityPackagePojo = mapPackage.get(packageId);
                countMount += amount*activityPackagePojo.getAmount();
                int price = amount*activityPackagePojo.getAmount()*activityPackagePojo.getPrice();
                if(activityPackagePojo.getPromotePrice()>NumberEnum.ZERO.getValue()){
                    price = price*activityPackagePojo.getPromotePrice()/100;
                }
                countMoney +=price ;
            }
            int allCountMoney = countMoney+countMount*socialSecurityPrice;
            socialSecurityInfoVO.setCountMoney(allCountMoney);
        }

    }

    private void setSocialSecurityEndDate(SocialSecurityInfoVO socialSecurityInfoVO){
        if(EmptyUtils.isNotEmpty(socialSecurityInfoVO.getFeePackageIds())&&EmptyUtils.isNotEmpty(socialSecurityInfoVO.getSocialSecurityDate())){
            List<Integer> listPackageId = new ArrayList<>();
            Map<Integer, Integer> mapAmount = new HashMap<>();
            addJsonToCollection(socialSecurityInfoVO.getFeePackageIds(),mapAmount,listPackageId);
            /**
             * 计算到期日期
             */
            List<ActivityPackagePojo> listPackage = activityPackageService.getAllActivityPackageByIds(listPackageId);
            int countMont = getAllMonth(listPackage,mapAmount);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(socialSecurityInfoVO.getSocialSecurityDate());
            calendar.add(Calendar.MONTH,countMont);
            socialSecurityInfoVO.setSocialSecurityEndDate(calendar.getTime());
        }
    }

    private void setSocialSecurityInfo (SocialSecurityInfoVO socialSecurityInfoVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            socialSecurityInfoVO.setCreateDate(new Date());
            socialSecurityInfoVO.setCreater(user.getUserName());
            socialSecurityInfoVO.setUserCode(user.getUserCode());
        }
        socialSecurityInfoVO.setUpdateDate(new Date());
        socialSecurityInfoVO.setUpdater(user.getUserName());
    }

    @Override
    public int updateSocialSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO) {
        setSocialSecurityInfo(socialSecurityInfoVO,false);
        setSocialSecurityEndDate(socialSecurityInfoVO);
        setSocialSecurityMoney(socialSecurityInfoVO);
        return socialSecurityInfoMapper.updateSocialSecuritInfo(socialSecurityInfoVO);
    }

    @Override
    public SocialSecurityInfoPojo getSocialSecuritInfoById(int id) {
        SocialSecurityInfoPojo socialSecurityInfo = socialSecurityInfoMapper.getSocialSecuritInfoById(id);
        if(EmptyUtils.isNotEmpty(socialSecurityInfo)){
            imageToBase64(socialSecurityInfo);
            //获取所有的套餐信息
            List<ActivityPackagePojo> listPackages = activityPackageService.getAllActivityPackage();
            Map<Integer,ActivityPackagePojo> mapPackages = new HashMap<>();
            listToMap(listPackages,mapPackages);
            JSONObject jsonObject = new JSONObject();
            calculateSingleSocialInfo(socialSecurityInfo,mapPackages,jsonObject);
            socialSecurityInfo.setCaculatePrice(jsonObject);
        }
        return socialSecurityInfo;
    }

    /**
     * 社保图片转base64格式
     */
    private void imageToBase64(SocialSecurityInfoPojo socialSecurityInfo){
        if(socialSecurityInfo.getPositiveIdCard()>NumberEnum.ZERO.getValue()){
            DocumentPojo positiveIdCard = documentService.getDocument(socialSecurityInfo.getPositiveIdCard(), NumberEnum.ONE.getValue());
            if(EmptyUtils.isNotEmpty(positiveIdCard)){
                String positivePath = positiveIdCard.getUrl()+ File.separator+positiveIdCard.getDocumentName()+OrdinaryConstant.SYMBOL_5+positiveIdCard.getSuffix();
                socialSecurityInfo.setPositiveIdCardInfo(ImageBase64Utils.encryptToBase64(positivePath));
            }
        }
        if(socialSecurityInfo.getReverseIdCard()>NumberEnum.ZERO.getValue()){
            DocumentPojo  reverseIdCard= documentService.getDocument(socialSecurityInfo.getReverseIdCard(), NumberEnum.ONE.getValue());
            if(EmptyUtils.isNotEmpty(reverseIdCard)){
                String reversePath = reverseIdCard.getUrl()+ File.separator+reverseIdCard.getDocumentName()+OrdinaryConstant.SYMBOL_5+reverseIdCard.getSuffix();
                socialSecurityInfo.setReverseIdCardInfo(ImageBase64Utils.encryptToBase64(reversePath));
            }
        }
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
        List<SocialSecurityInfoPojo> allSecuritInfo = socialSecurityInfoMapper.getAllSecuritInfo(user.getUserCode());
        setListSocialSecurity(allSecuritInfo);
        return allSecuritInfo;
    }

    private void setListSocialSecurity(List<SocialSecurityInfoPojo> allSecuritInfo){
        List<ActivityPackagePojo> listPackages = activityPackageService.getAllActivityPackage();
        Map<Integer,ActivityPackagePojo> mapPackages = new HashMap<>();
        listToMap(listPackages,mapPackages);
        if(EmptyUtils.isNotEmpty(allSecuritInfo)){
            for(SocialSecurityInfoPojo securityInfoPojo:allSecuritInfo){
                JSONObject jsonObject = new JSONObject();
                calculateSingleSocialInfo(securityInfoPojo,mapPackages,jsonObject);
                securityInfoPojo.setAllCountMoney(jsonObject.getInteger("totalCount"));
            }
        }
    }

    /**
     * 计算单个社保的价格
     * @param securityInfoPojo 社保详情
     * @param mapPackages  所有套餐信息
     * @param jsonObject   存储套餐计算后的信息
     */
    private void calculateSingleSocialInfo(SocialSecurityInfoPojo securityInfoPojo,Map<Integer,ActivityPackagePojo> mapPackages,JSONObject jsonObject){
        int cardinality = securityInfoPojo.getCardinality();
        String feePackageIds = securityInfoPojo.getFeePackageIds();
        List<ActivityPackagePojo> currPackages=new ArrayList<>();
        List<Integer> listPackageId = new ArrayList<>();
        Map<Integer, Integer> mapAmount = new HashMap<>();
        addJsonToCollection(feePackageIds,mapAmount,listPackageId);
        if(EmptyUtils.isNotEmpty(feePackageIds)){
            JSONArray packageArray = JSON.parseArray(feePackageIds);
            for(int index=NumberEnum.ZERO.getValue();index<packageArray.size();index++){
                JSONObject packageJson = packageArray.getJSONObject(index);
                int packageId = packageJson.getInteger("packageId");
                int amount = packageJson.getInteger("amount");
                mapAmount.put(packageId,amount);
                currPackages.add(mapPackages.get(packageId));
            }
        }
        calculateProjectPrice(securityInfoPojo.getSocialStandardPojo(),cardinality,jsonObject,currPackages,mapAmount);
    }

    private void listToMap(List<ActivityPackagePojo> listPackages,Map<Integer,ActivityPackagePojo> mapPackages){
        for(ActivityPackagePojo packagePojo : listPackages){
            if(EmptyUtils.isNotEmpty(packagePojo)){
                mapPackages.put(packagePojo.getId(),packagePojo);
            }
        }
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecuritInfoByOrderInfoId(int orderInfoId) {
        return socialSecurityInfoMapper.getAllSecuritInfoByOrderInfoId(orderInfoId);
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecuritInfoByIdCard(String idCard) {
        return socialSecurityInfoMapper.getAllSecuritInfoByIdCard(idCard);
    }

    @Override
    public int totalMonth(SocialSecurityInfoVO socialSecurityInfoVO) {
        String feePackageIds=socialSecurityInfoVO.getFeePackageIds();
        if(EmptyUtils.isNotEmpty(feePackageIds)){
            JSONArray jsonArray = JSON.parseArray(feePackageIds);
            List<Integer> listPackageIds = new ArrayList<>();
            Map<Integer,Integer> packageMap = new HashMap<>();
            for(int index=NumberEnum.ZERO.getValue();index<jsonArray.size();index++){
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                int packageId = jsonObject.getInteger("packageId");
                int amount = jsonObject.getInteger("amount");
                packageMap.put(packageId,amount);
                listPackageIds.add(packageId);
            }
            List<ActivityPackagePojo> listPackage = activityPackageService.getAllActivityPackageByIds(listPackageIds);
            int count =getAllMonth(listPackage,packageMap);
            return count;
        }
        return 0;
    }

    @Override
    public ResultVO saveDirectSecuritInfo(SocialSecurityInfoVO socialSecurityInfoVO) {
        setSocialSecurityInfo(socialSecurityInfoVO,true);
        setSocialSecurityEndDate(socialSecurityInfoVO);
        socialSecurityInfoMapper.saveSocialSecurityInfo(socialSecurityInfoVO);
        SocialSecurityInfoPojo socialSecurityInfoPojo = getSocialSecuritInfoById(socialSecurityInfoVO.getId());
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        orderInfoVO.setPayStatus(NumberEnum.ONE.getValue());
        orderInfoVO.setTotalAmount(socialSecurityInfoPojo.getCaculatePrice().getInteger("totalCount"));
        orderInfoVO.setSubject(socialSecurityInfoVO.getSubject());
        orderInfoVO.setSocialInfoIds(socialSecurityInfoPojo.getId()+OrdinaryConstant.IS_BLACK);
        OrderInfoPojo orderInfoPojo = orderInfoService.saveOrderInfo(orderInfoVO);
        if(EmptyUtils.isNotEmpty(orderInfoPojo)){
            return ApplyConfigUtils.preCreate( InformationConstant.ALIPAY,orderInfoVO);
        }
        return ResultVO.error(orderInfoPojo);
    }

    @Override
    public ResultVO inspectDedupleSocialInfo(SocialSecurityInfoVO socialSecurityInfoVO) {
        String idCard = socialSecurityInfoVO.getIdCard();
        List<SocialSecurityInfoPojo> listSocialInfo = getAllSecuritInfoByIdCard(idCard);
        if(EmptyUtils.isNotEmpty(listSocialInfo)){
            int count = totalMonth(socialSecurityInfoVO);
            Date socialSecurityDate=socialSecurityInfoVO.getSocialSecurityDate();
            Calendar calendar =Calendar.getInstance();
            calendar.setTime(socialSecurityDate);
            calendar.add(Calendar.MONTH,count);
            Date endDate = calendar.getTime();
            for(SocialSecurityInfoPojo socialInfo: listSocialInfo){
                if((socialSecurityInfoVO.getSocialSecurityDate().compareTo(socialInfo.getSocialSecurityDate())!=-1&&socialSecurityInfoVO.getSocialSecurityDate().compareTo(socialInfo.getSocialSecurityEndDate())==-1)
                        ||(endDate.compareTo(socialInfo.getSocialSecurityDate())!=-1&&endDate.compareTo(socialInfo.getSocialSecurityEndDate())==-1)){
                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM");
                    return ResultVO.error(simple.format(socialSecurityDate) +" 到 " + simple.format(endDate) +"已有添加社保，请重新选择社保代缴开始日期");
                }
            }
        }
        return null;
    }

    @Override
    public List<SocialSecurityInfoPojo> getAllSecurityByOrderInfoId(int orderInfoId) {
        UserVO userVO = ThreadLocalUtils.getUser();
        List<SocialSecurityInfoPojo> listSocialSecurity = socialSecurityInfoMapper.getAllSecurityByOrderInfoId(userVO.getUserCode(),orderInfoId);
        setListSocialSecurity(listSocialSecurity);
        return listSocialSecurity;
    }

    @Override
    public List<SocialSecurityInfoPojo> getSecuritInfos() {
        return socialSecurityInfoMapper.getSecuritInfos();
    }

    @Override
    public void exportSecurityInfo(HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();
        List<SocialSecurityInfoPojo> listSecuritys = getSecuritInfos();
        setListToJson(listSecuritys,jsonArray);
        ExportUtils.exportExcel(jsonArray,response,"tab_security_info",System.currentTimeMillis()+".xlsx");
    }

    @Override
    public JSONArray getListSocialSecurity() {
        JSONArray jsonArray = new JSONArray();
        List<SocialSecurityInfoPojo> listSecuritys = getSecuritInfos();
        setListToJson(listSecuritys,jsonArray);
        return jsonArray;
    }

    private void setListToJson(List<SocialSecurityInfoPojo> listSecuritys,JSONArray jsonArray){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(EmptyUtils.isNotEmpty(listSecuritys)){
            for(SocialSecurityInfoPojo securityInfoPojo :listSecuritys){
                JSONObject jsonObject = new JSONObject();
                SocialStandardPojo socialStandardPojo=securityInfoPojo.getSocialStandardPojo();
                jsonObject.put("applyName",securityInfoPojo.getApplyName());
                jsonObject.put("phone",securityInfoPojo.getPhone());
                jsonObject.put("city",socialStandardPojo.getCity());
                jsonObject.put("cardinality",securityInfoPojo.getCardinality()*0.01);
                jsonObject.put("feePackage",securityInfoPojo.getFeePackageIds());
                jsonObject.put("securityDate",simpleDateFormat.format(securityInfoPojo.getSocialSecurityDate()));
                jsonObject.put("securityPrice",securityInfoPojo.getSocialSecurityPrice()*0.01);
                jsonObject.put("createDate",simple.format(securityInfoPojo.getCreateDate()));
                jsonObject.put("countMoney",securityInfoPojo.getCountMoney()*0.01);
                if(securityInfoPojo.getPayStatus()==NumberEnum.ONE.getValue()){
                    jsonObject.put("payStatus","未支付");
                }else if (securityInfoPojo.getPayStatus()==NumberEnum.TWO.getValue()){
                    jsonObject.put("payStatus","已支付");
                }else{
                    jsonObject.put("payStatus",OrdinaryConstant.IS_BLACK);
                }
                jsonArray.add(jsonObject);
            }
        }

    }


}

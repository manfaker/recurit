package com.shenzhen.recurit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.dao.AdvertisementMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.AdvertisementPojo;
import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.service.AdvertisementService;
import com.shenzhen.recurit.service.DocumentService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.AdvertisementVO;
import com.shenzhen.recurit.vo.NewsVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Resource
    private AdvertisementMapper advertisementMapper;
    @Resource
    private DocumentService documentService;

    @Override
    public AdvertisementPojo addAdvertisement(AdvertisementVO advertisementVO) {
        setAdvertisementInfo(advertisementVO, true);
        advertisementMapper.addAdvertisement(advertisementVO);
        return getAdvertisementById(advertisementVO.getId());
    }

    private void setAdvertisementInfo(AdvertisementVO advertisementVO, boolean isCreate) {
        UserVO user = ThreadLocalUtils.getUser();
        if (isCreate) {
            advertisementVO.setStatus(NumberEnum.ONE.getValue());
            advertisementVO.setCreater(user.getUserName());
            advertisementVO.setCreateDate(new Date());
        }
        advertisementVO.setUpdater(user.getUserName());
        advertisementVO.setUpdateDate(new Date());
    }

    @Override
    public AdvertisementPojo updateAdvertisement(AdvertisementVO advertisementVO) {
        setAdvertisementInfo(advertisementVO, false);
        advertisementMapper.updateAdvertisement(advertisementVO);
        return getAdvertisementById(advertisementVO.getId());
    }

    @Override
    @Transactional
    public int batchDeleteAdvertisement(List<Integer> idList) {
        List<AdvertisementPojo> advertisementByIds = getAdvertisementByIds(idList);
        List<Integer> documentIdList = new ArrayList<>();
        advertisementByIds.stream().forEach(advertisementPojo -> {
            DocumentPojo advertisementImage = advertisementPojo.getAdvertisementImage();
            if (EmptyUtils.isNotEmpty(advertisementImage)) {
                documentIdList.add(advertisementImage.getId());
            }
        });
        documentService.batchDeleteDocument(idList, true);
        return advertisementMapper.batchDeleteAdvertisement(idList);
    }

    @Override
    public PageInfo<AdvertisementPojo> getAdvertisementsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<AdvertisementPojo> advertisementList = advertisementMapper.getAllAdvertisements();
        PageInfo<AdvertisementPojo> pageInfo = new PageInfo<>(advertisementList);
        return pageInfo;
    }

    @Override
    public List<AdvertisementPojo> getAdvertisementByIds(List<Integer> idList) {
        return advertisementMapper.getAdvertisementByIds(idList);
    }

    @Override
    public AdvertisementPojo getAdvertisementById(Integer id) {
        return advertisementMapper.getAdvertisementById(id);
    }

}

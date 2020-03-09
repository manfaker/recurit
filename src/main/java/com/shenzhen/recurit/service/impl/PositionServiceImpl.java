package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.PositionMapper;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.vo.PositionVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionMapper positionMapper;

    @Override
    public List<PositionVO> getAllPosition() {
        return positionMapper.getAllPosition();
    }

    @Override
    public List<PositionVO> getAllPositionByCondition(PositionVO position) {
        return positionMapper.getAllPositionByCondition(position);
    }

    @Override
    public void savePosition(PositionVO position) {
        positionMapper.savePosition(position);
    }

    @Override
    public List<PositionVO> getByCompanyId(String companyId) {
        return positionMapper.getByCompanyId(companyId);
    }

    @Override
    public int updatePosition(PositionVO position) {
        return positionMapper.updatePosition(position);
    }
}

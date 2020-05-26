package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.DocumentMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.service.DocumentService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.DocumentVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DocumentServiceImpl implements DocumentService {

        @Resource
        private DocumentMapper documentMapper;

        @Override
        public DocumentPojo saveDocument(DocumentVO documentVO) {
            setDocumentInfo(documentVO,true);
            documentMapper.saveDocument(documentVO);
            return documentMapper.getDocument(documentVO.getId(), NumberEnum.ONE.getValue());
        }

        private void setDocumentInfo(DocumentVO documentVO,boolean flag){
            UserVO user = ThreadLocalUtils.getUser();
            if(flag){
                documentVO.setCreateDate(new Date());
                documentVO.setCreater(user.getUserName());
            }
            documentVO.setUpdateDate(new Date());
            documentVO.setUpdater(user.getUserName());
        }

        @Override
        public int updateDocument(DocumentVO documentVO) {
            setDocumentInfo(documentVO,false);
            return documentMapper.updateDocument(documentVO);
        }

        @Override
        public int deleteDocument(int id) {
            return documentMapper.deleteDocument(id);
        }

        @Override
        public DocumentPojo getDocument(int id, int status) {
            return documentMapper.getDocument(id,status);
        }
}

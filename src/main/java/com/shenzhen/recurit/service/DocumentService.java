package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.vo.DocumentVO;

public interface DocumentService {

    DocumentPojo saveDocument(DocumentVO documentVO);

    int updateDocument(DocumentVO documentVO);

    int deleteDocument(int id);

    DocumentPojo getDocument(int id,int status);

}

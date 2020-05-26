package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.vo.DocumentVO;
import org.apache.ibatis.annotations.Param;

public interface DocumentMapper {
    
    void saveDocument(DocumentVO documentVO);

    int updateDocument(DocumentVO documentVO);

    int deleteDocument(int id);

    DocumentPojo getDocument(@Param("id") int id,@Param("status") int status);
}

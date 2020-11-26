package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.vo.DocumentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentMapper {
    
    void saveDocument(DocumentVO documentVO);

    int updateDocument(DocumentVO documentVO);

    int deleteDocument(int id);

    DocumentPojo getDocument(@Param("id") int id,@Param("status") int status);

    /**
     * 批量删除文件
     *
     * @param idList
     * @return
     */
    int batchDeleteDocument(@Param("idList") List<Integer> idList);

    /**
     * 根据id批量获取文件信息
     *
     * @param idList
     * @return
     */
    List<DocumentPojo> getDocumentsByIds(@Param("idList") List<Integer> idList);
}

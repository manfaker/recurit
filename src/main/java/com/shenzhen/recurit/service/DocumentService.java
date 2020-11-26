package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.vo.DocumentVO;

import java.util.List;

public interface DocumentService {

    DocumentPojo saveDocument(DocumentVO documentVO);

    int updateDocument(DocumentVO documentVO);

    int deleteDocument(int id, boolean isImage);

    DocumentPojo getDocument(int id,int status);

    /**
     * 批量删除文件
     *
     * @param idList
     * @param isImage
     * @return
     */
    int batchDeleteDocument(List<Integer> idList, boolean isImage);

    /**
     * 根据id批量获取文件信息
     *
     * @param idList
     * @return
     */
    List<DocumentPojo> getDocumentsByIds(List<Integer> idList);


}

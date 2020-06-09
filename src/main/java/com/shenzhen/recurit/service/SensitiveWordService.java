package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.SensitiveWordVO;

import java.util.List;

public interface SensitiveWordService {

    /**
     * 批量导出敏感字符
     * @param listSensitiveWord
     */
    void importSensitiveWord(List<String> listSensitiveWord);

    /**
     * 批量保存敏感字符
     * @param listSensitiveWord
     */
    void saveBatchSensitiveWord(List<SensitiveWordVO>  listSensitiveWord);

    /**
     * 检查敏感字符
     *
     * @param jsonData
     * @return
     */
    ResultVO checkSensitiveWord(String jsonData);
}

package com.lvmama.tnt.bms.api.service;

import com.lvmama.tnt.bms.api.domain.NewsTypeDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;

import java.util.List;

/**
 * 消息类型接口
 */
public interface INewsTypeService {

    /**
     * 验证是否存在
     * @param typeName
     * @return
     */
    ResponseDTO<Boolean> existTypeName(String typeName);

    /**
     * 新增消息类型
     * @param newsTypeDTO
     * @return
     */
    ResponseDTO<Integer> saveNewsType(NewsTypeDTO newsTypeDTO);

    /**
     * 修改消息类型
     * @param newsTypeDTO
     * @return
     */
    ResponseDTO<Integer> updateNewsType(NewsTypeDTO newsTypeDTO);

    /**
     * 删除消息类型
     * @param typeName
     * @return
     */
    ResponseDTO<Integer> deleteByType(String typeName);

    /**
     *
     * @param typeName
     * @return
     */
    ResponseDTO<List<NewsTypeDTO>> findNewsTypeByName(String typeName);

    /**
     * 所有消息类型
     * @return
     */
    ResponseDTO<List<NewsTypeDTO>> findAllNewsType();

}

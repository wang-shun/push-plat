package com.lvmama.tnt.bms.api.service;

import com.lvmama.tnt.bms.api.domain.ConvertDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;

import java.util.List;

/**
 *  映射器
 */
public interface IConvertService {

    /**
     * 验证是否已存在
     * @param name
     * @return
     */
    ResponseDTO<Boolean> existName(String name);

    /**
     * 新增映射器
     * @param convertDTO
     * @return
     */
    ResponseDTO<Integer> saveConvert(ConvertDTO convertDTO);

    /**
     * 修改映射器
     * @param convertDTO
     * @returnC
     */
    ResponseDTO<Integer> updateConvert(ConvertDTO convertDTO);

    /**
     * 删除映射器
     * @param name
     * @return
     */
    ResponseDTO<Integer> deleteByName(String name);

    /**
     *
     * @param name
     * @return
     */
    ResponseDTO<List<ConvertDTO>> findConvertByName(String name);

    /**
     * 所有消息类型
     * @return
     */
    ResponseDTO<List<ConvertDTO>> findAllConvert();
}

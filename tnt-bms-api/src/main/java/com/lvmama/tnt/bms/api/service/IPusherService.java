package com.lvmama.tnt.bms.api.service;

import com.lvmama.tnt.bms.api.domain.NewsPusherDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;

import java.util.List;

/**
 * 推送器接口
 */
public interface IPusherService {

    /**
     * 验证是否存在
     * @param name
     * @return
     */
    ResponseDTO<Boolean> existName(String name);

    /**
     * 新增推送器
     * @param pusherDTO
     * @return
     */
    ResponseDTO<Integer> savePusher(NewsPusherDTO pusherDTO);

    /**
     * 修改推送器
     * @param pusherDTO
     * @return
     */
    ResponseDTO<Integer> updatePusher(NewsPusherDTO pusherDTO);

    /**
     * 删除推送器
     * @param name
     * @return
     */
    ResponseDTO<Integer> deleteByName(String name);

    /**
     *
     * @param name
     * @param includeJar 是否包含jar文件
     * @return
     */
    ResponseDTO<List<NewsPusherDTO>> findByName(String name, boolean includeJar);

    /**
     *
     * @return
     */
    ResponseDTO<List<NewsPusherDTO>> findAllPusher();

}

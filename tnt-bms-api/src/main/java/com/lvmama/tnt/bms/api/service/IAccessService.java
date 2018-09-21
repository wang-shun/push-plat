package com.lvmama.tnt.bms.api.service;

import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;

import java.util.List;

/**
 * 接入接口
 */
public interface IAccessService {

    /**
     * 如果存在则返回，没有则创建
     * @param newsAccessDTO
     * @return
     */
    ResponseDTO<NewsAccessDTO> createIfAbsentByName(NewsAccessDTO newsAccessDTO);

    /**
     * 验证是否存在
     * @param groupToken
     * @param name
     * @return
     */
    ResponseDTO<Boolean> existName(String groupToken, String name);

    /**
     * 新增接入
     * @param newsAccessDTO
     * @return token
     */
    ResponseDTO<String> saveAccess(NewsAccessDTO newsAccessDTO);

    /**
     * 修改接入
     * @param newsAccessDTO
     * @return 修改条数
     */
    ResponseDTO<Integer> updateAccess(NewsAccessDTO newsAccessDTO);

    /**
     * 查找接入信息，一个组也是一个接入，传入组token查询即组信息
     * @param token
     * @return
     */
    ResponseDTO<NewsAccessDTO> findByToken(String token);

    /**
     *
     * @param keyword
     * @return
     */
    ResponseDTO<List<NewsAccessDTO>> fuzzyByName(String keyword);

    /**
     * 查找接入组成员信息
     * @param groupToken
     * @return
     */
    ResponseDTO<List<NewsAccessDTO>> findByGroupToken(String groupToken);

    /**
     * 批量查询
     * @param tokens
     * @return
     */
    ResponseDTO<List<NewsAccessDTO>> findByTokenBatch(List<String> tokens);

    /**
     * 删除接入信息，传入组token，删除的就是组及组内所有成员
     * @param token
     * @return
     */
    ResponseDTO<Integer> deleteByToken(String token);
}

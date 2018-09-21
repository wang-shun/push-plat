package com.lvmama.tnt.bms.admin.web.manage.service;

import com.lvmama.tnt.bms.admin.web.manage.domain.RolePO;

import java.util.List;

/**
 *
 */
public interface IRoleService {

    int save(RolePO rolePO);

    int update(RolePO rolePO);

    int deleteById(Integer id);

    RolePO findById(Integer id);

    List<RolePO> findByParams(RolePO rolePO, int pageNo, int pageSize);

    List<RolePO> findAllRoles();

    List<Integer> findUserRoles(Integer userId);

}

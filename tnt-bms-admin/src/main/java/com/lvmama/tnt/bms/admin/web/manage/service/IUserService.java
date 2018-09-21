package com.lvmama.tnt.bms.admin.web.manage.service;

import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;

import java.util.List;
import java.util.Set;

/**
 *
 */
public interface IUserService {

    int save(UserPO userPO);

    int update(UserPO userPO);

    int deleteByUserName(String userName);

    int deleteById(Integer id);

    UserPO findByUserName(String userName);

    Set<ResourceDTO> findResourceByRoles(Set<String> roleCodes);

    UserPO findById(Integer id);

    List<UserPO> findByParams(UserPO userPO, int pageNo, int pageSize);

    void distributeRole(Integer userId, List<Integer> roleIds);
}

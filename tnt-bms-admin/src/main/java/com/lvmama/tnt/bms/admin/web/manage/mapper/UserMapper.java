package com.lvmama.tnt.bms.admin.web.manage.mapper;

import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserRolePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Repository
public interface UserMapper {

    int save(UserPO userPO);

    int update(UserPO userPO);

    UserPO findByUserName(String userName);

    Set<ResourceDTO> findResourceByRoles(@Param("roleCodes") Set<String> roleCodes);

    UserPO findById(Integer id);

    List<UserPO> findByParams(UserPO userPO);

    void deleteUserRole(Integer userId);

    void saveUserRoleBatch(List<UserRolePO> list);
}

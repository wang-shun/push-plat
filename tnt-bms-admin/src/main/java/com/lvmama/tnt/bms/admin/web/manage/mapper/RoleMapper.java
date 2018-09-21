package com.lvmama.tnt.bms.admin.web.manage.mapper;

import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RolePO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Repository
public interface RoleMapper {

    int save(RolePO rolePO);

    int update(RolePO rolePO);

    RolePO findById(Integer id);

    List<RolePO> findByParams(RolePO rolePO);

    List<RolePO> findAllRoles();

    List<Integer> findUserRoles(Integer userId);
}

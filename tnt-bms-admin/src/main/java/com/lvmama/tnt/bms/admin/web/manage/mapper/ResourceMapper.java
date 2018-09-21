package com.lvmama.tnt.bms.admin.web.manage.mapper;

import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceNode;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourcePO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RoleResourcePO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Repository
public interface ResourceMapper {

    int save(ResourcePO resourcePO);

    int update(ResourcePO resourcePO);

    ResourcePO findById(Integer id);

    List<ResourcePO> findByParams(ResourcePO resourcePO);

    List<ResourceNode> loadDistributeStore(Map<String, Object> param);

    void deleteRoleResByRoleId(Integer roleId);

    void saveRoleResourceBatch(List<RoleResourcePO> list);
}

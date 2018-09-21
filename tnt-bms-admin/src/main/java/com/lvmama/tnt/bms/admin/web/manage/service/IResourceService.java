package com.lvmama.tnt.bms.admin.web.manage.service;

import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceNode;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourcePO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RoleResourceDTO;

import java.util.List;

/**
 *
 */
public interface IResourceService {

    int save(ResourcePO resourcePO);

    int update(ResourcePO resourcePO);

    int deleteById(Integer id);

    ResourcePO findById(Integer id);

    List<ResourcePO> findByParams(ResourcePO resourcePO, int pageNo, int pageSize);

    List<ResourcePO> findByTitleFuzzy(String title);

    List<ResourceNode> loadDistributeStore();

    List<ResourceNode> loadDistributeByRole(Integer roleId);

    void saveRoleResource(RoleResourceDTO roleResourceDTO);
}

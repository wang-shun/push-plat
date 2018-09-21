package com.lvmama.tnt.bms.admin.web.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.lvmama.tnt.bms.admin.web.manage.constant.Constants;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceNode;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourcePO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RoleResourceDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RoleResourcePO;
import com.lvmama.tnt.bms.admin.web.manage.mapper.ResourceMapper;
import com.lvmama.tnt.bms.admin.web.manage.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 */
@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public int save(ResourcePO resourcePO) {
        if (resourcePO == null) {
            throw new IllegalArgumentException("save resource undo, resourcePO is empty");
        }
        Date date = new Date();
        resourcePO.setActive(com.lvmama.tnt.bms.admin.web.manage.constant.Constants.ACTIVE);
        resourcePO.setCreateTime(date);
        resourcePO.setModifyTime(date);
        return resourceMapper.save(resourcePO);
    }

    @Override
    public int update(ResourcePO resourcePO) {
        if (resourcePO == null) {
            throw new IllegalArgumentException("update resource undo, resourcePO is empty");
        }
        Date date = new Date();
        resourcePO.setModifyTime(date);
        return resourceMapper.update(resourcePO);
    }

    @Override
    public int deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("deleteById undo, id is empty");
        }
        ResourcePO resourcePO = new ResourcePO();
        resourcePO.setId(id);
        resourcePO.setActive("N");
        resourcePO.setModifyTime(new Date());
        return resourceMapper.update(resourcePO);
    }

    @Override
    public ResourcePO findById(Integer id) {
        return resourceMapper.findById(id);
    }

    @Override
    public List<ResourcePO> findByParams(ResourcePO resourcePO, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return resourceMapper.findByParams(resourcePO);
    }

    @Override
    public List<ResourcePO> findByTitleFuzzy(String title) {
        ResourcePO po = new ResourcePO();
        po.setTitle(title);
        return resourceMapper.findByParams(po);
    }

    @Override
    public List<ResourceNode> loadDistributeStore() {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", null);
        params.put("parentCode", Constants.ROOT);
        return resourceMapper.loadDistributeStore(params);
    }

    @Override
    public List<ResourceNode> loadDistributeByRole(Integer roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("parentCode", Constants.ROOT);
        return resourceMapper.loadDistributeStore(params);
    }

    @Override
    @Transactional
    public void saveRoleResource(RoleResourceDTO roleResourceDTO) {
        if (roleResourceDTO != null) {
            resourceMapper.deleteRoleResByRoleId(roleResourceDTO.getRoleId());
            List<RoleResourcePO> list = new ArrayList<>(roleResourceDTO.getResourceIds().size());
            Integer roleId = roleResourceDTO.getRoleId();
            RoleResourcePO po = null;
            StringBuilder permission = null;
            for (Integer resourceId : roleResourceDTO.getResourceIds()) {
                permission = new StringBuilder();
                for (String p : roleResourceDTO.getPermissions()) {
                    if (p.startsWith(String.valueOf(resourceId))) {
                        String[] arr = p.split("_");
                        if (arr.length == 2) {
                            permission.append(arr[1]).append(",");
                        }
                    }
                }
                po = new RoleResourcePO(roleId, resourceId, permission.toString());
                list.add(po);
            }
            resourceMapper.saveRoleResourceBatch(list);
        }
    }
}

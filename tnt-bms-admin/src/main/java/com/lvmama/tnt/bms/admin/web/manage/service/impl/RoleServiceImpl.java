package com.lvmama.tnt.bms.admin.web.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.lvmama.tnt.bms.admin.web.manage.domain.RolePO;
import com.lvmama.tnt.bms.admin.web.manage.mapper.RoleMapper;
import com.lvmama.tnt.bms.admin.web.manage.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public int save(RolePO rolePO) {
        if (rolePO == null) {
            throw new IllegalArgumentException("save role undo, rolePO is empty");
        }
        Date date = new Date();
        rolePO.setActive(com.lvmama.tnt.bms.admin.web.manage.constant.Constants.ACTIVE);
        rolePO.setCreateTime(date);
        rolePO.setModifyTime(date);
        return roleMapper.save(rolePO);
    }

    @Override
    public int update(RolePO rolePO) {
        if (rolePO == null) {
            throw new IllegalArgumentException("update role undo, rolePO is empty");
        }
        Date date = new Date();
        rolePO.setModifyTime(date);
        return roleMapper.update(rolePO);
    }

    @Override
    public int deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("deleteById undo, id is empty");
        }
        RolePO rolePO = new RolePO();
        rolePO.setId(id);
        rolePO.setActive("N");
        rolePO.setModifyTime(new Date());
        return roleMapper.update(rolePO);
    }

    @Override
    public RolePO findById(Integer id) {
        return roleMapper.findById(id);
    }

    @Override
    public List<RolePO> findByParams(RolePO rolePO, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return roleMapper.findByParams(rolePO);
    }

    @Override
    public List<RolePO> findAllRoles() {
        return roleMapper.findAllRoles();
    }

    @Override
    public List<Integer> findUserRoles(Integer userId) {
        return roleMapper.findUserRoles(userId);
    }
}

package com.lvmama.tnt.bms.admin.web.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserRolePO;
import com.lvmama.tnt.bms.admin.web.manage.mapper.UserMapper;
import com.lvmama.tnt.bms.admin.web.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public int save(UserPO userPO) {
        if (userPO == null) {
            throw new IllegalArgumentException("save user undo, userPO is empty");
        }
        Date date = new Date();
        userPO.setActive(com.lvmama.tnt.bms.admin.web.manage.constant.Constants.ACTIVE);
        userPO.setCreateTime(date);
        userPO.setModifyTime(date);
        return userMapper.save(userPO);
    }

    @Override
    public int update(UserPO userPO) {
        if (userPO == null) {
            throw new IllegalArgumentException("update user undo, userPO is empty");
        }
        Date date = new Date();
        userPO.setModifyTime(date);
        return userMapper.update(userPO);
    }

    @Override
    public int deleteByUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("deleteByUserName undo, userName is empty");
        }
        UserPO userPO = new UserPO();
        userPO.setUserName(userName);
        userPO.setActive("N");
        userPO.setModifyTime(new Date());
        return userMapper.update(userPO);
    }

    @Override
    public int deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("deleteById undo, id is empty");
        }
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setActive("N");
        userPO.setModifyTime(new Date());
        return userMapper.update(userPO);
    }

    @Override
    public UserPO findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    @Override
    public Set<ResourceDTO> findResourceByRoles(Set<String> roleCodes) {
        return userMapper.findResourceByRoles(roleCodes);
    }

    @Override
    public UserPO findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public List<UserPO> findByParams(UserPO userPO, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return userMapper.findByParams(userPO);
    }

    @Override
    @Transactional
    public void distributeRole(Integer userId, List<Integer> roleIds) {
        if (userId != null && roleIds != null && roleIds.size() > 0) {
            userMapper.deleteUserRole(userId);
            List<UserRolePO> pos = new ArrayList<>(roleIds.size());
            UserRolePO po = null;
            for (Integer id : roleIds) {
                po = new UserRolePO(userId, id);
                pos.add(po);
            }
            userMapper.saveUserRoleBatch(pos);
        }
    }
}

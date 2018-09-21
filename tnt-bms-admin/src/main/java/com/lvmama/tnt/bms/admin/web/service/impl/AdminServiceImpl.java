package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.tnt.bms.admin.web.domain.po.AdminDO;
import com.lvmama.tnt.bms.admin.web.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * @author longhr
 * @version 2017/10/31 0031
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public AdminDO findByNameAndPassword(AdminDO admin) {
        //TODO
        return admin;
    }
}

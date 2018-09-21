package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.AdminDO;

/**
 * @author longhr
 * @version 2017/10/31 0031
 */
public interface AdminService {

    AdminDO findByNameAndPassword(AdminDO admin);
}

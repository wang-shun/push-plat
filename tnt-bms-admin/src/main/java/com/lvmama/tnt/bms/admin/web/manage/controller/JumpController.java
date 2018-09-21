package com.lvmama.tnt.bms.admin.web.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
public class JumpController {

    @RequestMapping("/manage/user_list")
    public String userList() {
        return "manage/user_list";
    }

    @RequestMapping("/manage/role_list")
    public String roleList() {
        return "manage/role_list";
    }

    @RequestMapping("/manage/resource_list")
    public String resourceList() {
        return "manage/resource_list";
    }

    @RequestMapping("/manage/change_password")
    public String changePassword() {
        return "manage/change_password";
    }
}

package com.lvmama.tnt.bms.admin.web.manage.controller;

import com.github.pagehelper.PageInfo;
import com.lvmama.bms.core.commons.utils.Md5Encrypt;
import com.lvmama.tnt.bms.admin.web.manage.constant.Constants;
import com.lvmama.tnt.bms.admin.web.manage.domain.RequestDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.UserPO;
import com.lvmama.tnt.bms.admin.web.manage.service.IRoleService;
import com.lvmama.tnt.bms.admin.web.manage.service.IUserService;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
public class UserController extends AbstractController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/manage/user/list")
    public ResponseDTO<List<UserPO>> list(@RequestBody RequestDTO<UserPO> requestDTO) {
        ResponseDTO responseDTO = returnSuccess();
        List<UserPO> userPOS = userService.findByParams(requestDTO.getRequest(), requestDTO.getPageNo(), requestDTO.getPageSize());
        responseDTO.setResult(userPOS);
        responseDTO.setTotalCount(new PageInfo(userPOS).getTotal());
        return responseDTO;
    }

    @RequestMapping("/manage/user/findById")
    public ResponseDTO<UserPO> findById(Integer userId) {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(userService.findById(userId));
        return responseDTO;
    }

    @RequestMapping("/manage/user/resetPassword")
    public ResponseDTO<Void> resetPassword(Integer userId) {
        ResponseDTO responseDTO = returnSuccess();
        UserPO userPO = new UserPO();
        userPO.setId(userId);
        userPO.setPassword(Md5Encrypt.md5(Constants.DEFAULT_PASSWORD));
        userService.update(userPO);
        return responseDTO;
    }

    @RequestMapping("/manage/user/deleteById")
    public ResponseDTO<Void> deleteById(Integer userId) {
        ResponseDTO responseDTO = returnSuccess();
        userService.deleteById(userId);
        return responseDTO;
    }

    @RequestMapping("/manage/user/saveOrUpdate")
    public ResponseDTO<String> saveOrUpdate(@RequestBody UserPO userPO) {
        ResponseDTO responseDTO = returnSuccess();
        if (userPO.getId() == null) {
            userPO.setPassword(Md5Encrypt.md5(Constants.DEFAULT_PASSWORD));
            userService.save(userPO);
        } else {
            userService.update(userPO);
        }
        return responseDTO;
    }

    @RequestMapping("/manage/user/findRoles")
    public ResponseDTO<Map<String, Object>> findRoles(Integer userId) {
        ResponseDTO responseDTO = returnSuccess();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("roles", roleService.findAllRoles());
        result.put("userRoles", roleService.findUserRoles(userId));
        responseDTO.setResult(result);
        return responseDTO;
    }

    @RequestMapping("/manage/user/distributeRole")
    public ResponseDTO<Void> distributeRole(@RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = returnSuccess();
        userService.distributeRole(userDTO.getId(), userDTO.getRoleIds());
        return responseDTO;
    }
}

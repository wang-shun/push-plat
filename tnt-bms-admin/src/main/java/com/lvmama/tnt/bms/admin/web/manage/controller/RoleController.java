package com.lvmama.tnt.bms.admin.web.manage.controller;

import com.github.pagehelper.PageInfo;
import com.lvmama.tnt.bms.admin.web.manage.domain.RequestDTO;
import com.lvmama.tnt.bms.admin.web.manage.domain.RolePO;
import com.lvmama.tnt.bms.admin.web.manage.service.IRoleService;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
public class RoleController extends AbstractController{

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/manage/role/list")
    public ResponseDTO<List<RolePO>> list(@RequestBody RequestDTO<RolePO> requestDTO) {
        ResponseDTO responseDTO = returnSuccess();
        List<RolePO> userPOS = roleService.findByParams(requestDTO.getRequest(), requestDTO.getPageNo(), requestDTO.getPageSize());
        responseDTO.setResult(userPOS);
        responseDTO.setTotalCount(new PageInfo(userPOS).getTotal());
        return responseDTO;
    }

    @RequestMapping("/manage/role/findById")
    public ResponseDTO<RolePO> findById(Integer id) {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(roleService.findById(id));
        return responseDTO;
    }

    @RequestMapping("/manage/role/deleteById")
    public ResponseDTO<Void> deleteById(Integer id) {
        ResponseDTO responseDTO = returnSuccess();
        roleService.deleteById(id);
        return responseDTO;
    }

    @RequestMapping("/manage/role/saveOrUpdate")
    public ResponseDTO<String> saveOrUpdate(@RequestBody RolePO rolePO) {
        ResponseDTO responseDTO = returnSuccess();
        if (rolePO.getId() == null) {
            roleService.save(rolePO);
        } else {
            roleService.update(rolePO);
        }
        return responseDTO;
    }
}

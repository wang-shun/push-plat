package com.lvmama.tnt.bms.admin.web.manage.controller;

import com.github.pagehelper.PageInfo;
import com.lvmama.tnt.bms.admin.web.manage.domain.*;
import com.lvmama.tnt.bms.admin.web.manage.service.IResourceService;
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
public class ResourceController extends AbstractController {

    @Autowired
    private IResourceService resourceService;

    @RequestMapping("/manage/resource/list")
    public ResponseDTO<List<ResourcePO>> list(@RequestBody RequestDTO<ResourcePO> requestDTO) {
        ResponseDTO responseDTO = returnSuccess();
        List<ResourcePO> userPOS = resourceService.findByParams(requestDTO.getRequest(), requestDTO.getPageNo(), requestDTO.getPageSize());
        responseDTO.setResult(userPOS);
        responseDTO.setTotalCount(new PageInfo(userPOS).getTotal());
        return responseDTO;
    }

    @RequestMapping("/manage/resource/findById")
    public ResponseDTO<ResourcePO> findById(Integer id) {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(resourceService.findById(id));
        return responseDTO;
    }

    @RequestMapping("/manage/resource/deleteById")
    public ResponseDTO<Void> deleteById(Integer id) {
        ResponseDTO responseDTO = returnSuccess();
        resourceService.deleteById(id);
        return responseDTO;
    }

    @RequestMapping("/manage/resource/saveOrUpdate")
    public ResponseDTO<String> saveOrUpdate(@RequestBody ResourcePO resourcePO) {
        ResponseDTO responseDTO = returnSuccess();
        if (resourcePO.getId() == null) {
            resourceService.save(resourcePO);
        } else {
            resourceService.update(resourcePO);
        }
        return responseDTO;
    }

    @RequestMapping("/manage/resource/findByTitle")
    public ResponseDTO<String> findByTitle(String title) {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(resourceService.findByTitleFuzzy(title));
        return responseDTO;
    }

    @RequestMapping("/manage/resource/loadDistributeStore")
    public ResponseDTO<List<ResourceNode>> loadDistributeStore() {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(resourceService.loadDistributeStore());
        return responseDTO;
    }

    @RequestMapping("/manage/resource/loadDistributeByRole")
    public ResponseDTO<List<ResourceNode>> loadDistributeByRole(Integer roleId) {
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(resourceService.loadDistributeByRole(roleId));
        return responseDTO;
    }

    @RequestMapping("/manage/resource/saveRoleResource")
    public ResponseDTO<String> saveRoleResource(@RequestBody RoleResourceDTO roleResourceDTO) {
        ResponseDTO responseDTO = returnSuccess();
        resourceService.saveRoleResource(roleResourceDTO);
        return responseDTO;
    }

}

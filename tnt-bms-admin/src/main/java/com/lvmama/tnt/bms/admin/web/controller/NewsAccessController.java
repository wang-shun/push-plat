package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.bms.core.domain.enums.ProtocolType;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.ConvertManageService;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.service.PusherManagerService;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
@Controller
public class NewsAccessController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(NewsAccessController.class);

    @Autowired
    private NewsAccessService newsAccessService;
    @Autowired
    private PusherManagerService pusherManagerService;
    @Autowired
    private ConvertManageService convertManageService;

    @ResponseBody
    @RequestMapping("/config/access/reloadCache")
    public ResponseVO reloadCache() {
        ResponseVO responseVO = returnSuccess();
        try {
            newsAccessService.reloadCache();
            responseVO.setResult("success");
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }

    @RequestMapping("/config/access/list")
    public String list(Model model, NewsAccessDO newsAccessDO, HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = StringUtils.isNotEmpty(pageNoStr) ? Integer.parseInt(pageNoStr) : 1;
        List<NewsAccessDO> list = newsAccessService.findByPage(newsAccessDO, pageNo, Constants.PAGE_SIZE);
        long totalCount = newsAccessService.totalCount(newsAccessDO);

        PageResultVO<List<NewsAccessDO>> resultVo = new PageResultVO<List<NewsAccessDO>>();
        resultVo.setPageNo(pageNo);
        resultVo.setTotalCount(totalCount);
        resultVo.setTotalPage(PageUtil.calculateTotalPage(totalCount,Constants.PAGE_SIZE));
        resultVo.setResult(list);
        String mode = newsAccessDO.getMode();
        if (StringUtils.isEmpty(mode)) {
            mode = "group";
        }
        model.addAttribute("mode", mode);
        model.addAttribute("groupToken", newsAccessDO.getGroupToken());
        model.addAttribute("resultVo", resultVo);
        model.addAttribute("pageinationd", PageUtil.getPageContent(request, resultVo));
        return "news/news_access_list";
    }

    @ResponseBody
    @RequestMapping(value = "/config/access/findByGroupID/{groupID}", method = RequestMethod.GET)
    public ResponseVO findAccessByGroupID(@PathVariable("groupID") Long groupID) {
        ResponseVO responseVO = returnSuccess();
        try {
            responseVO.setResult(newsAccessService.findByGroupID(groupID));
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }

    @RequestMapping("/config/access/toAdd_{mode}")
    public String newsTypeToAdd(Model model, HttpServletRequest request,@PathVariable("mode") String mode) {
        List<NewsAccessDO> groups = newsAccessService.findGroupsForChoose();
        List<MsgPusherDO> pusherDOS = pusherManagerService.findPushers();
        List<ConvertDO> convertDOS = convertManageService.findAll();
        model.addAttribute("convertDOS",convertDOS);
        model.addAttribute("pusherDOS", pusherDOS);
        model.addAttribute("groups", groups);
        model.addAttribute("mode", mode);
        return "news/news_access_add";
    }

    @RequestMapping("/config/access/toEdit/{mode}/{id}")
    public String newsTypeToEidt(Model model,@PathVariable("mode") String mode, @PathVariable("id") Long id) {
        List<MsgPusherDO> pusherDOS = pusherManagerService.findPushers();
        List<ConvertDO> convertDOS = convertManageService.findAll();
        model.addAttribute("convertDOS",convertDOS);
        model.addAttribute("pusherDOS", pusherDOS);
        model.addAttribute("newsAccessDO", newsAccessService.findById(id));
        model.addAttribute("mode", mode);
        return "news/news_access_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/config/access/addN", method = RequestMethod.POST)
    public ResponseVO newsTypeAdd(@RequestBody NewsAccessDO newsAccessDO) {
        String token = null;
        try {
            if (!"group".equals(newsAccessDO.getMode())) {
                checkParams(newsAccessDO);
            }
            newsAccessService.insert(newsAccessDO);
            token = newsAccessDO.getToken();
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("接入成功", token);
    }

    @ResponseBody
    @RequestMapping(value = "/config/access/editN", method = RequestMethod.POST)
    public ResponseVO newsTypeEdit(@RequestBody NewsAccessDO newsAccessDO) {
        try {
            newsAccessService.update(newsAccessDO);
        } catch (BusinessException e) {
            logger.warn(e.getMessage(),e);
            return returnFailed(e.getMessage());
        }
        return returnSuccess("修改成功");
    }

    @ResponseBody
    @RequestMapping("/config/access/deleteByToken/{token}")
    public ResponseVO deleteAccessByToken(Model model, @PathVariable("token") String token) {
        try {
            newsAccessService.deleteByToken(token);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping("/config/access/delete/{accessID}")
    public ResponseVO deleteNesType(Model model, @PathVariable("accessID") Long accessID) {
        try {
            newsAccessService.delete(accessID);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping("/config/access/removeGroup/{accessID}")
    public ResponseVO removeGroup(Model model, @PathVariable("accessID") Long accessID) {
        try {
            newsAccessService.removeGroup(accessID);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("移除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/config/access/findGroup/{token}", method = RequestMethod.GET)
    public ResponseVO findAccessGroupByToken(@PathVariable("token") String token) {
        ResponseVO responseVO = returnSuccess();
        try {
            responseVO.setResult(newsAccessService.findAccessGroupByToken(token));
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }

    private void checkParams(NewsAccessDO newsAccessDO) throws BusinessException {
        if (!isEmpty(newsAccessDO.getGroupToken())) {
            //有分组
            NewsAccessDO groupDO = newsAccessService.findAccessGroupByToken(newsAccessDO.getGroupToken());
            if (groupDO == null) {
                throw new BusinessException("接入失败，查无此分组！");
            }
            newsAccessDO.setGroupId(groupDO.getId());
            if (isEmpty(newsAccessDO.getName())) {
                throw new BusinessException("分销商名称不能为空！");
            }
            if (isEmpty(newsAccessDO.getPushUrl()) && ProtocolType.RPC.getValue() != newsAccessDO.getPushType()) {
                throw new BusinessException("推送地址不能为空！");
            }
            if (isEmpty(newsAccessDO.getReceiveType()+"") && isEmpty(groupDO.getReceiveType()+"")) {
                throw new BusinessException("分组内此设置为空，接收类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getPushType()+"") && isEmpty(groupDO.getReceiveType()+"")) {
                throw new BusinessException("分组内此设置为空，推送类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getConverterType()+"") && isEmpty(groupDO.getReceiveType()+"")) {
                throw new BusinessException("分组内此设置为空，转换类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getThreshold()+"") && isEmpty(groupDO.getReceiveType()+"")) {
                throw new BusinessException("分组内此设置为空，推送或拉取频率不能为空！");
            }
        } else {
            //默认分组
            newsAccessDO.setGroupId(Constants.DEFAULT_GROUP_ID);
            if (isEmpty(newsAccessDO.getName())) {
                throw new BusinessException("分销商名称不能为空！");
            }
            if (isEmpty(newsAccessDO.getPushUrl()) && ProtocolType.RPC.getValue() != newsAccessDO.getPushType()) {
                throw new BusinessException("推送地址不能为空！");
            }
            if (isEmpty(newsAccessDO.getReceiveType()+"")) {
                throw new BusinessException("接收类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getPushType()+"")) {
                throw new BusinessException("推送类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getConverterType()+"")) {
                throw new BusinessException("转换类型不能为空！");
            }
            if (isEmpty(newsAccessDO.getThreshold()+"")) {
                throw new BusinessException("推送频率不能为空！");
            }
        }

    }

    private boolean isEmpty(String cs) {
        return cs == null || cs.length() == 0 || "null".equals(cs);
    }

}

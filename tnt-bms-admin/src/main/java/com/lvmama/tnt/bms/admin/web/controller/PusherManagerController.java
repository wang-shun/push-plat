package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.PusherManagerService;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 推送器管理
 */
@Controller
public class PusherManagerController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(PusherManagerController.class);

    @Autowired
    private PusherManagerService pusherManagerService;

    @RequestMapping("/config/pusher/download_{id}")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        MsgPusherDO msgPusherDO = pusherManagerService.findByID(id,true);
        String fileName = msgPusherDO.getFileName();// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os.write(msgPusherDO.getJar());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @RequestMapping("/config/pusher/addPusher")
    public String addPusher(Model model, MsgPusherDO msgPusherDO, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            String fileName = null;
            if (file != null && msgPusherDO != null) {
                fileName = file.getOriginalFilename();
                msgPusherDO.setFileName(fileName);
                msgPusherDO.setJar(file.getBytes());
                if (msgPusherDO.getJar() != null) {
                    logger.info("upload file length is " + msgPusherDO.getJar().length);
                }
                if (msgPusherDO.getId() != null) {
                    pusherManagerService.update(msgPusherDO);
                } else {
                    pusherManagerService.insert(msgPusherDO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:list";
    }

    @RequestMapping("/config/pusher/toAdd_{pusherID}")
    public String toAdd(Model model,@PathVariable Integer pusherID) {
        MsgPusherDO msgPusherDO = null;
        if (pusherID != null && pusherID > 0) {
            msgPusherDO = pusherManagerService.findByID(pusherID, false);
        }
        if (msgPusherDO == null) {
            msgPusherDO = new MsgPusherDO();
        }
        model.addAttribute("pusherDO", msgPusherDO);
        return "news/pusher_manager_add";
    }

    @RequestMapping("/config/pusher/list")
    public String toPusher(Model model, MsgPusherDO msgPusherDO, HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = StringUtils.isNotEmpty(pageNoStr) ? Integer.parseInt(pageNoStr) : 1;
        List<MsgPusherDO> list = pusherManagerService.findPushersByPage(msgPusherDO, pageNo, Constants.PAGE_SIZE);
        long totalCount = pusherManagerService.totalCount(msgPusherDO);

        PageResultVO<List<MsgPusherDO>> resultVo = new PageResultVO<List<MsgPusherDO>>();
        resultVo.setPageNo(pageNo);
        resultVo.setTotalCount(totalCount);
        resultVo.setTotalPage(PageUtil.calculateTotalPage(totalCount,Constants.PAGE_SIZE));
        resultVo.setResult(list);
        model.addAttribute("resultVo", resultVo);
        model.addAttribute("pageinationd", PageUtil.getPageContent(request, resultVo));
        return "news/pusher_manager_list";
    }

    @ResponseBody
    @RequestMapping("/config/pusher/delete/{pusherID}")
    public ResponseVO deleteNesType(Model model, @PathVariable("pusherID") Integer pusherID) {
        try {
            pusherManagerService.delete(pusherID);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping("/config/pusher/check/{name}")
    public ResponseVO checkExist(Model model, @PathVariable String name) {
        if(pusherManagerService.checkExist(name)){
            return returnFailed("该映射器已注册");
        }
        return returnSuccess();
    }

}

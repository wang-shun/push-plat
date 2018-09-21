package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;
import com.lvmama.tnt.bms.admin.web.service.PusherManagerService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.api.domain.NewsPusherDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.IPusherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class RemoteNewsPusherServiceImpl extends AbstractRemoteService implements IPusherService {

    private static Logger logger = LoggerFactory.getLogger(RemoteNewsPusherServiceImpl.class);

    @Autowired
    private PusherManagerService pusherManagerService;

    @Override
    public ResponseDTO<Boolean> existName(String name) {
        logger.info("check pusher exist : {}", name);
        if (StringUtils.isEmpty(name)) {
            return returnFailed("参数不能为空");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(pusherManagerService.checkExist(name));
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> savePusher(NewsPusherDTO pusherDTO) {
        logger.info("remote add new pusher : {}", pusherDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (pusherDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            MsgPusherDO msgPusherDO = new MsgPusherDO();
            BeanCopyUtils.copyBean(pusherDTO, msgPusherDO);
            responseDTO.setResult(pusherManagerService.insert(msgPusherDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> updatePusher(NewsPusherDTO pusherDTO) {
        logger.info("remote update new pusher : {}", pusherDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (pusherDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            MsgPusherDO msgPusherDO = new MsgPusherDO();
            BeanCopyUtils.copyBean(pusherDTO, msgPusherDO);
            responseDTO.setResult(pusherManagerService.update(msgPusherDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> deleteByName(String name) {
        logger.info("remote delete pusher: {}", name);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(pusherManagerService.deleteByName(name));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsPusherDTO>> findByName(String name, boolean includeJar) {
        logger.info("remote find pusher: {}", name);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(pusherManagerService.findByName(name, includeJar)));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsPusherDTO>> findAllPusher() {
        logger.info("remote find all pusher");
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(pusherManagerService.findPushers()));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    private List<NewsPusherDTO> convert(List<MsgPusherDO> srcDOS) {
        if (srcDOS == null || srcDOS.size() == 0) {
            return null;
        }
        List<NewsPusherDTO> result = new ArrayList<>(srcDOS.size());
        NewsPusherDTO dto = null;
        for (MsgPusherDO pusherDO : srcDOS) {
            dto = new NewsPusherDTO();
            BeanCopyUtils.copyBean(pusherDO, dto);
            result.add(dto);
        }
        return result;

    }
}

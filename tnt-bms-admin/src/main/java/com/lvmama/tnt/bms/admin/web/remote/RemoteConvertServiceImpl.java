package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import com.lvmama.tnt.bms.admin.web.service.ConvertManageService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.api.domain.ConvertDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.IConvertService;
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
public class RemoteConvertServiceImpl extends AbstractRemoteService implements IConvertService {

    private static Logger logger = LoggerFactory.getLogger(RemoteConvertServiceImpl.class);

    @Autowired
    private ConvertManageService convertManageService;

    @Override
    public ResponseDTO<Boolean> existName(String name) {
        logger.info("check converter exist : {}", name);
        if (StringUtils.isEmpty(name)) {
            return returnFailed("参数不能为空");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convertManageService.checkExist(name));
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> saveConvert(ConvertDTO convertDTO) {
        logger.info("remote add new converter : {}", convertDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (convertDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            ConvertDO convertDO = new ConvertDO();
            BeanCopyUtils.copyBean(convertDO, convertDO);
            responseDTO.setResult(convertManageService.insert(convertDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> updateConvert(ConvertDTO convertDTO) {
        logger.info("remote update new converter : {}", convertDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (convertDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            ConvertDO convertDO = new ConvertDO();
            BeanCopyUtils.copyBean(convertDO, convertDO);
            responseDTO.setResult(convertManageService.update(convertDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> deleteByName(String name) {
        logger.info("remote delete converter: {}", name);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convertManageService.deleteByName(name));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<ConvertDTO>> findConvertByName(String name) {
        logger.info("remote find converter: {}", name);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(convertManageService.findByName(name)));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<ConvertDTO>> findAllConvert() {
        logger.info("remote find all converter");
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(convertManageService.findAll()));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    public List<ConvertDTO> convert(List<ConvertDO> src) {
        if (src == null || src.size() == 0) {
            return null;
        }
        List<ConvertDTO> result = new ArrayList<>(src.size());
        ConvertDTO dto = null;
        for (ConvertDO convertDO : src) {
            dto = new ConvertDTO();
            BeanCopyUtils.copyBean(convertDO, dto);
            result.add(dto);
        }
        return result;
    }
}

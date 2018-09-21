package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.api.domain.NewsTypeDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.INewsTypeService;
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
public class RemoteNewsTypeServiceImpl extends AbstractRemoteService implements INewsTypeService {

    private static Logger logger = LoggerFactory.getLogger(RemoteNewsTypeServiceImpl.class);

    @Autowired
    private NewsTypeService newsTypeService;

    @Override
    public ResponseDTO<Boolean> existTypeName(String typeName) {
        logger.info("check type exist : {}", typeName);
        if (StringUtils.isEmpty(typeName)) {
            return returnFailed("参数不能为空");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(newsTypeService.existName(typeName));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> saveNewsType(NewsTypeDTO newsTypeDTO) {
        logger.info("remote add new type : {}", newsTypeDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (newsTypeDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            NewsTypeDO newsTypeDO = new NewsTypeDO();
            BeanCopyUtils.copyBean(newsTypeDTO, newsTypeDO);
            responseDTO.setResult(newsTypeService.insert(newsTypeDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> updateNewsType(NewsTypeDTO newsTypeDTO) {
        logger.info("remote update new type : {}", newsTypeDTO);
        ResponseDTO responseDTO = returnSuccess();
        if (newsTypeDTO == null) {
            return returnFailed("传入参数为空");
        }
        try {
            NewsTypeDO newsTypeDO = new NewsTypeDO();
            BeanCopyUtils.copyBean(newsTypeDTO, newsTypeDO);
            responseDTO.setResult(newsTypeService.update(newsTypeDO));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> deleteByType(String typeName) {
        logger.info("remote delete new type : {}", typeName);
        ResponseDTO responseDTO = returnSuccess();
        if (typeName == null) {
            return returnFailed("传入参数为空");
        }
        try {
            responseDTO.setResult(newsTypeService.deleteByName(typeName));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsTypeDTO>> findNewsTypeByName(String typeName) {
        logger.info("remote delete new type : {}", typeName);
        ResponseDTO responseDTO = returnSuccess();
        try {
            List<NewsTypeDO> list = newsTypeService.findTypeListByParam(typeName);
            List<NewsTypeDTO> result = null;
            if (list != null && list.size() > 0) {
                result = new ArrayList<>(list.size());
                NewsTypeDTO dto = null;
                for (NewsTypeDO typeDO : list) {
                    dto = new NewsTypeDTO();
                    BeanCopyUtils.copyBean(typeDO, dto);
                    result.add(dto);
                }
            }
            responseDTO.setResult(result);
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsTypeDTO>> findAllNewsType() {
        return this.findNewsTypeByName("");
    }
}

package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.IAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class RemoteAccessServiceImpl extends AbstractRemoteService implements IAccessService {
    private static Logger logger = LoggerFactory.getLogger(RemoteAccessServiceImpl.class);

    @Autowired
    private NewsAccessService newsAccessService;

    @Override
    public ResponseDTO<NewsAccessDTO> createIfAbsentByName(NewsAccessDTO newsAccessDTO) {
        logger.info("remote createByNameIfAbsent : {}", newsAccessDTO);
        if (newsAccessDTO == null || StringUtils.isEmpty(newsAccessDTO.getName())) {
            return returnFailed("名称不能为空");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            NewsAccessDO exist = newsAccessService.findByName(newsAccessDTO.getName());
            if (exist != null) {
                NewsAccessDTO result = new NewsAccessDTO();
                BeanCopyUtils.copyBean(exist, result);
                responseDTO.setResult(result);
            } else {
                this.saveAccess(newsAccessDTO);
                exist = newsAccessService.findByName(newsAccessDTO.getName());
                BeanCopyUtils.copyBean(exist, newsAccessDTO);
                responseDTO.setResult(newsAccessDTO);
            }
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Boolean> existName(String groupToken, String name) {
        logger.info("check access exist : {}", name);
        ResponseDTO responseDTO = returnSuccess();
        if (StringUtils.isEmpty(groupToken) || StringUtils.isEmpty(name)) {
            return returnFailed("参数不能为空");
        }
        try {
            responseDTO.setResult(newsAccessService.checkExist(groupToken ,name));
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<String> saveAccess(NewsAccessDTO newsAccessDTO) {
        logger.info("remote add access:{}",newsAccessDTO);
        if (newsAccessDTO == null) {
            return returnFailed("参数不能为空！");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(newsAccessService.insertForAPI(newsAccessDTO));
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<Integer> updateAccess(NewsAccessDTO newsAccessDTO) {
        logger.info("remote update access:{}",newsAccessDTO);
        if (newsAccessDTO == null) {
            return returnFailed("参数不能为空！");
        }
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(newsAccessService.updateForAPI(newsAccessDTO));
        } catch (Exception e) {
            logger.error("", e);
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<NewsAccessDTO> findByToken(String token) {
        logger.info("remote find access by token : {}", token);
        ResponseDTO responseDTO = returnSuccess();
        try {
            NewsAccessDO newsAccessDO = newsAccessService.findByToken(token);
            NewsAccessDTO newsAccessDTO = new NewsAccessDTO();
            BeanUtils.copyProperties(newsAccessDO,newsAccessDTO);
            responseDTO.setResult(newsAccessDTO);
        } catch (BusinessException e) {
            logger.error("",e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsAccessDTO>> fuzzyByName(String keyword) {
        logger.info("remote fuzzyByName : {}", keyword);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(newsAccessService.fuzzyByName(keyword)));
        } catch (BusinessException e) {
            logger.error("",e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsAccessDTO>> findByGroupToken(String groupToken) {
        logger.info("remote find access by group token : {}", groupToken);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(newsAccessService.findByGroupToken(groupToken)));
        } catch (BusinessException e) {
            logger.error("",e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<NewsAccessDTO>> findByTokenBatch(List<String> tokens) {
        logger.info("remote find access by batch token : {}", tokens);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(convert(newsAccessService.findByTokenBatch(tokens)));
        } catch (BusinessException e) {
            logger.error("",e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseDTO;
    }

    private List<NewsAccessDTO> convert(List<NewsAccessDO> src) {
        if (src == null || src.size() == 0) {
            return null;
        }
        List<NewsAccessDTO> result = new ArrayList<>(src.size());
        NewsAccessDTO dto = null;
        for (NewsAccessDO accessDO : src) {
            dto = new NewsAccessDTO() ;
            BeanCopyUtils.copyBean(accessDO, dto);
            result.add(dto);
        }
        return result;
    }

    @Override
    public ResponseDTO<Integer> deleteByToken(String token) {
        logger.info("remote delete access: {}", token);
        ResponseDTO responseDTO = returnSuccess();
        try {
            responseDTO.setResult(newsAccessService.deleteByToken(token));
        } catch (Exception e) {
            logger.error("", e);
            responseDTO = returnFailed(e.getMessage());
        }
        return responseDTO;
    }
}

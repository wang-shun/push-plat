package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.domain.enums.ProtocolType;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.mapper.MessageMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsTypeMapper;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.mapper.ConvertMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsAccessMapper;
import com.lvmama.tnt.bms.admin.web.service.ConvertManageService;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import com.lvmama.tnt.bms.admin.web.util.BeanCopyUtils;
import com.lvmama.tnt.bms.admin.web.util.BmsUtil;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
@Service
public class NewsAccessServiceImpl implements NewsAccessService {
    private Logger logger = LoggerFactory.getLogger(NewsAccessServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private NewsTypeMapper newsTypeMapper;
    @Autowired
    private NewsAccessMapper newsAccessMapper;
    @Autowired
    private SendConfigService sendConfigService;
    @Autowired
    private ConvertMapper convertMapper;
    @Autowired
    private ConvertManageService convertManageService;

    @Override
    public NewsAccessDO findFullAccessInfo(Long id) {
        return newsAccessMapper.findFullAccessInfo(id);
    }

    @Override
    public List<NewsAccessDO> findGroupsForChoose() {
        return newsAccessMapper.findGroupsForChoose();
    }

    @Override
    public List<NewsAccessDO> findByGroupID(Long groupID) {
        return newsAccessMapper.findByGroupID(groupID);
    }

    @Override
    public NewsAccessDO findById(Long id) {
        return newsAccessMapper.findById(id);
    }

    @Override
    public NewsAccessDO findByToken(String token) {
        return newsAccessMapper.findByToken(token);
    }

    @Override
    public NewsAccessDO findByName(String name) {
        return newsAccessMapper.findByName(name);
    }

    @Override
    public NewsAccessDO findByParam(NewsAccessDO newsAccessDO) {
        return newsAccessMapper.findByParam(newsAccessDO);
    }

    @Override
    public List<NewsAccessDO> findByPage(NewsAccessDO newsAccessDO, int pageNo, int pageSize) {
        return newsAccessMapper.findByPage(newsAccessDO, PageUtil.getRowBounds(pageNo, pageSize));
    }

    @Override
    public long totalCount(NewsAccessDO accessDO) {
        return newsAccessMapper.totalCount(accessDO);
    }

    @Override
    public NewsAccessDO findAccessGroupByToken(String token) {
        return newsAccessMapper.findAccessGroupByToken(token);
    }

    @Override
    @Transactional
    public int deleteByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return 0;
        }
        try {
            NewsAccessDO newsAccessDO = newsAccessMapper.findByToken(token);
            if (newsAccessDO == null) {
                return 0;
            }
            List<Long> tokenIds = new ArrayList<>();
            if (newsAccessDO.getGroupId() == null) {
                //组 删除组下所有成员
                Long id = newsAccessDO.getId();
                List<NewsAccessDO> children = newsAccessMapper.findByGroupID(id);
                for (NewsAccessDO accessDO : children) {
                    tokenIds.add(accessDO.getId());
                }
                sendConfigService.sendBatchAccessMessage(SyncEvent.EventType.Delete, children);
                newsAccessMapper.deleteByGroupId(id);
            } else {
                tokenIds.add(newsAccessDO.getId());
                sendConfigService.sendAccessMessage(SyncEvent.EventType.Delete,newsAccessDO);
            }
            Map<String, String> paramsMap = new HashMap<>();
            List<NewsTypeDO> types = newsTypeMapper.findTypeListByParam(paramsMap);
            if (types != null && types.size() > 0) {
                for (NewsTypeDO typeDO : types) {
                    messageMapper.deleteMessageToken(typeDO.getId(), tokenIds);
                }
            }
            return newsAccessMapper.deleteByToken(token);
        } catch (BusinessException e) {
            logger.warn(e.getMessage(),e);
            throw e;
        }
    }

    @Override
    @Transactional
    public String insertForAPI(NewsAccessDTO newsAccessDTO) {
        if (newsAccessMapper.existName(newsAccessDTO.getName()) > 0) {
            throw new BusinessException("该分销商名称已注册");
        }
        NewsAccessDO newsAccessDO = new NewsAccessDO();
        BeanCopyUtils.copyBean(newsAccessDTO,newsAccessDO);
        if (!"group".equals(newsAccessDTO.getMode())) {
            checkParams(newsAccessDO);
        }
        Integer convertID = null;
        if (!StringUtils.isEmpty(newsAccessDTO.getConvertMap())) {
            ConvertDO convertDO = new ConvertDO();
            convertDO.setVersion(1);
            convertDO.setRequestMap(newsAccessDTO.getConvertMap());
            convertDO.setName(newsAccessDTO.getName()+"转化器");
            convertMapper.insert(convertDO);
            convertID = convertDO.getId();

            convertManageService.sendMessage(SyncEvent.EventType.Add,convertDO);
            newsAccessDO.setConvertID(convertID);
        }
        newsAccessDO.setToken(BmsUtil.getUUID());
        newsAccessDO.setVersion(1);
        newsAccessDO.setModifyTime(new Date());
        newsAccessDO.setCreateTime(new Date());
        if (newsAccessDO.getOpened() == null) {
            newsAccessDO.setOpened(Constants.OPENED);
        }
        if (newsAccessDO.getPriority() == null) {
            newsAccessDO.setPriority(Constants.NORM_PRIORITY);
        }
        newsAccessMapper.insert(newsAccessDO);
        //组装完整的配置 新增成员
        if (newsAccessDO.getGroupId() != null) {
            NewsAccessDO group = newsAccessMapper.findById(newsAccessDO.getGroupId());
            assemble(newsAccessDO, group);
            //只有成员才需要同步
            sendConfigService.sendAccessMessage(SyncEvent.EventType.Add,newsAccessDO);
        }

        return newsAccessDO.getToken();
    }

    @Override
    public void reloadCache() {
        sendConfigService.sendAccessMessage(SyncEvent.EventType.Reload,"");
    }

    @Override
    @Transactional
    public String updateForAPI(NewsAccessDTO newsAccessDTO){
        NewsAccessDO newsAccessDO = new NewsAccessDO();
        BeanCopyUtils.copyBean(newsAccessDTO,newsAccessDO);

        NewsAccessDO oldAccessDO = newsAccessMapper.findByToken(newsAccessDTO.getToken());
        if (newsAccessDO.getGroupId() == null) {
            newsAccessDO.setGroupId(oldAccessDO.getGroupId());
        }
        if (newsAccessDO.getId() == null) {
            newsAccessDO.setId(oldAccessDO.getId());
        }

        if (!StringUtils.isEmpty(newsAccessDTO.getConvertMap())) {
            ConvertDO convertDO = convertMapper.findByID(oldAccessDO.getConvertID());
            convertDO.setRequestMap(newsAccessDTO.getConvertMap());
            convertDO.setVersion(convertDO.getVersion()+1);
            convertMapper.update(convertDO);

            convertManageService.sendMessage(SyncEvent.EventType.Update,convertDO);
        }
        this.update(newsAccessDO);
        return null;
    }

    private void checkParams(NewsAccessDO newsAccessDO) throws BusinessException {
        if (!BmsUtil.isEmpty(newsAccessDO.getGroupToken())) {
            //有分组
            NewsAccessDO groupDO = newsAccessMapper.findAccessGroupByToken(newsAccessDO.getGroupToken());
            if (groupDO == null) {
                throw new BusinessException("接入失败，查无此分组！");
            }
            newsAccessDO.setGroupId(groupDO.getId());
            if (BmsUtil.isEmpty(newsAccessDO.getName())) {
                throw new BusinessException("分销商名称不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getPushUrl()) && ProtocolType.RPC.getValue() != newsAccessDO.getPushType()) {
                throw new BusinessException("推送地址不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getReceiveType()+"") && BmsUtil.isEmpty(groupDO.getReceiveType()+"")) {
                throw new BusinessException("分组内此设置为空，接收类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getPushType()+"") && BmsUtil.isEmpty(groupDO.getPushType()+"")) {
                throw new BusinessException("分组内此设置为空，推送类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getConverterType()+"") && BmsUtil.isEmpty(groupDO.getConverterType()+"")) {
                throw new BusinessException("分组内此设置为空，转换类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getThreshold()+"") && BmsUtil.isEmpty(groupDO.getThreshold()+"")) {
                throw new BusinessException("分组内此设置为空，推送或拉取频率不能为空！");
            }
        } else {
            //默认分组
            newsAccessDO.setGroupId(Constants.DEFAULT_GROUP_ID);
            if (BmsUtil.isEmpty(newsAccessDO.getName())) {
                throw new BusinessException("分销商名称不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getPushUrl()) && ProtocolType.RPC.getValue() != newsAccessDO.getPushType()) {
                throw new BusinessException("推送地址不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getReceiveType()+"")) {
                throw new BusinessException("接收类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getPushType()+"")) {
                throw new BusinessException("推送类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getConverterType()+"")) {
                throw new BusinessException("转换类型不能为空！");
            }
            if (BmsUtil.isEmpty(newsAccessDO.getThreshold()+"")) {
                throw new BusinessException("推送频率不能为空！");
            }
        }

    }

    @Override
    @Transactional
    public int insert(NewsAccessDO newsAccessDO) {
        if (newsAccessMapper.existName(newsAccessDO.getName()) > 0) {
            throw new BusinessException("该分销商名称已注册");
        }
        int returnCount = 0;
        if (newsAccessDO != null) {
            newsAccessDO.setToken(BmsUtil.getUUID());
//            不设默认值
            if (newsAccessDO.getOpened() == null) {
                newsAccessDO.setOpened(Constants.OPENED);
            }
            if (newsAccessDO.getPriority() == null) {
                newsAccessDO.setPriority(Constants.NORM_PRIORITY);
            }
            newsAccessDO.setVersion(1);
            newsAccessDO.setModifyTime(new Date());
            newsAccessDO.setCreateTime(new Date());
            returnCount = newsAccessMapper.insert(newsAccessDO);
            //组装完整的配置 新增成员
            if (newsAccessDO.getGroupId() != null) {
                NewsAccessDO group = newsAccessMapper.findById(newsAccessDO.getGroupId());
                assemble(newsAccessDO, group);
                //只有成员才需要同步
                sendConfigService.sendAccessMessage(SyncEvent.EventType.Add, newsAccessDO);
            }
        }
        return returnCount;
    }

    @Override
    @Transactional
    public int update(NewsAccessDO newsAccessDO) {
        logger.info("update access: {}", newsAccessDO);
        int updateCount = 0;
        if (newsAccessDO != null) {
            newsAccessDO.setModifyTime(new Date());
            if (newsAccessDO.getVersion() == null) {
                int dataVersion = newsAccessMapper.dataVersion(newsAccessDO.getToken());
                newsAccessDO.setVersion(dataVersion + 1);
            } else {
                newsAccessDO.setVersion(newsAccessDO.getVersion()+1);
            }
            updateCount = newsAccessMapper.update(newsAccessDO);

            try {
                //需要区分是组还是成员 通过groupID区分
                if (newsAccessDO.getGroupId() == null) {
                    logger.info("修改分组信息，并推送消息！");
                    //修改组
                    List<NewsAccessDO> children = newsAccessMapper.findByGroupID(newsAccessDO.getId());
                    for (NewsAccessDO accessDO : children) {
                        assemble(accessDO, newsAccessDO);
                    }
                    sendConfigService.sendBatchAccessMessage(SyncEvent.EventType.Update, children);
                } else {
                    NewsAccessDO group = newsAccessMapper.findById(newsAccessDO.getGroupId());
                    assemble(newsAccessDO, group);
                    sendConfigService.sendAccessMessage(SyncEvent.EventType.Update,newsAccessDO);
                }

            } catch (BusinessException e) {
                logger.warn(e.getMessage(),e);
                throw e;
            }
        }
        return updateCount;
    }

    @Override
    @Transactional
    public int delete(Long id) {
        try {
            NewsAccessDO newsAccessDO = newsAccessMapper.findById(id);
            if (newsAccessDO == null) {
                return 0;
            }
            List<Long> tokenIds = new ArrayList<>();
            if (newsAccessDO.getGroupId() == null) {
                //组 删除组下所有成员
                List<NewsAccessDO> children = newsAccessMapper.findByGroupID(id);
                for (NewsAccessDO accessDO : children) {
                    tokenIds.add(accessDO.getId());
                }
                sendConfigService.sendBatchAccessMessage(SyncEvent.EventType.Delete, children);
                newsAccessMapper.deleteByGroupId(id);
            } else {
                tokenIds.add(newsAccessDO.getId());
                sendConfigService.sendAccessMessage(SyncEvent.EventType.Delete,newsAccessDO);
            }

            Map<String, String> paramsMap = new HashMap<>();
            List<NewsTypeDO> types = newsTypeMapper.findTypeListByParam(paramsMap);
            if (types != null && types.size() > 0) {
                for (NewsTypeDO typeDO : types) {
                    messageMapper.deleteMessageToken(typeDO.getId(), tokenIds);
                }
            }
        } catch (BusinessException e) {
            logger.warn(e.getMessage(),e);
            throw e;
        }
        return newsAccessMapper.delete(id);
    }

    @Override
    @Transactional
    public int removeGroup(Long id) {
        NewsAccessDO newGroup = newsAccessMapper.findById(Constants.DEFAULT_GROUP_ID);
        NewsAccessDO newsAccessDO = newsAccessMapper.findById(id);
        newsAccessDO.setGroupId(Constants.DEFAULT_GROUP_ID);

        assemble(newsAccessDO, newGroup);
        sendConfigService.sendAccessMessage(SyncEvent.EventType.Delete,newsAccessDO);

        return newsAccessMapper.update(newsAccessDO);
    }

    @Override
    public List<NewsAccessDO> findByGroupToken(String groupToken) {
        return newsAccessMapper.findByGroupToken(groupToken);
    }

    @Override
    public boolean checkExist(String groupToken, String name) {
        return newsAccessMapper.checkExist(groupToken,name) > 0;
    }

    @Override
    public List<NewsAccessDO> findByTokenBatch(List<String> tokens) {
        if (tokens == null || tokens.size() == 0) {
            return null;
        }
        return newsAccessMapper.findByTokenBatch(tokens);
    }

    @Override
    public int deleteMessageToken(Long typeId, List<Long> tokenIds) {
        return messageMapper.deleteMessageToken(typeId, tokenIds);
    }

    @Override
    public List<NewsAccessDO> fuzzyByName(String keyword) {
        return newsAccessMapper.fuzzyByName(keyword);
    }

    @Override
    public Map<String, NewsAccessDO> findNameByTokens(List<String> tokens) {
        return newsAccessMapper.findNameByTokens(tokens);
    }

    private void assemble(NewsAccessDO accessDO, NewsAccessDO group) {
        if (accessDO == null || group == null) {
            return;
        }
        if (accessDO.getReceiveType() == null && group.getReceiveType() != null) {
            accessDO.setReceiveType(group.getReceiveType());
        }
        if (accessDO.getPushType() == null && group.getPushType() != null) {
            accessDO.setPushType(group.getPushType());
        }
        if (accessDO.getOpened() == null && group.getOpened() != null) {
            accessDO.setOpened(group.getOpened());
        }
        if (accessDO.getConverterType() == null && group.getConverterType() != null) {
            accessDO.setConverterType(group.getConverterType());
        }
        if (accessDO.getPriority() == null && group.getPriority() != null) {
            accessDO.setPriority(group.getPriority());
        }
       /* if (accessDO.getTimeOut() == null && group.getTimeOut() != null) {
            accessDO.setTimeOut(group.getTimeOut());
        }*/
        if (accessDO.getThreshold() == null && group.getThreshold() != null) {
            accessDO.setThreshold(group.getThreshold());
        }
        if (accessDO.getPushID() == null && group.getPushID() != null) {
            accessDO.setPushID(group.getPushID());
        }
    }
}

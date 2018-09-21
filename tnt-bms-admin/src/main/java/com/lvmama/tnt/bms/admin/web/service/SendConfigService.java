package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/21 0021
 */
public interface SendConfigService {

    void sendMessage(SyncEvent syncEvent);

    <T> void sendMessage(SyncEvent.ObjectType type, SyncEvent.EventType operate, T messageContext) throws BusinessException;

    <T> void sendNewsTypeMessage(SyncEvent.EventType operate, T messageContext) throws BusinessException;

    <T> void sendAccessMessage(SyncEvent.EventType operate, T messageContext) throws BusinessException;

    <T> void sendBatchAccessMessage(SyncEvent.EventType operate, List<T> messageContexts) throws BusinessException;
}

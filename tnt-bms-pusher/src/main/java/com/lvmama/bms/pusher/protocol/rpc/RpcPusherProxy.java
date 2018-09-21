package com.lvmama.bms.pusher.protocol.rpc;

import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.domain.enums.ConvertType;
import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.extend.rpc.AbstractRpcPusher;
import com.lvmama.bms.extend.rpc.Message;
import com.lvmama.bms.extend.rpc.Result;
import com.lvmama.bms.extend.rpc.RpcPusher;
import com.lvmama.bms.pusher.convert.MessageConvertFactory;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.protocol.ProtocolPusher;
import com.lvmama.bms.pusher.protocol.domain.ConvertMapper;
import com.lvmama.bms.pusher.support.MsgCacheManager;

public class RpcPusherProxy implements ProtocolPusher {

    private Logger logger = LoggerFactory.getLogger(RpcPusherProxy.class);

    private MsgCacheManager msgCacheManager;

    public RpcPusherProxy(MsgPusherAppContext appContext) {
        msgCacheManager = appContext.getMsgCacheManager();
    }

    @Override
    public Action push(MessageDTO message) {

        Class<RpcPusher> msgPusherClass = msgCacheManager.getMsgPusherClass(message.getRpcPusherId());
        if(msgPusherClass == null) {
            logger.error("tip=The Message Pusher Class is not exist|"+message);
            return Action.EXECUTE_FAILED;
        }

        try {
            RpcPusher rpcPusher = msgPusherClass.newInstance();
            if(rpcPusher instanceof AbstractRpcPusher) {

                AbstractRpcPusher abstractRpcPusher = (AbstractRpcPusher) rpcPusher;

                ConvertType convertType = ConvertType.getTypeByValue(message.getConverterType());
                if(convertType != null) {

                    MapperContext mapperContext = new MapperContext();

                    if(message.getConvertMapId()!=null) {
                        ConvertMapper convertMapper = msgCacheManager.getConvertMapper(message.getConvertMapId());
                        if(convertMapper!=null) {
                            mapperContext.setRequestMap(convertMapper.getRequestMap());
                            mapperContext.setDataMap(convertMapper.getDataMap());
                            mapperContext.setConvertVersion(convertMapper.getConvertVersion());
                            mapperContext.setEncryptMethod(EncryptMethod.getEnumByValue(message.getEncryptMethod()));
                            mapperContext.setEncryptKey(message.getEncryptKey());
                        }
                    }

                    if(mapperContext.getConvertVersion() == null) {
                        mapperContext.setConvertVersion(ConvertVersion.NotDefine);
                    }

                    ConverterProxy proxy = new ConverterProxy();
                    proxy.setMessageConverter(MessageConvertFactory.getMessageConverter(convertType.name()));
                    proxy.setMapperContext(mapperContext);

                    abstractRpcPusher.setConverter(proxy);

                }

            }

            long pushTimeCost = System.currentTimeMillis();
            Result result =  rpcPusher.push(convertToMessage(message));
            pushTimeCost = System.currentTimeMillis() - pushTimeCost;

            logger.info("tip=success push message|{}|pushTimeCost={}|rpcPusher={}", message, pushTimeCost, msgPusherClass.getName());

            switch (result) {
                case SUCCESS:
                    return Action.EXECUTE_SUCCESS;
                case EXCEPTION:
                    return Action.EXECUTE_EXCEPTION;
                default:
                    return Action.EXECUTE_FAILED;
            }

        } catch (Exception e) {
            logger.error("tip=fail push message|"+message+"|exception=", e);
            return Action.EXECUTE_FAILED;
        }

    }


    private Message convertToMessage(MessageDTO messageDTO) {

        Message message = new Message();

        message.setId(messageDTO.getId()+"-"+messageDTO.getMsgTokenId());
        message.setMsgId(messageDTO.getMsgId());
        message.setMsgType(messageDTO.getMsgType());
        message.setMsgContent(messageDTO.getMsgContent());
        message.setPushUrl(messageDTO.getPushUrl());

        return message;

    }

}

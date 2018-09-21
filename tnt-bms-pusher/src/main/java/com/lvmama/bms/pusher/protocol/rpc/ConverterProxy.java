package com.lvmama.bms.pusher.protocol.rpc;

import com.lvmama.bms.extend.rpc.Converter;
import com.lvmama.bms.pusher.convert.MessageConverter;
import com.lvmama.bms.pusher.map.MapperContext;

public class ConverterProxy implements Converter {

    private MessageConverter messageConverter;

    private MapperContext mapperContext;

    public <T> T formatMessage(Object payload) throws Exception {
        mapperContext.setPayload(payload);
        return messageConverter.formatMessage(mapperContext);
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public void setMapperContext(MapperContext mapperContext) {
        this.mapperContext = mapperContext;
    }

}

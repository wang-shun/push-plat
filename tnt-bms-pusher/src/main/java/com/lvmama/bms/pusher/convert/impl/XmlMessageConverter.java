package com.lvmama.bms.pusher.convert.impl;

import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.v1.impl.XmlMapper;
import com.lvmama.bms.pusher.map.v2.impl.TemplateMapper;

public class XmlMessageConverter extends AbstractMessageConverter {

    private XmlMapper xmlMapper;

    public XmlMessageConverter() {
        this.xmlMapper = new XmlMapper();
    }

    @Override
    public String formatMessage(MapperContext mapperContext) throws Exception {
        switch (mapperContext.getConvertVersion()) {
            case V1:
                XmlMapper xmlMapper = new XmlMapper();
                return xmlMapper.map(mapperContext);
            case V2:
                TemplateMapper templateMapper = new TemplateMapper();
                return templateMapper.map(mapperContext);
            default:
                throw new IllegalArgumentException("tip=the converter config is invalid");
        }
    }

}

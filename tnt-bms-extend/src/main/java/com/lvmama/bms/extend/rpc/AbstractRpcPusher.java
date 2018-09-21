package com.lvmama.bms.extend.rpc;

public abstract class AbstractRpcPusher implements RpcPusher {

    protected Converter converter;

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}

package com.lvmama.bms.extend.rpc;

public interface Converter {

    public <T> T formatMessage(Object payload) throws Exception;

}

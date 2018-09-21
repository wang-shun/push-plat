package com.lvmama.bms.remoting.serialize;


import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.spi.SPI;

/**
 * @author Robert HG (254963746@qq.com) on 11/6/15.
 */
@SPI(key = ExtConfig.REMOTING_SERIALIZABLE_DFT, dftValue = "fastjson")
public interface RemotingSerializable {

    int getId();

    byte[] serialize(final Object obj) throws Exception;

    <T> T deserialize(final byte[] data, Class<T> clazz) throws Exception;
}

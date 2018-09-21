package com.lvmama.bms.remoting.codec;

import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.remoting.serialize.AdaptiveSerializable;
import com.lvmama.bms.remoting.serialize.RemotingSerializable;

/**
 * @author Robert HG (254963746@qq.com) on 11/6/15.
 */
public abstract class AbstractCodec implements Codec {

    protected RemotingSerializable getRemotingSerializable(int serializableTypeId) {

        RemotingSerializable serializable = null;
        if (serializableTypeId > 0) {
            serializable = AdaptiveSerializable.getSerializableById(serializableTypeId);
            if (serializable == null) {
                throw new IllegalArgumentException("Can not support RemotingSerializable that serializableTypeId=" + serializableTypeId);
            }
        } else {
            serializable = ServiceLoader.load(RemotingSerializable.class, Constants.ADAPTIVE);
        }
        return serializable;
    }

}

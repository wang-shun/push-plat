package com.lvmama.bms.nio.codec;

import com.lvmama.bms.nio.channel.NioChannel;

import java.nio.ByteBuffer;

/**
 * @author Robert HG (254963746@qq.com) on 1/30/16.
 */
public interface Encoder {

    ByteBuffer encode(NioChannel channel, Object msg);

}

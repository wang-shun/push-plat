package com.lvmama.meituan;

import com.lvmama.bms.extend.rpc.AbstractRpcPusher;
import com.lvmama.bms.extend.rpc.Message;
import com.lvmama.bms.extend.rpc.Result;
import org.apache.commons.codec.digest.DigestUtils;

public class MeiTuanPusher extends AbstractRpcPusher {

    @Override
    public Result push(Message message) {
        try {
            String format = converter.formatMessage(message.getMsgContent());
            System.out.println("md5: 125="+DigestUtils.md5Hex("125"));
            System.out.println(format);
            return Result.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED;
        }
    }
}

package com.lvmama.tnt.bms.test.service.impl;

import com.lvmama.tnt.bms.test.domain.po.ReceivePO;
import com.lvmama.tnt.bms.test.mapper.ReceiveMapper;
import com.lvmama.tnt.bms.test.service.IReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ReceiveService implements IReceiveService {

    @Autowired
    private ReceiveMapper receiveMapper;

    @Override
    public void save(String dataStr) {
        ReceivePO receivePO = new ReceivePO();
        receivePO.setContext(dataStr);
        receiveMapper.save(receivePO);
    }
}

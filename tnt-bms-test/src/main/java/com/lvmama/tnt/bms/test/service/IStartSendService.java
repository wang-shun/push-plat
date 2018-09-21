package com.lvmama.tnt.bms.test.service;

/**
 *
 */
public interface IStartSendService {

    void startSend();

    void cancelSend();

    void sendOneTime();

    void sendFixedTime(int minute);

    void sendOneThread();
}

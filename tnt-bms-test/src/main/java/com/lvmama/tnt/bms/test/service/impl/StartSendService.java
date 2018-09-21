package com.lvmama.tnt.bms.test.service.impl;

import com.lvmama.bms.core.domain.Message;
import com.lvmama.tnt.bms.test.domain.po.CountPO;
import com.lvmama.tnt.bms.test.domain.po.DistributorPO;
import com.lvmama.tnt.bms.test.mapper.DistributorMapper;
import com.lvmama.tnt.bms.test.mapper.SendCountMapper;
import com.lvmama.tnt.bms.test.service.IStartSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
public class StartSendService implements IStartSendService{

    private Logger logger = LoggerFactory.getLogger(StartSendService.class);

    private static int distributorCount;
    private static final int pageSize = 30;
    private static final int threadCount = 20;
    private static final int taskCount = 50;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(threadCount);
    private List<Future> futures = new ArrayList<>();

    private AtomicInteger totalSend;

    @Autowired
    private DistributorMapper distributorMapper;
    @Autowired
    private SendCountMapper sendCountMapper;

    @Override
    public void startSend() {
        totalSend  = new AtomicInteger(0);
        for (int i = 0; i < taskCount; i++) {
            futures.add(executorService.scheduleAtFixedRate(new SendMessageTask(), 0, 10, TimeUnit.SECONDS));
        }
    }

    @Override
    public void cancelSend() {
        if (futures != null && futures.size() > 0) {
            Future future = null;
            for (int i = 0, len = futures.size(); i < len; i++) {
                future = futures.get(i);
                if (!future.isCancelled()) {
                    future.cancel(true);
                }
            }
        }
        try {
            Thread.sleep(1000);
            sendCountMapper.save(new CountPO(totalSend.get(),new Date()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendOneTime() {
        new Thread(new SendMessageTask()).start();
    }

    @Override
    public void sendFixedTime(int minute) {
        try {
            startSend();
            Thread.sleep(minute*60000);
            cancelSend();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendOneThread() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        futures.add(executorService.scheduleWithFixedDelay(new SendMessageTask(), 0, 10000, TimeUnit.MILLISECONDS));
    }

    class SendMessageTask implements Runnable {
        @Override
        public void run() {
            distributorCount = distributorMapper.distributorCount();
            int offset = RANDOM.nextInt(distributorCount - pageSize);
            List<DistributorPO> distributorPOS = getDistributor(offset, pageSize);
            Map<Integer, StringBuilder> distributorMap = new HashMap<>();
            for (int i = 0,len = distributorPOS.size(); i < len; i++) {
                totalSend.incrementAndGet();
                Integer key = distributorPOS.get(i).getConvertType();
                if (distributorMap.containsKey(key)) {
                    distributorMap.get(key).append(distributorPOS.get(i).getToken()).append(",");
                } else {
                    StringBuilder temp = new StringBuilder();
                    temp.append(distributorPOS.get(i).getToken()).append(",");
                    distributorMap.put(key, temp);
                }
                logger.info("distributor:{}",distributorPOS.get(i));
            }
            for (Map.Entry<Integer, StringBuilder> entry : distributorMap.entrySet()) {
                Integer key =  entry.getKey();
                if (key == null) {
                    MessageClientManager.getInstance().sendMessage(getMessage(0, entry.getValue().toString()));
                } else {
                    MessageClientManager.getInstance().sendMessage(getMessage(key, entry.getValue().toString()));
                }
            }
        }
    }


    private List<DistributorPO> getDistributor(int offset, int pageSize) {
        return distributorMapper.findByPage(offset, pageSize);
    }

    private Object getContext(int type) {
        Map<String, Object> content = new HashMap<String, Object>();
        int num = RANDOM.nextInt(100);
        switch (type) {
            case 1:
                content.put("name", "order");
                content.put("orderId", num);
                content.put("amount", RANDOM.nextInt(1000));
                content.put("status", "success");
            case 2:
                content.put("name", "product");
                content.put("productId", num);
                content.put("describe", RANDOM.nextInt(1000));
            case 3:
                content.put("name", "goods");
                content.put("goodsId", num);
                content.put("describe", RANDOM.nextInt(1000));
            default:
                content.put("name", "lisi"+num);
                content.put("age", num);
                content.put("sex", num%2==0?"male":"female");
        }
        return content;
    }

    private Message getMessage(int type, String tokens) {
        Message message = new Message();
        message.setMsgId(getMsgID());
        message.setReplaceOnExist(true);
        message.setMaxRetry(3);
        message.setMsgType("product1");
        message.setTokens(tokens.split(","));
        message.setPayload(getContext(type));
        return message;
    }

    private static final Random RANDOM = new Random();

    private String getMsgID() {
        return String.valueOf(RANDOM.nextInt(10000));
    }
}

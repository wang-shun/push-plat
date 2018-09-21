package com.lvmama.tnt.bms.test;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.tnt.bms.test.service.impl.MessageClientManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class PerformanceTest {

    public static void main(String[] args){
        PerformanceTest test = new PerformanceTest();
        multiTaskRun(test);
    }

    public static void multiTaskRun(final PerformanceTest test) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(20);
        service.scheduleAtFixedRate(new Runnable() {
                @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    test.sendMessage();
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void sendMessage(){
        Message message = new Message(getMsgID(), "product1", getContent(), getTokens());
        message.setReplaceOnExist(false);
        Response response = MessageClientManager.getInstance().sendMessage(message);
        System.out.println(response);
    }

    public String[] getTokens() {
        String[] tokens = {"4F766309C9C74E69A3CC0456E6B670FE"};//"4F9FF22E8B83478E8F6E7B809697592B","05ABCD7A435741DD90410265344D12CD"};
        return tokens;
    }

    public Map<String, Object> getContent() {
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("name", "order");
        content.put("orderId", RANDOM.nextInt(1000));
        content.put("amount", RANDOM.nextInt(1000));
        content.put("status", "success");
        content.put("product", "南山文化旅游区(181731)");
        content.put("channel", "B2B");
        content.put("payway", "online");
        content.put("company", "广州分公司");
        content.put("visitor", "张三");
        content.put("createUser", "李四");
        content.put("startMillis", System.currentTimeMillis());


        return content;
    }

    private static final Random RANDOM = new Random();
    private String getMsgID() {
        return String.valueOf(RANDOM.nextInt(10000));
    }
}

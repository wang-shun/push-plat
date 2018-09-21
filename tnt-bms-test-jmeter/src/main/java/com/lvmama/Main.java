package com.lvmama;

import com.lvmama.bms.client.domain.Response;

/**
 *
 */
public class Main {
    public static void main(String[] args){
        for(int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                        while (true) {
                        SentMessageJmeter send = new SentMessageJmeter();
                        Response response = send.sendMessage();
                        System.out.println(response.toString());
                    }
                }
            }).start();
        };

    }
}

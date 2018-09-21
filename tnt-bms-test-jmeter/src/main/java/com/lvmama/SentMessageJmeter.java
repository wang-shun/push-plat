package com.lvmama;

import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;





public class SentMessageJmeter extends AbstractJavaSamplerClient {

	private String Samplelabel = "sentmessage";
	Arguments params;

	public void setupTest(JavaSamplerContext context) {
	}

	public Arguments getDefaultParameters(){
		params = new Arguments();
		return params;
	}

	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult results  = new SampleResult();
		results.setSampleLabel(Samplelabel);
		results.sampleStart();
		Response response = this.sendMessage();
		results.sampleEnd();
		if (response != null && (response.getFailedMessages() == null || response.getFailedMessages().size() == 0)) {
			results.setSuccessful(true);
			results.setResponseMessage("send success!");
		} else {
			results.setSuccessful(false);
			results.setResponseMessage("send failed!");
		}
		return results;
	}

    public Response sendMessage(){
        Message message = new Message(getMsgID(), "product", getContent(), getTokens());
        message.setReplaceOnExist(false);
        MessageClient messageClient = MessageClientManager.getInstance();
        Response response = messageClient.sendMessage(message);
        MessageClientManager.release(messageClient);
        return response;
    }

    public String[] getTokens() {
        String[] tokens = {"B8FD47135550445980E642C6D4105BD2"};
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
        return String.valueOf(RANDOM.nextInt(1000000));
    }
	
	
}

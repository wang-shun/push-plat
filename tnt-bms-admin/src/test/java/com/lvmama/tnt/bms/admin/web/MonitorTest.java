package com.lvmama.tnt.bms.admin.web;

import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import com.lvmama.tnt.bms.admin.web.mapper.ReceiverMonitorMapper;
import com.lvmama.tnt.bms.admin.web.util.BmsDateUtils;
import com.sleepycat.je.recovery.RollbackTracker;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/12/5 0005
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class MonitorTest {

    @Autowired
    private ReceiverMonitorMapper receiverMonitorMapper;

    public static void main(String[] args){
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("changeType", "product_info_change");
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("productId", 123456L);
        map1.put("goodsId", 34546L);
        mapList.add(map1);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"request\": {");
        sb.append("\"body\": {");
        sb.append("\"changeType\":" + "\"" + rootMap.get("changeType") + "\",");
        sb.append("\"product\": [");
        for (Map<String, Object> map : mapList) {
            sb.append("{");
            sb.append("\"productId\":" + "\"" + map.get("productId") + "\",");
            sb.append("\"goodsId\":" + "\"" + map.get("goodsId") + "\"");
            sb.append("},");
        }
        sb = sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]");
        sb.append("},");
        sb.append("\"header\": {");
        sb.append("\"signed\":" + "\"" + rootMap.get("signed") + "\"");
        sb.append("}");
        sb.append("}");
        sb.append("}");

        System.out.println(sb.toString());
    }

    @Test
    public void test1() {
        List<ReceiverMonitorDO> list = receiverMonitorMapper.findByInterval("", 151237640800L, null);
        ReceiverMonitorDO receiverMonitorDO = null;
        Long timestamp = BmsDateUtils.getTimeMillis("2017-12-05 12:00:24");
        for (ReceiverMonitorDO monitorDO : list) {
            receiverMonitorDO = new ReceiverMonitorDO();
            receiverMonitorDO.setId(monitorDO.getId());
            receiverMonitorDO.setTimestamp(timestamp);
            receiverMonitorMapper.update(receiverMonitorDO);
            timestamp += 10000L;
        }
    }
}

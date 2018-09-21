package com.lvmama.tnt.bms.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.lvmama.tnt.bms.test.config.HttpClientPool;
import com.lvmama.tnt.bms.test.config.HttpUtil;
import com.lvmama.tnt.bms.test.config.HttpUtils;
import com.lvmama.tnt.bms.test.domain.po.ConvertPO;
import com.lvmama.tnt.bms.test.domain.po.DistributorPO;
import com.lvmama.tnt.bms.test.domain.po.UrlMapPO;
import com.lvmama.tnt.bms.test.domain.vo.RegistVO;
import com.lvmama.tnt.bms.test.mapper.ConvertMapper;
import com.lvmama.tnt.bms.test.mapper.DistributorMapper;
import com.lvmama.tnt.bms.test.mapper.UrlMapMapper;
import com.lvmama.tnt.bms.test.service.IRegisterApiDistributorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试注册20000家分销商
 * convertMap提供组装数据格式
 * urlMap提供接收推送地址，均为http接口，字符串参数
 */
@Service
public class RegisterApiDistributorService implements IRegisterApiDistributorService {

    private Logger logger = LoggerFactory.getLogger(RegisterApiDistributorService.class);

    public static final String register_url = "http://10.200.6.197:8080/tnt-bms-admin/config/access/addNews";

    private AtomicInteger registCount = new AtomicInteger(0);
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private DistributorMapper distributorMapper;

    @Autowired
    private ConvertMapper convertMapper;

    @Autowired
    private UrlMapMapper urlMapMapper;


    @Override
    public void register() {
        List<ConvertPO> convertPOS = convertMapper.findAll();
        List<UrlMapPO> urlMapPOS = urlMapMapper.findAll();
        int step = 500;
        for (int i = 0;i < 2000; i+=step) {
            executorService.submit(new RegisterTask(convertPOS,urlMapPOS, step));
        }
    }

    class RegisterTask implements Runnable {
        List<ConvertPO> convertPOS;
        List<UrlMapPO> urlMapPOS;
        int registerCount;

        public RegisterTask(List<ConvertPO> convertPOS, List<UrlMapPO> urlMapPOS, int registerCount) {
            this.convertPOS = convertPOS;
            this.urlMapPOS = urlMapPOS;
            this.registerCount = registerCount;
        }

        @Override
        public void run() {
            int convertLength = convertPOS.size();
            int urlLength = urlMapPOS.size();
            String threadName = Thread.currentThread().getName();
            DistributorPO distributorPO = null;
            RegistVO registVO = null;
            ConvertPO convertPO = null;
            for (int i = 0;i < registerCount; i++) {
                convertPO = null;
                registVO = new RegistVO();
                registVO.setConverterType(i%2==0?2:1);

                if (i%2==0) {
                    convertPO = convertPOS.get(i%convertLength);
                    registVO.setConvertMap(convertPO.getConvertMap());
                }
                registVO.setName("分销商"+threadName+registCount.incrementAndGet());
                registVO.setPushUrl(urlMapPOS.get(i%urlLength).getUrl());
                registVO.setPriority(5);
                String token = null;
                try {
                    String resp = HttpClientPool.processPostJson((register_url), JSON.toJSONString(registVO));
                    logger.info(resp);
                    token = JSON.parseObject(resp).getString("result");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("注册失败：{}",JSON.toJSONString(registVO));
                    continue;
                }

                distributorPO = new DistributorPO();
                distributorPO.setName(registVO.getName());
                if (convertPO != null) {
                    distributorPO.setConvertMapID(convertPO.getId());
                    distributorPO.setConvertType(convertPO.getType());
                }
                distributorPO.setToken(token);
                logger.info("{}",distributorPO);
                distributorMapper.save(distributorPO);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void initConvert() {
        ConvertPO convertPO = new ConvertPO();
        String context = "<mapping>\n" +
                "    <type name=\"order\">\n" +
                "        <bind name=\"order\"/>\n" +
                "        <prop name=\"orderId\">\n" +
                "            <bind name=\"orderId\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"amount\">\n" +
                "            <bind name=\"amount\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"status\">\n" +
                "            <bind name=\"status\"/>\n" +
                "        </prop>\n" +
                "    </type>\n" +
                "</mapping>";
        convertPO.setConvertMap(context);
        convertPO.setType(1);
        convertMapper.save(convertPO);

        context = "<mapping>\n" +
                "    <type name=\"product\">\n" +
                "        <bind name=\"product\"/>\n" +
                "        <prop name=\"productId\">\n" +
                "            <bind name=\"productId\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"name\">\n" +
                "            <bind name=\"name\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"describe\">\n" +
                "            <bind name=\"describe\"/>\n" +
                "        </prop>\n" +
                "    </type>\n" +
                "</mapping>";
        convertPO.setConvertMap(context);
        convertPO.setType(2);
        convertMapper.save(convertPO);

        context = "<mapping>\n" +
                "    <type name=\"goods\">\n" +
                "        <bind name=\"goods\"/>\n" +
                "        <prop name=\"goodsId\">\n" +
                "            <bind name=\"goodsId\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"name\">\n" +
                "            <bind name=\"name\"/>\n" +
                "        </prop>\n" +
                "        <prop name=\"describe\">\n" +
                "            <bind name=\"describe\"/>\n" +
                "        </prop>\n" +
                "    </type>\n" +
                "</mapping>";
        convertPO.setConvertMap(context);
        convertPO.setType(3);
        convertMapper.save(convertPO);

    }

    @Override
    public void initUrlMap() {
        UrlMapPO urlMapPO = new UrlMapPO();
        for (int i = 0; i < 10;i++) {
            urlMapPO.setUrl("http://10.112.3.49:8022/tnt-bms-test/receive"+i);
            urlMapMapper.save(urlMapPO);
        }
    }

}

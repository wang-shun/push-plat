package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.core.json.TypeReference;
import com.lvmama.tnt.bms.admin.web.domain.po.Order;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.mapper.ReceivedMapper;
import com.lvmama.tnt.bms.admin.web.util.JaxbUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

/**
 *
 */
@RestController
public class PusherReceiveController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PusherReceiveController.class);
    private Random random = new Random();

    @Autowired
    private ReceivedMapper receivedMapper;



    @RequestMapping("/rest/received")
    public ResponseVO received(HttpServletRequest request) {
        String data = "";
        InputStream in = null;
        BufferedReader bReader = null;
        try {
            in = request.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(in));//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s);
            }
            data = sb.toString();
            if (!StringUtils.isEmpty(data)) {
                receivedMapper.received(data);
            }
            LOGGER.info("receive data:{}", data);
        } catch (Exception e) {
            return  returnFailed(e.getMessage());
        }finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
            } catch (Exception e) {

            }
        }
        return returnSuccess();
    }

    @RequestMapping("/rest/receive")
    public ResponseVO receive(HttpServletRequest request) {
        try {
            String data = request.getParameter("product");
            if (com.lvmama.bms.core.commons.utils.StringUtils.isEmpty(data)) {
                data = request.getParameter("order");
            }
            if (com.lvmama.bms.core.commons.utils.StringUtils.isNotEmpty(data)) {
                receivedMapper.received(data);
            }
            LOGGER.info("receive data:{}", data);
        } catch (Exception e) {
            return  returnFailed(e.getMessage());
        }
        return returnSuccess();
    }

}

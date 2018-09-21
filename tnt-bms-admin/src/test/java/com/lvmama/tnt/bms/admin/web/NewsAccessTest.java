package com.lvmama.tnt.bms.admin.web;

import com.lvmama.boot.core.App;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.mapper.MessageMapper;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import com.lvmama.tnt.bms.admin.web.util.HttpUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class NewsAccessTest {

    static {
        App.setProfileIfNotExists("ARK");
    }

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private NewsAccessService newsAccessService;

    @Test
    public void testMessage() {
        Long tokenId = 128235L;
        Long typeId = 140L;
//        messageMapper.deleteMessageToken(typeId, tokenId);
    }

    @Test
    public void findByPageTest() {
        List<NewsAccessDO> list = newsAccessService.findByPage(new NewsAccessDO(), 1, 10);
        Assert.assertEquals(10,list.size());
    }

    @Test
    public void findByIdTest() {
        NewsAccessDO accessDO = newsAccessService.findById(1L);
        System.out.println(FastJsonUtil.toJsonString(accessDO));
    }

    @Test
    public void restAccessTest() throws Exception{
        NewsAccessDO accessDO = new NewsAccessDO();
        int i = 3;
        accessDO.setName("resutfulAccessName");
        accessDO.setReceiveType(i%2+1);
        accessDO.setPushType(i%3+1);
        accessDO.setConverterType(i%3+1);
        accessDO.setPriority(i%10+1);
        accessDO.setThreshold(i);
        accessDO.setPushUrl("http://localhost:8082/bms-admin-web/news/access/addNews");


        String url = "http://localhost:8082/bms-admin-web/news/access/addNews";
        System.out.println(FastJsonUtil.toJsonString(accessDO));
        String resp = HttpUtil.sendPost(url, FastJsonUtil.toJsonString(accessDO));
        System.out.println(resp);

    }

//    @Test
    public void insertTest() {
        NewsAccessDO accessDO = null;
        for (int i = 0; i < 50; i++) {
            accessDO = new NewsAccessDO();
            accessDO.setToken(UUID.randomUUID().toString());
            accessDO.setName("accessName"+i);
            accessDO.setReceiveType(i%2+1);
            accessDO.setPushType(i%3+1);
            accessDO.setOpened(i%2);
            accessDO.setConverterType(i%3+1);
            accessDO.setPriority(i%10+1);
            accessDO.setThreshold(i);
            newsAccessService.insert(accessDO);
        }
    }
}

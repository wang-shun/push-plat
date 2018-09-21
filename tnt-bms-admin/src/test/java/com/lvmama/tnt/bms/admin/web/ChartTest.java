package com.lvmama.tnt.bms.admin.web;

import com.lvmama.tnt.bms.admin.web.domain.po.*;
import com.lvmama.tnt.bms.admin.web.domain.vo.ChartDataVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;
import com.lvmama.tnt.bms.admin.web.mapper.GlobalStatisticsMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsSendSpeedMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsTypeStatisticsMapper;
import com.lvmama.tnt.bms.admin.web.service.ChartService;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.util.BmsDateUtils;
import com.lvmama.tnt.bms.admin.web.util.DateUtil;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class ChartTest {

    @Autowired
    private GlobalStatisticsMapper mapper;
    @Autowired
    private NewsTypeStatisticsMapper newsTypeStatisticsMapper;
    @Autowired
    private NewsTypeService newsTypeService;

    @Autowired
    private ChartService chartService;

    @Autowired
    private NewsAccessService newsAccessService;

    @Autowired
    private NewsSendSpeedMapper sendSpeedMapper;

    public static void main(String[] args){
        String str = "<mapping>\n" +
                "\t<type name=\"teacher\">\n" +
                "\t\t<bind name=\"teacher\"/>\n" +
                "\t\t<prop name=\"id\">\n" +
                "\t\t\t<bind name=\"tid\" isAttr=\"true\"/>\n" +
                "\t\t</prop>\n" +
                "\t\t<prop name=\"name\">\n" +
                "\t\t\t<bind name=\"tname\" isAttr=\"false\"/>\n" +
                "\t\t</prop>\n" +
                "\t\t<prop name=\"student\" type=\"student\" isMulti=\"false\">\n" +
                "\t\t\t<bind name=\"tstudent\" isAttr=\"false\"/>\n" +
                "\t\t</prop>\n" +
                "\t\t<prop name=\"students\" type=\"student\" isMulti=\"true\">\n" +
                "\t\t\t<bind name=\"tstudents\"/>\n" +
                "\t\t</prop>\t\t\n" +
                "\t</type>\n" +
                "\t<type name=\"student\">\n" +
                "\t\t<bind name=\"student\"/>\n" +
                "\t\t<prop name=\"id\" >\n" +
                "\t\t\t<bind name=\"tid\" node=\"attribute\"/>\n" +
                "\t\t</prop>\n" +
                "\t\t<prop name=\"name\">\n" +
                "\t\t\t<bind name=\"tname\" node=\"element\"/>\n" +
                "\t\t</prop>\n" +
                "\t</type>\n" +
                "</mapping>";

        System.out.println(str);
    }

    @Test
    public void speedDetailTest() {
//        List<NewsSpeedDetailVO> vos = sendSpeedMapper.findSpeedDetailByPage("9AEF438A3AC047A78B964DB31BEDAF28","", new RowBounds(0, 20));
//        long count = sendSpeedMapper.totalSpeedDetailCount("9AEF438A3AC047A78B964DB31BEDAF28","");
//        System.out.println(JSON.toJSONString(vos));
//        System.out.println(count);


    }

//    @Test
    public void sendSpeedTest() {
        List<NewsAccessDO> list = newsAccessService.findByPage(new NewsAccessDO(), 1, 50);
        List<MessageDO> messageDOS = null;//messageDOMapper.findMessage();
        int size = list.size()*messageDOS.size();
        List<MessageTokenDO> tokenDOS = new ArrayList<>(size);
        MessageTokenDO tokenDO = null;
        String msgId = null;
        String token = null;
        for (int i = 0,len1 = messageDOS.size(); i < len1; i++) {
//            msgId = messageDOS.get(i).getId();
            for (int j = 0,len2 = list.size(); j < len2; j++) {
                tokenDO = new MessageTokenDO();
                tokenDO.setMsgId(msgId);
                tokenDO.setToken(list.get(j).getToken());
                tokenDO.setNowRetry(Short.valueOf("3"));
                tokenDO.setModifyTime(new Date());
                tokenDO.setStatus(Byte.valueOf(""+j%4));
                tokenDOS.add(tokenDO);
            }
        }
      //  messageTokenDOMapper.batchInsert(tokenDOS);
    }

    @Test
    public void newsTypestatisticsFindTest() {
        /*List<NewsTypeStatisticsDO> list = newsTypeStatisticsMapper.findNewsTypeStatis("type1", 1510105487000L,1510191887000L);
        Assert.assertNotNull(list.size());*/
        RequestVO vo = new RequestVO();
        vo.setMsgType("type1");
        vo.setStartTime(BmsDateUtils.getOneDayBeforeTime());
        vo.setEndTime(DateUtil.convert(new Date(), BmsDateUtils.DATE_FORMAT));
        ChartDataVO chartDataVO = chartService.getNewsTypeData(vo);
        System.out.println(FastJsonUtil.toJsonString(chartDataVO));
    }

//    @Test
    public void insertNewsTypeStatistics() {
        Random random = new Random(1000);
        NewsTypeStatisticsDO statisticsDO = null;
        List types = newsTypeService.findTypeList();
        for (int i = 0; i < 200; i++) {
            statisticsDO = new NewsTypeStatisticsDO();
            statisticsDO.setMsgType((String) types.get(random.nextInt(types.size())));
            statisticsDO.setReachCount(random.nextInt(500));
            statisticsDO.setReceiveCount(random.nextInt(500));
            statisticsDO.setSendCount(random.nextInt(1000));
            statisticsDO.setFailCount(random.nextInt(100));
            statisticsDO.setTimestamp(System.currentTimeMillis());
            newsTypeStatisticsMapper.insert(statisticsDO);
        }
    }

    @Test
    public void getDateTest() {
        RequestVO reqvo = new RequestVO();
        reqvo.setStartTime(BmsDateUtils.getOneDayBeforeTime());
        reqvo.setEndTime(DateUtil.convert(new Date(),BmsDateUtils.DATE_FORMAT));
        ChartDataVO chartDataVO = chartService.getGlobalData(reqvo);
        System.out.println(FastJsonUtil.toJsonString(chartDataVO));

    }

    @Test
    public void testTypeUpdate() {
        List<NewsTypeStatisticsDO> list = newsTypeStatisticsMapper.findNewsTypeStatis(null,1511247844811L, null);
        NewsTypeStatisticsDO globalStatisticsDO = null;
        Long timestamp = BmsDateUtils.getTimeMillis("2017-12-06 09:00:00");
        for (NewsTypeStatisticsDO statisticsDO : list) {
            globalStatisticsDO = new NewsTypeStatisticsDO();
            globalStatisticsDO.setId(statisticsDO.getId());
            globalStatisticsDO.setTimestamp(timestamp);
            newsTypeStatisticsMapper.update(globalStatisticsDO);
            timestamp += 100000;
        }
    }

    @Test
    public void testUpdate() {
        List<GlobalStatisticsDO> list = mapper.findByTimeSlot(1511247844811L, null);
        GlobalStatisticsDO globalStatisticsDO = null;
        Long timestamp = BmsDateUtils.getTimeMillis("2017-12-06 09:00:00");
        for (GlobalStatisticsDO statisticsDO : list) {
            globalStatisticsDO = new GlobalStatisticsDO();
            globalStatisticsDO.setId(statisticsDO.getId());
            globalStatisticsDO.setTimestamp(timestamp);
            mapper.update(globalStatisticsDO);
            timestamp += 100000;
        }
    }

//    @Test
    public void insertGlobalTest() {
        Random random = new Random(1000);
        GlobalStatisticsDO statisticsDO = null;
        for (int i = 0; i < 200; i++) {
            statisticsDO = new GlobalStatisticsDO();
            statisticsDO.setReachCount(random.nextInt(500));
            statisticsDO.setReceiveCount(random.nextInt(500));
            statisticsDO.setSendCount(random.nextInt(1000));
            statisticsDO.setFailCount(random.nextInt(100));
            statisticsDO.setTimestamp(System.currentTimeMillis());
            mapper.insert(statisticsDO);
        }

    }
}

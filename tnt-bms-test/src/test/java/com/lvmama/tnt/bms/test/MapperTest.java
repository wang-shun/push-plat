package com.lvmama.tnt.bms.test;

import com.lvmama.tnt.bms.test.domain.po.DistributorPO;
import com.lvmama.tnt.bms.test.mapper.DistributorMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MapperTest {

    @Autowired
    private DistributorMapper distributorMapper;

    @Test
    public void test1() {
        DistributorPO distributorPO = new DistributorPO();
//        distributorPO.setConvertMap("<mapping></mapping>");
        distributorPO.setToken(UUID.randomUUID().toString());
        distributorPO.setName(Thread.currentThread().getName());
//        distributorMapper.save(distributorPO);

    }
}

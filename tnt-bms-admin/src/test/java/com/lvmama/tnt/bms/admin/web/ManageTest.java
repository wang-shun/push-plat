package com.lvmama.tnt.bms.admin.web;

import com.lvmama.bms.core.json.JSON;
import com.lvmama.boot.core.App;
import com.lvmama.tnt.bms.admin.web.manage.constant.Constants;
import com.lvmama.tnt.bms.admin.web.manage.domain.ResourceNode;
import com.lvmama.tnt.bms.admin.web.manage.mapper.ResourceMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class ManageTest {
    static {
        App.setProfileIfNotExists("ARK");
    }

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    public void test() {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", 1);
        params.put("parentCode", Constants.ROOT);
        List<ResourceNode>  nodes = resourceMapper.loadDistributeStore(params);

        System.out.println(JSON.toJSONString(nodes));
    }
}

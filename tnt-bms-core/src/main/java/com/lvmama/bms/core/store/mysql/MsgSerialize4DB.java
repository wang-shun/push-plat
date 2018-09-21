package com.lvmama.bms.core.store.mysql;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.Arrays;

public class MsgSerialize4DB {


    public static String serialize(Object object) {
        JSONArray jsonArray = new JSONArray(Arrays.asList(object));
        return jsonArray.toString();
    }

    public static Object unserialize(String object) {
        JSONArray jsonArray = JSON.parseArray(object);
        return jsonArray.get(0);
    }

}

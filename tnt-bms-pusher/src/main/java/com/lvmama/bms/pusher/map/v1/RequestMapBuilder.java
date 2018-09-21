package com.lvmama.bms.pusher.map.v1;

import com.lvmama.bms.core.commons.utils.JAXBUtils;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;
import com.lvmama.bms.pusher.map.v1.vo.DataProp;
import com.lvmama.bms.pusher.map.v1.vo.DataType;

import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class RequestMapBuilder {

    public static DataMap build(String mapXml) throws JAXBException {

        DataMap dataMap = JAXBUtils.deserialize(new StringReader(mapXml), DataMap.class);

        Map<String, DataType> allDataType = new HashMap<>();
        for(DataType dataType : dataMap.getDataTypes()) {
            allDataType.put(dataType.getName(), dataType);
        }

        for(DataType dataType : dataMap.getDataTypes()) {
            for(DataProp dataProp : dataType.getProperties()) {
                if(dataProp.getType()!=null) {
                    DataType propType = allDataType.get(dataProp.getType());
                    if(propType!=null) {
                        dataProp.setDataType(propType);
                        if(propType.isRoot()) {
                            if(!propType.getName().equals(dataType.getName())) {
                                propType.setRoot(false);
                            }
                        }
                    } else {
                        throw new IllegalArgumentException(String.format("The Type{%s}.Prop{%}.Type{%s} is not exist", dataType.getName(), dataProp.getName(), dataProp.getType()));
                    }
                }
            }
        }

        for(DataType dataType : dataMap.getDataTypes()) {
            if(dataType.isRoot()) {
                dataMap.setRootType(dataType);
                break;
            }
        }

        return dataMap;

    }

}

package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
public class ChartDataVO implements Serializable{

    private static final long serialVersionUID = -6702524725008782771L;

    private List<String> labels;
    private Map<String, List<String>> datasetsMap;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Map<String, List<String>> getDatasetsMap() {
        return datasetsMap;
    }

    public void setDatasetsMap(Map<String, List<String>> datasetsMap) {
        this.datasetsMap = datasetsMap;
    }
}

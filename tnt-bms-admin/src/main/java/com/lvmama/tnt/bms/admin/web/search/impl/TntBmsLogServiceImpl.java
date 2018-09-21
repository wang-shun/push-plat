package com.lvmama.tnt.bms.admin.web.search.impl;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.tnt.bms.admin.web.configure.ElasticsearchProperties;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.LogVO;
import com.lvmama.tnt.bms.admin.web.search.TntBmsLogService;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.util.DateUtil;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class TntBmsLogServiceImpl implements TntBmsLogService {
    private Logger logger = LoggerFactory.getLogger(TntBmsLogServiceImpl.class);

    @Autowired
    private TransportClient client;

    @Autowired
    private NewsAccessService newsAccessService;

    @Autowired
    ElasticsearchProperties elasticsearchProperties;

    @Override
    public ResponseDTO<LogVO.Response> queryById(String id) {
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds(id);
        SearchResponse response = client.prepareSearch(getIndices())
                .setTypes("log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(idsQueryBuilder)
                .execute().actionGet();

        LogVO.Response result = null ;
        for (SearchHit hit: response.getHits().getHits()){
            result = convert(hit);
        }
        ResponseDTO<LogVO.Response> responseDTO = new ResponseDTO<>();
        responseDTO.setResult(result);
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<LogVO.Response>> searchLog(LogVO.Request request) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(request.getToken())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("token", request.getToken()));
        }
        if (StringUtils.isNotEmpty(request.getMsgType())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("msgType", request.getMsgType()));
        }
        if (StringUtils.isNotEmpty(request.getMsgId())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("msgId", request.getMsgId()));
        }


        String pushStatus = request.getPushStatus();
        BoolQueryBuilder tipQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(pushStatus) && "success".equals(pushStatus)) {
            tipQuery.should(QueryBuilders.matchPhraseQuery("tip", "success push message"));
        } else if (StringUtils.isNotEmpty(pushStatus) && "fail".equals(pushStatus)) {
            tipQuery.should(QueryBuilders.matchPhraseQuery("tip", "fail push message"));
        } else {
            tipQuery.should(QueryBuilders.matchPhraseQuery("tip", "success push message"))
                    .should(QueryBuilders.matchPhraseQuery("tip", "fail push message"));
        }
        queryBuilder.must(tipQuery);

        String[] queryTimes = request.getQueryTime();
        if (queryTimes != null && queryTimes.length == 2) {
            RangeQueryBuilder timeQuery = QueryBuilders.rangeQuery("dateTime")
                    .from(convertTime(queryTimes[0])).to(convertTime(queryTimes[1]));
            queryBuilder.must(timeQuery);
        } else {

        }

        Integer pageNo = request.getPageNo();
        Integer pageSize = request.getPageSize();
        SearchResponse response = client.prepareSearch(getIndices())
                .setTypes("log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort("dateTime", SortOrder.DESC)
                .setFrom((pageNo-1)*pageSize)
                .setSize(pageSize)
                .execute().actionGet();

        long totalCount = response.getHits().getTotalHits();
        List<LogVO.Response> result = new ArrayList<>(pageSize);
        List<String> tokens = new ArrayList<>();
        LogVO.Response r = null;
        for (SearchHit hit: response.getHits().getHits()){
            r = convert(hit);
            result.add(r);
            tokens.add(r.getToken());
        }
        if (!tokens.isEmpty()) {
            Map<String, NewsAccessDO> tokenMap = newsAccessService.findNameByTokens(tokens);
            if (tokenMap != null && !tokenMap.isEmpty()) {
                for (LogVO.Response resp : result) {
                    resp.setDistributeName(tokenMap.get(resp.getToken()).getName());
                }
            }
        }
        ResponseDTO<List<LogVO.Response>> responseDTO = new ResponseDTO<>();
        responseDTO.setTotalCount(totalCount);
        responseDTO.setResult(result);
        return responseDTO;
    }

    private LogVO.Response convert(SearchHit hit) {
        Map<String, Object> source = hit.getSource();
        LogVO.Response log = new LogVO.Response();
        log.setId(hit.getId());
        log.setMsgId(handleData(source.get("msgId")));
        log.setMsgType(handleData(source.get("msgType")));
        log.setToken(handleData(source.get("token")));
        log.setDistributeName(handleData(source.get("distributeName")));
        log.setResponseStatus(handleData(source.get("responseStatus")));
        log.setMaxRetry(handleData(source.get("maxRetry")));
        log.setReceiveTime(handleData(source.get("receiveTime")));
        log.setPushTimeCost(handleData(source.get("pushTimeCost")));
        log.setRetryCount(handleData(source.get("retryCount")));
        log.setConvertMapId(handleData(source.get("convertMapId")));
        log.setRequestBody(handleData(source.get("requestBody")));
        log.setResponseContent(handleData(source.get("responseContent")));
        log.setRequestHeader(handleData(source.get("requestHeader")));
        log.setException(handleData(source.get("exception")));
        log.setMsgContent(handleData(source.get("msgContent")));
        String message = (String) source.get("tip");
        log.setPushStatus("success push message".equals(message) ? "成功" : "失败");
        return log;
    }

    private String handleData(Object object) {
        return object == null || "null".equals(object) ? "" : object.toString();
    }


    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
    private static SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Long convertTime(String time){
        try {
            if (time.endsWith("Z")) {
                time = time.replace("Z", " UTC");
                return sf.parse(time).getTime();
            } else {
                return sf1.parse(time).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getIndices() {
        List<String> indices = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(elasticsearchProperties.suffixFormat);
        /*if (!"online".equals(elasticsearchProperties.environment)) {
            indices.add(elasticsearchProperties.prefix + sdf.format(new Date()));
        } else {
            Date date = new Date();
            for (int i = 0; i < 7; i++) {
                indices.add(elasticsearchProperties.prefix + sdf.format(DateUtil.addDayToDate(date,-i)));
            }
        }*/
        indices.add(elasticsearchProperties.prefix);
        String[] result = new String[indices.size()];
        return indices.toArray(result);
    }
}

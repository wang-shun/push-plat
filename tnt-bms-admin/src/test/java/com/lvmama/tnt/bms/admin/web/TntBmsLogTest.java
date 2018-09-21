package com.lvmama.tnt.bms.admin.web;

import com.lvmama.boot.core.App;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class TntBmsLogTest {
    static {
        App.setProfileIfNotExists("ARK");
    }

    @Autowired
    private TransportClient client;

    @Test
    public void query2() {
        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.matchQuery("msgType", "order"))
//                .add(QueryBuilders.matchQuery("msgId", "15807900"))
                .add(QueryBuilders.matchQuery("tip", "push message"))
//                .add(QueryBuilders.matchQuery("token","89FCBD76542111E8A473525400641218"))
                .add(QueryBuilders.rangeQuery("receiveTime").from("2018-07-26 05:43:50").to("2018-07-25 05:43:50"))
                ;
        BoolQueryBuilder tipQuery = QueryBuilders.boolQuery();
        tipQuery.should(QueryBuilders.matchPhraseQuery("tip", "success push message"))
                .should(QueryBuilders.matchPhraseQuery("tip", "fail push message"));

        BoolQueryBuilder classNameQuery = QueryBuilders.boolQuery();
        classNameQuery.must(QueryBuilders.matchQuery("className","com.lvmama.bms.pusher.protocol.http.HttpPusher"));

        RangeQueryBuilder rangeQueryBuilder =
        QueryBuilders
                .rangeQuery("@timestamp")
                .from("2018-07-25T05:45:34.378Z").to("2018-07-28T05:45:34.378Z");

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("msgType", "order"))
                .must(tipQuery);//.must(rangeQueryBuilder);


        SearchResponse response = client.prepareSearch("lvmama-tnt_message_biz-2018.07")
                .setTypes("log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort("dateTime", SortOrder.DESC)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        for (SearchHit hit: response.getHits().getHits()){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void query1() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("body","push message");
        SearchResponse response = client.prepareSearch("lvmama-tnt_message_biz-2018.07")
                .setTypes("log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        for (SearchHit hit: response.getHits().getHits()){
            System.out.println(hit.getSourceAsString());
        }
    }
}

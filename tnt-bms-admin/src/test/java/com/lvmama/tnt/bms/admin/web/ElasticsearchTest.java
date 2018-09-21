package com.lvmama.tnt.bms.admin.web;

import com.lvmama.boot.core.App;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class ElasticsearchTest {

    static {
        App.setProfileIfNotExists("ARK");
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name","elasticsearch")
                    .put("client.transport.sniff",true).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.200.6.197"), 9300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TransportClient client;

    @Test
    public void commonTermQuery() {
        SearchResponse res = null;
        QueryBuilder qb = QueryBuilders
                .commonTermsQuery("title","article");

        res = client.prepareSearch("test")
                .setTypes("test")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        for (SearchHit hit: res.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }

        // on shutdown
        client.close();

        //common terms query
    }

    @Test
    public void multiMatchQuery() {
        SearchResponse res = null;
        QueryBuilder qb = QueryBuilders
                .multiMatchQuery("老虎","title");

        res = client.prepareSearch("test")
                .setTypes("test")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        for (SearchHit hit: res.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }

        // on shutdown
        client.close();
    }

    @Test
    public void matchQuery( ) {
        SearchResponse res = null;
        QueryBuilder qb = QueryBuilders.matchQuery("title", "豹");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query();

                res = client.prepareSearch("test")
                .setTypes("test")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .setFrom(0)
                .setSize(10)
//                .addFields(new String[]{"cphm1","jcdid","cplx1","tpid1","tgsj","cdid"})
                .execute().actionGet();
        for (SearchHit hit: res.getHits().getHits()){
            System.out.println(hit.getSourceAsString());
        }

        // on shutdown
        client.close();
    }

    @Test
    public void matchAllQuery() {
        SearchResponse res = null;
        QueryBuilder qb = QueryBuilders.matchAllQuery();

        res = client.prepareSearch("test")
                .setTypes("test")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();
        for (SearchHit hit: res.getHits().getHits()){
            System.out.println(hit.getSourceAsString());
        }

        // on shutdown
        client.close();
    }


    @Test
    public void indexGet( ) {
        SearchResponse res = null;
        res = client.prepareSearch("test").setTypes("test").get();

        System.out.println(res);
        // on shutdown
        client.close();
    }

    @Test
    public void indexCreate() {
        String jsonSource = "";
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject();
            contentBuilder.field("title", "今晚打豹子");
            contentBuilder.field("body", "号称是赌圣的存在，1937年某一天今晚打老虎");
            contentBuilder.field("publish", "2016-12-5");
            jsonSource = contentBuilder.endObject().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexResponse res = null;
        res = client.prepareIndex("test","test").setSource(jsonSource ).execute().actionGet();
        System.out.println(res);
        // on shutdown
        client.close();
    }
}

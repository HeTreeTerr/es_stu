package com.hss.es;

import com.alibaba.fastjson.JSON;
import com.hss.entity.Product;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 使用java Api批量操作es集群测试类
 */
public class TestEsBulk {

    private final static Logger logger = Logger.getLogger(TestES.class);
    /**
     * 连接es集群的端口号
     */
    private final static int PORT = 9300;
    /**
     * 索引库
     */
    private final static String INDEX = "bigdata";
    /**
     * 类型
     */
    private final static String TYPE = "product";
    //过时（7.0过时，8.0移除）
    private TransportClient client;

    /**
     * 前处理
     */
    @Before
    public void setup() throws UnknownHostException {
        logger.info("前处理操做，用于进行全局的初始化...");
        Settings settings = Settings.builder().put("cluster.name","bigdata").build();
        client = new PreBuiltTransportClient(settings);
        //集群,多个节点
//        client.addTransportAddresses(new TransportAddress(InetAddress.getByName("localhost"),PORT),
//                new TransportAddress(InetAddress.getByName("localhost"),PORT),
//                new TransportAddress(InetAddress.getByName("localhost"),PORT));
        //单节点
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.2.106"),PORT));

    }

    /**
     * 批处理操作索引信息
     */
    @Test
    public void testBulkDealWith(){
        Product product1 = new Product();
        product1.setName("gitHub");
        product1.setAuthor("李二小");
        product1.setVersion("1.4.7");
        Product product2 = new Product();
        product2.setName("svn");
        product2.setAuthor("王小二");
        product2.setVersion("1.4.3");
        //1.数据准备
        IndexRequestBuilder indexRequestBuilder1 = client.prepareIndex(INDEX,TYPE).setSource(JSON.toJSONString(product1),XContentType.JSON);
        IndexRequestBuilder indexRequestBuilder2 = client.prepareIndex(INDEX,TYPE).setSource(JSON.toJSONString(product2),XContentType.JSON);

        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(INDEX,TYPE,"3");
        //2.批处理实施
         BulkResponse responses = client.prepareBulk()
                .add(indexRequestBuilder1)
                .add(indexRequestBuilder2)
                .add(deleteRequestBuilder).get();
        //3.分析批处理之后的返回结果
        BulkItemResponse[] items = responses.getItems();
        for(BulkItemResponse item : items){
            logger.info(item.toString());
        }
    }

    /**
     * 测试：检索类型，以及分页检索
     */
    @Test
    public void testSearchType(){
        //当前页
        int currentPage = 1;
        //每页显示的记录条数
        int pageSize = 2;

        //开始进行定制SearchType的分页检索
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFrom((currentPage - 1) * pageSize)
                .setSize(pageSize)
                .setQuery(QueryBuilders.termQuery("name","hss"))
                .get();

        //显示检索到的信息
        SearchHits hits = response.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * boolQuery演示
     * 需求：查询索引库中，name为xx的索引信息
     */
    @Test
    public void testBoolQuery(){
        //must必须满足，should尽量满足
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchPhraseQuery("name","hss"))
//                        .should(QueryBuilders.wildcardQuery("name","svn"))
                        .should(QueryBuilders.matchPhraseQuery("name","svn"))
                )
                .get();
        //显示检索到的信息
        SearchHits hits = response.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * 测试fuzzyQuery(模糊查询)
     * 查询：内容中包含name值包含xx的索引信息
     */
    @Test
    public void testFuzzQuery(){
        SearchResponse response  = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.fuzzyQuery("name","hsww"))
                .get();
        //显示检索到的信息
        SearchHits hits = response.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * 查询所有
     * matchAllQuery()匹配所有文件 match_all查询是Elasticsearch中简单的查询之一。
     * 它使我们能够匹配索引中的所有文件。
     * 需求：查询索引库之INDEX下的TYPE下的所有记录（第一页）信息，且根据id进行降序排列
     */
    @Test
    public void testMatchAllQuery(){
        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.matchAllQuery())
                //只支持int型的，字符串会报错
                //.addSort("id",SortOrder.DESC)
                .get();
        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * regexpQuery演示
     * 案例：查询索引库下INDEX下的TYPE之下name字段包含有oo的索引信息。（使用regexpQuery的形式实现）
     */
    @Test
    public void testRegexpQuery(){
        //正则表达式
        String regex = ".+kee.+";

        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.regexpQuery("name",regex))
                .get();
        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * termQuery
     * 词条查询是ElasticSearch中的一个简单查询。它仅匹配在给定字段中
     * 含有该词条的文档，而且是确切的、未经分析的词条
     * 需求：使用termQuery,查询出author中含有‘天’，且包含‘下’的索引信息。
     * 暂时，只识别单个字，不识别词（需要中文分词）
     */
    @Test
    public void testTermQuery(){
        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setTypes(TYPE)
//                .setQuery(QueryBuilders.termQuery("author","小二"))
                .setQuery(QueryBuilders.termsQuery("author","王","小"))
                .get();

        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * wildcardQuery
     * *匹配多个字符，？匹配一个字符
     * 注意：避免*开始，会检索大量内容造成效率缓慢
     * 需求：使用wildcardQuery，查询出name中包含有m的所有索引信息
     */
    @Test
    public void testWildcardQuery(){
        SearchResponse searchResponse = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.wildcardQuery("name","??????_*")).get();
        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * 测试中文分词插件（没有安装中文分词插件的情形）
     *
     * 查询ok索引中type之news中的字段content中包含有中的索引信息
     */
    @Test
    public void testChinesePlugin(){
//        中文分词前
        /*SearchResponse searchResponse = client.prepareSearch("bigdata")
                .setTypes("product")
                .setQuery(QueryBuilders.termQuery("name","入门"))
                .get();
        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }*/

//        中文分词后
        SearchResponse searchResponse = client.prepareSearch("chinese")
//                .setTypes("_doc")
                .setQuery(QueryBuilders.termQuery("content","洛杉矶"))
                .get();
        // 获取命中次数，查询结果有多少对象
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits){
            logger.info(hit.getSourceAsString());
        }
    }

    /**
     * 后处理操作
     */
    @After
    public void cleanup(){
        logger.info("后处理操作，用于资源释放");
        if(client != null){
            client.close();
        }
    }
}

package com.hss.es;

import com.alibaba.fastjson.JSON;
import com.hss.entity.Product;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
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
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.40.123"),PORT));

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

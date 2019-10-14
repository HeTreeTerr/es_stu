package com.hss.es;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hss.entity.Product;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用java Api操作es集群测试类
 */
public class TestES {

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
    //新的
    private RestClient restClient;
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
     * 核心测试的方法
     */
    @Test
    public void testEnv(){
        logger.info("开始测试");
        logger.info("client="+client.toString());
    }

    /**
     * 使用JSON的方式新增索引
     */
    @Test
    public void testNewAddIndexJsonWay(){
        logger.info("开始使用json的方式新增索引了...");
        Product product = new Product();
        product.setName("spring_cloud入门到放弃");
        product.setAuthor("李大宝");
        product.setVersion("1.9.10");
        String jsonStr = JSON.toJSONString(product);
        logger.info(jsonStr);
        IndexResponse response = client.prepareIndex(INDEX,TYPE,"1").setSource(jsonStr,XContentType.JSON).get();
        logger.info("响应结果response="+response);
    }

    /**
     * 使用map的方式新增索引
     */
    @Test
    public void testNewAddIndexMapWay(){
        logger.info("开始使用map的方式新增索引");
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("name","zookeeper从入门到放弃");
        map.put("author","李二宝");
        map.put("version","1.1.0");
        IndexResponse response = client.prepareIndex(INDEX,TYPE,"2").setSource(map).get();
        logger.info("响应结果response="+response);
    }

    /**
     * 使用bean的方式新增索引
     */
    @Test
    public void testNewAddIndexBeanWay() throws JsonProcessingException {
        logger.info("使用bean的方式增加索引");
        Product product = new Product();
        product.setName("activeMQ_从入门到放弃");
        product.setAuthor("李二狗");
        product.setVersion("1.5.7");
        byte[] beanBytes = new ObjectMapper().writeValueAsBytes(product);

        IndexResponse response = client.prepareIndex(INDEX,TYPE,"3").setSource(beanBytes,XContentType.JSON).get();
        logger.info("响应结果response="+response);
    }

    /**
     * 使用es helper的方式增加索引
     */
    @Test
    public void testAddIndexHelperWay() throws IOException {
        logger.info("正在使用es helper的方式增加索引");
        XContentBuilder xBuilder = XContentFactory.contentBuilder(XContentType.JSON)
                .startObject()
                .field("name","redis_从入门到放弃")
                .field("author","李狗蛋")
                .field("version","1.15.6")
                .endObject();
        IndexResponse response = client.prepareIndex(INDEX,TYPE,"4")
                .setSource(xBuilder).get();
        logger.info("响应结果response="+response);
    }

    /**
     * 根据特定的id查询索引
     */
    @Test
    public void testFindIndexById() throws InvocationTargetException, IllegalAccessException {
        logger.info("正在使用特定的id查询索引");
        GetResponse response = client.prepareGet(INDEX,TYPE,"1").get();
        Map<String, Object> source = response.getSource();

        Product product = new Product();
        BeanUtils.populate(product,source);
        logger.info("product-->"+product);
    }

    /**
     * es javaApi根据特定id更新索引信息
     */
    @Test
    public void testUpdateById(){
        Product product = new Product();
        product.setAuthor("李二小");
        product.setVersion("1.8.7");
        UpdateResponse response = client.prepareUpdate(INDEX,TYPE,"1")
                .setDoc(JSON.toJSONString(product),XContentType.JSON).get();
        logger.info("响应结果response="+response);
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

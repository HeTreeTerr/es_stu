package com.hss.es;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * RestClient测试类
 */
public class TestRestClient {

    private final static Logger logger = Logger.getLogger(TestRestClient.class);

    private RestClient restClient;

    /**
     * 前处理
     */
    @Before
    public void setup(){
        logger.info("前处理操做，用于进行全局的初始化...");
        restClient = RestClient.builder(
            //new HttpHost("localhost", 9200, "http"),
            new HttpHost("192.168.40.188", 9200, "http")).build();
    }
    @Test
    public void testEnv(){
        logger.info("开始测试");
        logger.info("restClient="+restClient);
    }
    /**
     * 后处理操作
     */
    @After
    public void cleanup() throws IOException {
        logger.info("后处理操作，用于资源释放");
        if(restClient != null){
            restClient.close();
        }
    }
}

package com.hss.es;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 使用java Api操作es集群测试类
 */
public class TestES {

    private final static Logger logger = Logger.getLogger(TestES.class);

    private final static int PORT = 9300;
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
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.40.188"),PORT));

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

package com.hss.book;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 系统属性测试类
 */
public class TestSys {

    private final static Logger logger = Logger.getLogger(TestSys.class);

    @Test
    public void test1() throws IOException {
        Map<String, String> env = System.getenv();
        for(String name : env.keySet()){
            System.out.println(name+"---------->"+env.get(name));
        }
        //获取指定环境变量的值
        logger.info(System.getenv("JAVA_HOME"));
        //获取所有的系统属性
        Properties props = System.getProperties();
        //将所有属性保存到props.txt文件中
//        props.store(new FileOutputStream("props.txt")
//                ,"System Properties");

        //输出特定的系统属性
        logger.info(System.getProperty("os.name"));
    }

    @Test
    public void runTimeTest(){
        //获取java程序关联的运行时对象
        Runtime rt = Runtime.getRuntime();
        logger.info("处理器数量："+rt.availableProcessors());
        logger.info("空闲内存数："+rt.freeMemory());
        logger.info("总内存数："+rt.totalMemory());
        logger.info("可用最大内存数："+rt.maxMemory());
    }

    /**
     * 使用RunTime类直接单独启动一个进程开运行操作系统的命令
     */
    @Test
    public void execTest() throws IOException {

        Runtime rt = Runtime.getRuntime();
        //运行记事本程序
        rt.exec("notepad.exe");
    }
}

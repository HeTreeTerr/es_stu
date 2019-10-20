package com.hss.book;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * java垃圾回收机制
 */
public class TestGc {

    private final static Logger logger = Logger.getLogger(TestGc.class);

    @Test
    public void test1(){

        //创建一个字符串对象
        String str = new String("疯狂动物城");
        //创建一个弱引用，让此弱引用引用到"疯狂动物城"字符串
        WeakReference wr = new WeakReference(str);
        //切断str引用和"疯狂java讲义"字符串之间的引用
        str = null;
        //取出弱引用所引用的对象
        logger.info(wr.get());
        //强制垃圾回收
        System.gc();
        System.runFinalization();
        //再次取出弱引用所引用的对象
        logger.info(wr.get());
    }
}

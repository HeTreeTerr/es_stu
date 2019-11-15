package com.hss.book;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * java字符串
 *  String,
 *  StringBuilder,
 *  StringBuffer
 */
public class TestStr {

    private final static Logger logger = Logger.getLogger(TestStr.class);

    @Test
    public void stringBuilderTest(){
        //StringBuffer是线程安全的，而StringBuilder性能略高。一般推荐使用StringBuilder
        StringBuilder sb = new StringBuilder();
        //追加字符串
        sb.append("java");
        //插入
        sb.insert(0,"hello ");
        //替换
        sb.replace(5,6,",");
        //删除
        sb.delete(5,6);
        logger.info("字符集为="+sb);
        //反转
        sb.reverse();
        logger.info("反转后="+sb);
        logger.info("长度="+sb.length());
        //容量
        logger.info("容量="+sb.capacity());
        sb.setLength(5);
        logger.info("裁剪后"+sb);
    }
}

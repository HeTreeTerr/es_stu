package com.hss.book;

import com.hss.Enum.GenderEnum;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 枚举测试类
 */
public class TestEnum {

    private final static Logger logger = Logger.getLogger(TestEnum.class);

    @Test
    public void test(){

        logger.info("男："+GenderEnum.MAN.getCode());
        logger.info("女："+GenderEnum.WOMEN.getCode());
    }
}

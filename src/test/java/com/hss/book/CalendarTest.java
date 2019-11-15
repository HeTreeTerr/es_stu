package com.hss.book;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.time.*;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * 时间工具类测试
 */
public class CalendarTest {

    private final static Logger logger = Logger.getLogger(CalendarTest.class);

    @Test
    public void test1(){
        Calendar c = Calendar.getInstance();
        //取出年
        logger.info("年="+c.get(YEAR));
        //取出月
        logger.info("月="+c.get(MONTH));
        //取出日
        logger.info("日="+c.get(DATE));
        //分别设置年、月、日、小时、分钟、秒
        //Month字段代表月份，月份的起始值不是1而是0
        c.set(2003,10,23,12,32,23);
        //将Calendar的年前推一年
        c.add(YEAR,-1);
        //将Calendar的月前推8个月
        c.roll(MONTH,-8);
        logger.info(c.getTime());
        /*
        add和roll的区别：
        add当被修改的字段超出它允许的范围时，可以向上和向下自动补位
        roll可以向下补位，但上层字段不会增加
         */
    }

    @Test
    public void test2(){
        //calendar容错性
        Calendar cal = Calendar.getInstance();
        //结果YEAR字段加一，MONTH字段为1（2月）
        cal.set(MONTH,13);
        logger.info("time="+cal.getTime());
        //关闭容错性
        cal.setLenient(false);
        //导致运行时异常
        cal.set(MONTH,13);
        logger.info(cal.getTime());
    }

    @Test
    public void test3(){
        //延迟修改
        Calendar cal = Calendar.getInstance();
        cal.set(2003,7,31);//2003-8-31
        //将月份设为9，但9月31日不存在
        //如果立即修改，系统会把cal自动调整到10月1日
        cal.set(MONTH,8);
        //logger.info(cal.getTime());
        cal.set(DATE,5);
        logger.info(cal.getTime());
    }

    @Test
    public void clockTest(){
        //----------Clock的用法------------
        //获取当前Clock
        Clock clock = Clock.systemUTC();
        //通过Clock获取当前时刻
        logger.info("当前时刻为："+clock.instant());
        //获取clock对应的毫秒数，与System.currentTimeMillis()输出相同
        logger.info(clock.millis());
        logger.info(System.currentTimeMillis());
        //---------下面是关于Duration的用法----------
        Duration d = Duration.ofSeconds(6000);
        logger.info("6000秒相当于"+d.toMinutes()+"分");
        logger.info("6000秒相当于"+d.toHours()+"小时");
        logger.info("6000秒相当于"+d.toDays()+"天");
        //在clock基础上增加6000秒，返回新的Clock
        Clock clock2 = Clock.offset(clock,d);
        //可以看到clock2与clock1相差1小时40分
        logger.info("当前时刻加6000秒为："+clock2.instant());
        //-------------下面是关于Instant的用法------------
        //获取当前时间
        Instant instant = Instant.now();
        logger.info("instant="+instant);
        //instant添加6000秒（即100分钟），返回新的Instant
        Instant instant2 = instant.plusSeconds(6000);
        logger.info(instant2);
        //根据字符串解析Instant对象
        Instant instant3 = Instant.parse("2014-02-23T10:12:35.342Z");
        logger.info("instant3="+instant3);
        Instant instant4 = instant3.plus(Duration.ofHours(5).plusMinutes(4));
        logger.info("instant4="+instant4);
        //获取instant4的5天以前的时刻
        Instant instant5 = instant4.minus(Duration.ofDays(5));
        logger.info("instant5="+instant5);
        //-------------下面是关于LocalDate的用法----------
        LocalDate localDate = LocalDate.now();
        logger.info("localDate="+localDate);
        //设置2014年的第146天
        localDate = LocalDate.ofYearDay(2014,146);
        logger.info("2014年的第146天="+localDate);
        //设置为2014年5月21日
        localDate = LocalDate.of(2014,Month.MAY,21);
        logger.info("2014年5月21日="+localDate);
        //-------------下面是关于LocalTime的用法-----------
        //获取当前时间
        LocalTime localTime = LocalTime.now();
        logger.info("当前时间="+localTime);
        //设置为22点33分
        localTime = LocalTime.of(22,33);
        logger.info("22点33分="+localTime);
        //返回一天中的第5503秒
        localTime = LocalTime.ofSecondOfDay(5503);
        logger.info("一天中的第5503秒="+localTime);
        //-------------下面是关于localDateTime的用法--------------
        //获取当前日期、时间
        LocalDateTime localDateTime = LocalDateTime.now();
        //当前日期、时间加上25小时3分钟
        LocalDateTime future = localDateTime.plusHours(25).plusMinutes(3);
        logger.info("当前日期、时间的20小时3分之后："+future);
        //----------下面是关于Year、YearMonth、MonthDay的用法示例--------
        Year year = Year.now();//获取当前的年份
        logger.info("当前年份："+year);
        year = year.plusYears(5);//当前年份再加5年
        logger.info("当前年份再过5年："+year);
        //根据指定月份获取YearMonth
        YearMonth ym = year.atMonth(10);
        logger.info("year年10月："+ym);
        //当前年月再加5年、减3个月
        ym = ym.plusYears(5).minusMonths(3);
        logger.info("year年10月再加5年、减3个月："+ym);
        MonthDay md = MonthDay.now();
        logger.info("当前月日："+md);//输出--XX-XX，代表几月几日
        //设置为5月23日
        MonthDay md2 = md.with(Month.MAY).withDayOfMonth(23);
        logger.info("5月23日为："+md2);
    }

    /**
     * 正则表达式
     */
    @Test
    public void findGroup(){
        //使用字符串模拟从网络上得到的页面源码
        String str = "我的手机号是18837112195，曾经用过18888888888，还用过18812345678";
        //创建一个Pattern对象，并且用它建立一个Matcher对象
        //该正则表达式只抓去13X和15X段的手机号
        //实际要抓取哪些电话号码，只要修改正则表达式即可
        Matcher m = Pattern.compile("1[35789]\\d{9}").matcher(str);
        //将所有符合正则表达的子串（电话号码）全部输出
        while (m.find()){
            logger.info(m.group());
        }
    }
}

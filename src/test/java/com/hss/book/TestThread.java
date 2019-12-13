package com.hss.book;

import com.hss.Thread.DaemonThread;
import com.hss.Thread.JoinThread;
import com.hss.Thread.PriorityThread;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 线程测试
 */
public class TestThread{

    private final static Logger logger = Logger.getLogger(TestThread.class);

    @Test
    public void testJoin() throws InterruptedException {
        //启动子线程
        new JoinThread("新线程").start();
        for(int i = 0;i < 100;i++){
            if(i == 20){
                JoinThread jt = new JoinThread("被Join的线程");
                jt.start();
                //main线程调用了jt线程的join()方法，main线程
                //必须等待jt执行结束才会向下执行
                jt.join();
            }
            logger.info(JoinThread.currentThread().getName()+" "+i);
        }
    }

    @Test
    public void testDaemon(){
        DaemonThread t = new DaemonThread();
        //将此线程设置成后台线程
        t.setDaemon(true);
        //启动后台线程
        t.start();
        for(int i = 0;i < 10;i++){
            logger.info(Thread.currentThread().getName()+" "+i);
        }
        //---------程序执行到此处，前台线程（main线程）结束---------
        //后台线程也应该随之结束
    }

    @Test
    public void testPriority(){

        //改变主线程的优先级
        Thread.currentThread().setPriority(6);
        for(int i = 0;i < 30;i++){
            if(i == 10){
                PriorityThread low = new PriorityThread("低级");
                low.start();
                logger.info("创建之初的优先级："+low.getPriority());
                //设置该线程为最低优先级
                low.setPriority(Thread.MIN_PRIORITY);
            }
            if(i == 20){
                PriorityThread high = new PriorityThread("高级");
                high.start();
                logger.info("创建之初的优先级："+high.getPriority());
                //设定该线程为最高优先级
                high.setPriority(Thread.MAX_PRIORITY);
            }
        }
    }
}

package com.hss.Thread;

import org.apache.log4j.Logger;

/**
 * 守护线程
 */
public class DaemonThread extends Thread{

    private final static Logger logger = Logger.getLogger(DaemonThread.class);

    //定义后台线程的线程执行体与普通线程没有任何区别
    public void run(){
        for(int i = 0;i < 1000; i++){
            logger.info(getName()+" "+i);
        }
    }
}

package com.hss.Thread;

import org.apache.log4j.Logger;

/**
 * 线程优先级
 */
public class PriorityThread extends Thread{

    private final static Logger logger = Logger.getLogger(PriorityThread.class);

    //定义一个有参数的构造器，用于创建线程是指定name
    public PriorityThread(String name){
        super(name);
    }

    public void run(){
        for(int i = 0;i < 50;i++){
            logger.info(getName()+",其优先级是："+getPriority()
                    +",循环变量的值为："+i);
        }
    }
}

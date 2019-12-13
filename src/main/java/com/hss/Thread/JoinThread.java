package com.hss.Thread;

import org.apache.log4j.Logger;

public class JoinThread extends Thread {

    private final static Logger logger = Logger.getLogger(JoinThread.class);

    //提供一个有参数的构造器，用于设置该线程的名字
    public JoinThread(String name){
        super(name);
    }

    //重写run()方法，定义线程执行体
    public void run(){
        for(int i = 0;i < 100;i++){
            logger.info(getName()+" "+i);
        }
    }


}

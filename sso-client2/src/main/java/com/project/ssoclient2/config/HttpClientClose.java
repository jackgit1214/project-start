//package com.example.ssoclient2.config;
//
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.pool.PoolStats;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PreDestroy;
//
//@Component    //交给spring容器管理
//public class HttpClientClose extends Thread {
//
//    @Autowired
//    private PoolingHttpClientConnectionManager manage;
//    private volatile boolean shutdown;    //开关 volatitle表示多线程可变数据,一个线程修改,其他线程立即修改
//
//    public HttpClientClose() {
//        ///System.out.println("执行构造方法,实例化对象");
//        //线程开启启动
//        this.start();
//    }
//
//
//    @Override
//    public void run() {
//        try {
//            //如果服务没有关闭,执行线程
//            while (!shutdown) {
//                synchronized (this) {
//                    wait(5000);            //等待5秒
//                    //System.out.println("线程开始执行,关闭超时链接");
//                    //关闭超时的链接
//                    PoolStats stats = manage.getTotalStats();
//                    int av = stats.getAvailable();    //获取可用的线程数量
//                    int pend = stats.getPending();    //获取阻塞的线程数量
//                    int lea = stats.getLeased();    //获取当前正在使用的链接数量
//                    int max = stats.getMax();
//                    //System.out.println("max/"+max+":    av/"+av+":  pend/"+pend+":   lea/"+lea);
//                    manage.closeExpiredConnections();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//
//        super.run();
//    }
//
//    //关闭清理无效连接的线程
//    @PreDestroy    //容器关闭时执行该方法.
//    public void shutdown() {
//        shutdown = true;
//        synchronized (this) {
//            //System.out.println("关闭全部链接!!");
//            notifyAll(); //全部从等待中唤醒.执行关闭操作;
//        }
//    }
//}
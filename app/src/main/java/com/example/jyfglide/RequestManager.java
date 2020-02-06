package com.example.jyfglide;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 队列管理类
 * 相当于饭店的老板
 * 1：租店面（框架） 2：一堆桌子（管理队列）3：凳子（队列）4：人（单个请求）
 */
public class RequestManager {

    private static RequestManager requestManager = new RequestManager();

    /**
     * 相当于开业的时候，就让所有的服务员工作起来！
     */
    private RequestManager(){
        start();
    }

    /**
     * 单例方法
     * @return
     */
    public static RequestManager getInstance() {
        return requestManager;
    }

    // 创建队列
    private LinkedBlockingDeque<BitmapRequest> requestQueue = new LinkedBlockingDeque<>();

    /**
     * 提供添加请求的方法
     * 相当于顾客来了怎么排号等待
     * @param bitmapRequest
     */
    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) return;

        if (!requestQueue.contains(bitmapRequest)) {
            requestQueue.add(bitmapRequest);
        }
    }

    /**
     * 创建服务员（任务窗口）数组
     */
    private BitmapDispatcher[] bitmapDispatchers;

    /**
     * 创建并执行任务
     * 相当于服务员怎么去安排顾客就餐
     */
    private void createAndStartAllDispatchers() {

        // 获取线程最大队列数
        int threadCount = Runtime.getRuntime().availableProcessors();
        // 初始化任务窗口数组
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        // for循环创建任务窗口并执行Thread任务
        for (int i = 0; i < threadCount; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();
            // 执行完毕 再把每一个任务窗口都设置进去
            bitmapDispatchers[i] = bitmapDispatcher;
        }
    }

    /**
     * 停止所有任务
     */
    private void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

    /**
     * 让所有的任务开始的方法
     */
    private void start() {
        stop();
        createAndStartAllDispatchers();
    }
}

package com.kri.factory;


/*
 * @创建者     LuLiLi
 * @创建时间   2016/2/3 11:20
 * @描述	      ${线程池代理工厂，简化调用，分出普通线程池和下载线程池两部分}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class ThreadPoolProxyFactory {
    private static ThreadPoolProxy mNormalThreadPoolProxy;
    private static ThreadPoolProxy mDownLoadingThreadPoolProxy;

    /**
     * 返回普通的线程池
     * @return
     */
    public static ThreadPoolProxy getmNormalThreadPoolProxy(){
        if (mNormalThreadPoolProxy==null){
            synchronized (ThreadPoolProxyFactory.class){
                if (mNormalThreadPoolProxy==null){
                   mNormalThreadPoolProxy= new ThreadPoolProxy(5,5,3000);
                }
            }
        }

        return mNormalThreadPoolProxy;
    }

    /**
     * 返回用于下载的线程池
     * @return
     */
    public static ThreadPoolProxy getmDownLoadingThreadPoolProxy(){
        if (mDownLoadingThreadPoolProxy==null){
            synchronized (ThreadPoolProxyFactory.class){
                if (mDownLoadingThreadPoolProxy==null){
                    mDownLoadingThreadPoolProxy=new ThreadPoolProxy(3,3,3000);
                }
            }
        }

        return mDownLoadingThreadPoolProxy;
    }
}

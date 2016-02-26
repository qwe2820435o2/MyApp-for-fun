package com.kris.widget;

import com.kri.factory.ThreadPoolProxyFactory;
import com.kri.utils.LogUtils;
import com.kri.utils.UIUtils;
import com.kris.app.R;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;


/*
 * @创建者     LuLiLi
 * @创建时间   2016/2/3 13:39
 * @描述	      ${提供视图，4种视图（空、加载中、错误、成功）中的其中一种}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public abstract class LoadingPageController extends FrameLayout {

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mSuccessView;

    //用变量来标明状态，以便控制
    private static final int STATE_LOADING=0;
    private static final int STATE_EMPTY=1;
    private static final int STATE_ERROR=2;
    private static final int STATE_SUCCESS=3;

    private int curState=STATE_LOADING;
    private LoadDataTask mLoadDataTask;

    public LoadingPageController(Context context) {
        super(context);
        initCommenViews();
    }

    /**
     * 初始化常规视图，空页面、错误页面、加载页面
     */
    private void initCommenViews() {
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);

        this.addView(mEmptyView);
        this.addView(mErrorView);
        this.addView(mLoadingView);

        refreshPageByState();
    }

    /**
     * 根据状态来显示或隐藏页面
     */
    private void refreshPageByState() {
        mLoadingView.setVisibility((curState == STATE_LOADING) ? View.VISIBLE : View.GONE);

        mEmptyView.setVisibility((curState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        mErrorView.setVisibility((curState == STATE_ERROR) ? View.VISIBLE : View.GONE);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //触发加载数据
                triggerLoadData();
            }
        });

        if (mSuccessView==null & curState==STATE_SUCCESS){
            mSuccessView=initSuccessView();
            this.addView(mSuccessView);
        }

        if (mSuccessView!=null){
            mSuccessView.setVisibility((curState==STATE_SUCCESS)?View.VISIBLE:View.GONE);
        }
    }

    /**
     * 交由子类去实现,完成成功视图地加载
     * @return
     */
    public abstract View initSuccessView();

    /**
     * 交由子类去实现，在子线程中加载数据，得到数据
     * @return
     */
    public abstract LoadResultState initData();

    /**
     * 限定加载后的返回值，只能是成功、错误、空
     */
    public enum LoadResultState{
        SUCCESS(STATE_SUCCESS),EMPTY(STATE_EMPTY),ERROR(STATE_ERROR);

        private LoadResultState(int state){
            this.state=state;
        }

        int state;

        public int getState(){
            return state;
        }


    }

    class LoadDataTask implements Runnable{

        @Override
        public void run() {
            LoadResultState resultState = initData();
            LogUtils.d("U8SDK", "进resultState触发加载:"+resultState);
            curState = resultState.getState();
            
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                	
                    //更新ui
                	refreshPageByState();
                }
            });

            mLoadDataTask=null;
        }
    }

    /**
     * 触发加载数据，外界希望触发加载数据就调这个方法
     */
    public void triggerLoadData() {
    	//加载不成功，没有正在加载的时候可以进来
        if (curState!=STATE_SUCCESS && mLoadDataTask==null){
            curState=STATE_LOADING;
            LogUtils.d("U8SDK", "进入触发加载");
            //重新加载视图
            refreshPageByState();

            //异步加载数据
            mLoadDataTask = new LoadDataTask();

            ThreadPoolProxyFactory.getmNormalThreadPoolProxy().execute(mLoadDataTask);

        }
    }
}

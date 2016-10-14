package com.nd.river.customrefreshlistview.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nd.river.customrefreshlistview.R;

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private Context mContext;
    private View footerView;
    private OnRefreshListener mOnRefreshListener;

    private boolean isScrollToBottom;
    private boolean isLoadingMore;
    private int firstVisibleItem;

    private boolean isFooterEnable;
    private String mLoadMoreText;
    private float mLoadMoreTextSize;
    private int mLoadMoreTextColor;
    private int mLoadMoreBarWidth;
    private int mLoadMoreBarHeight;

    public RefreshListView(Context context) {
        super(context);
        initParams(context);
        initViews();
        initEvents();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        initParams(context);
        initViews();
        initEvents();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        initParams(context);
        initViews();
        initEvents();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshListView);
        isFooterEnable = ta.getBoolean(R.styleable.RefreshListView_isFooterEnable, true);

        mLoadMoreText = ta.getString(R.styleable.RefreshListView_loadMoreText);
        mLoadMoreTextSize = ta.getDimensionPixelSize(R.styleable.RefreshListView_loadMoreTextSize
                , getResources().getDimensionPixelSize(R.dimen.sixteen_text_size));
        mLoadMoreTextColor = ta.getColor(R.styleable.RefreshListView_loadMoreTextColor
                , ContextCompat.getColor(context, R.color.dimGray));

        mLoadMoreBarWidth = (int) ta.getDimension(R.styleable.RefreshListView_loadMoreBarWidth
                , getResources().getDimension(R.dimen.pb_load_more_size));
        mLoadMoreBarHeight = (int) ta.getDimension(R.styleable.RefreshListView_loadMoreBarHeight
                , getResources().getDimension(R.dimen.pb_load_more_size));
        ta.recycle();
    }

    private void initParams(Context context) {
        mContext = context;
        isLoadingMore = false;
        if (null == mLoadMoreText || mLoadMoreText.equals("")) {
            mLoadMoreText = getResources().getString(R.string.load_more_label);
        }
    }

    private void initViews() {
        if (isFooterEnable) {
            LinearLayout footerParent = new LinearLayout(this.getContext());
            footerParent.setGravity(Gravity.CENTER);
            footerView = customFooterView();
            footerParent.addView(footerView);
            this.addFooterView(footerParent);
        }
    }

    private View customFooterView() {
        //消除Header,Footer的View.GONE空白问题
        LinearLayout footerLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT);
        footerLayout.setLayoutParams(params);
        footerLayout.setGravity(Gravity.CENTER);
        footerLayout.setPadding(0, 15, 0, 15);

        ProgressBar loadingBar = new ProgressBar(mContext);
        LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(mLoadMoreBarWidth, mLoadMoreBarHeight);
        progressBarParams.rightMargin = (int) getResources().getDimension(R.dimen.common_ten_dp);
        loadingBar.setLayoutParams(progressBarParams);
        loadingBar.setIndeterminate(true);
        Drawable drawable = new ProgressBar(mContext).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(mLoadMoreTextColor, PorterDuff.Mode.SRC_IN);
        loadingBar.setIndeterminateDrawable(drawable);

        TextView loadingText = new TextView(mContext);
        loadingText.setText(mLoadMoreText);
        loadingText.setTextColor(mLoadMoreTextColor);
        loadingText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadMoreTextSize);

        footerLayout.addView(loadingBar);
        footerLayout.addView(loadingText);

        return footerLayout;
    }

    private void initEvents() {
        setOnScrollListener(this);
    }

    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && firstVisibleItem != 0) {
            if (isFooterEnable && isScrollToBottom && !isLoadingMore) {

                isLoadingMore = true;
                this.setSelection(this.getCount() - 1);

                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadingMore();
                }
            }
        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem 当前屏幕显示在顶部的item的position
     * @param visibleItemCount 当前屏幕显示了多少个条目的总数
     * @param totalItemCount   ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

        if (isFooterEnable && footerView != null) {
            //判断可视Item是否能在当前页面完全显示(去除footView)
            if (visibleItemCount == totalItemCount) {
                footerView.setVisibility(View.GONE);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }

            if (getLastVisiblePosition() == totalItemCount - 1) {
                isScrollToBottom = true;
            } else {
                isScrollToBottom = false;
            }
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView(boolean isFooterEnable) {
        isLoadingMore = false;
        footerView.setVisibility(View.GONE);
        this.isFooterEnable = isFooterEnable;
    }

    public interface OnRefreshListener {

        /**
         * 上拉加载更多
         */
        void onLoadingMore();
    }

    public void setFooterEnable(boolean footerEnable) {
        isFooterEnable = footerEnable;
    }
}
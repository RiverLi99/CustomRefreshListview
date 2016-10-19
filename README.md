# CustomRefreshListview

  An easy and customizable listview,which has useful load feature.
  How to use it?
  1、Set an adapter extends BaseAdapter an usual
  2、load more data
  for example:
  
  @Override
    public void onLoadingMore() {
        if (mDataCount++ > 2) {
            //没有更多数据
            Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
            mRefreshListView.hideFooterView(false, false);
        } else {
            getNewData(mAdapter.getDataList());
            mRefreshListView.hideFooterView(true, true);
        }
    }
    
  3、Method:hideFooterView()
  
  /**
     * 隐藏脚布局
     * true true:有新数据,并且还有未加载完的数据
     * false false:本次网络请求成功,没有更多数据,页面不需要刷新
     * true false:本次网络请求失败,页面不需要刷新,还有未加载完的数据
     *
     * @param isFooterEnable
     * @param isNotify
     */
    public void hideFooterView(boolean isFooterEnable, boolean isNotify) {
        isLoadingMore = false;
        footerView.setVisibility(View.GONE);
        this.isFooterEnable = isFooterEnable;
        //列表项数据有更新
        if (isNotify) {
            if (mInnerAdapter != null) {
                mInnerAdapter.notifyDataSetChanged();
            }
        }
    }
    
    4、customizable properties
    
    /**
     * 设置FootView是否可用
     */
    private boolean isFooterEnable;
    /**
     * 上拉加载提示的文字
     */
    private String mLoadMoreText;
    /**
     * 上拉加载提示的文字大小
     */
    private float mLoadMoreTextSize;
    /**
     * 上拉加载提示的文字颜色
     */
    private int mLoadMoreTextColor;
    /**
     * 上拉加载loading条的颜色
     */
    private int mLoadMoreBarColor;
    /**
     * 上拉加载loading条的宽度
     */
    private int mLoadMoreBarWidth;
    /**
     * 上拉加载loading条的高度
     */
    private int mLoadMoreBarHeight;

package com.nd.river.customrefreshlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nd.river.customrefreshlistview.adapter.HrProfileSearchAdapter;
import com.nd.river.customrefreshlistview.customview.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshListView.OnRefreshListener {

    private Context mContext;
    HrProfileSearchAdapter mAdapter;
    private RefreshListView mRefreshListView;

    private int mDataCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParams();
        initViews();
        initEvents();
    }

    private void initParams() {
        mContext = this;
        mDataCount = 0;
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefreshListView = (RefreshListView) this.findViewById(R.id.lv_test);
        mAdapter = new HrProfileSearchAdapter(mContext, getInitData());
        mRefreshListView.setAdapter(mAdapter);
    }

    private void initEvents() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRefreshListView.setOnRefreshListener(this);
    }

    private List<Integer> getInitData() {
        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(i);
        }
        return dataList;
    }

    private List<Integer> getNewData(List<Integer> dataList) {
        for (int i = 0; i < 10; i++) {
            dataList.add(i);
        }
        return dataList;
    }

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
}

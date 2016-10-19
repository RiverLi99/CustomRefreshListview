package com.nd.river.customrefreshlistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nd.river.customrefreshlistview.R;

import java.util.List;

/**
 * Created by RiverLi on 2016/10/12 0012.
 */
public class HrProfileSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mDataList;
    private LayoutInflater mInflater;

    public HrProfileSearchAdapter(Context context, List<Integer> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList != null ? mDataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hrprofile_item_search_employee, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.searchNameText.setText(position + "");
        return convertView;
    }

    public List<Integer> getDataList() {
        return mDataList;
    }

    public class ViewHolder {

        TextView searchNameText;

        public ViewHolder(View itemView) {
            searchNameText = (TextView) itemView.findViewById(R.id.tv_search_name);
        }
    }
}

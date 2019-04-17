package com.zing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zing.model.response.countryListResponse.Data;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends BaseAdapter {
    Context mContext;

    private List<Data> dataList = new ArrayList<>();

    public CountryAdapter(Context mContext) {


        this.mContext = mContext;
    }

    public void addAllData(List<Data> dataList) {
        this.dataList.clear();
        this.dataList.addAll( dataList );
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return dataList.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from( mContext )
                    .inflate( android.R.layout.simple_list_item_1, parent, false );

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById( android.R.id.text1 );

            convertView.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.itemView.setText( dataList.get( position ).getName() );


        return convertView;
    }

    private static class ViewHolder {
        private TextView itemView;
    }
}

package com.zing.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zing.R;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

/**
 * Created by savita on 9/4/18.
 */

public class ListPopupWindowAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<String> modelArrayList;

    public ListPopupWindowAdapter(Context mContext, ArrayList<String> modelArrayList) {
        this.mContext = mContext;
        this.modelArrayList = modelArrayList;
        AppTypeface.getTypeFace(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        holder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_listpopup, parent, false);
        holder.textView = convertView.findViewById(R.id.tvDefault);
        holder.textView.setTypeface(AppTypeface.avenieNext_medium);
        holder.textView.setText(modelArrayList.get(position));
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
    }
}
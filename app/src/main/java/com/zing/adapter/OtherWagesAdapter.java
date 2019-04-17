package com.zing.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zing.R;
import com.zing.model.response.paymentDetailResponse.Other;

import java.util.ArrayList;

/**
 * Created by abhishek on 5/7/18.
 */

public class OtherWagesAdapter extends RecyclerView.Adapter<OtherWagesAdapter.MyviewHolder> {

    private ClickListner clickListner;

    private Context context;
    private ArrayList<Other> otherList;

    public OtherWagesAdapter(Context context, ArrayList<Other> otherList) {
        this.context = context;
        this.otherList = otherList;
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item, parent,
                false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        holder.text.setText("$"+otherList.get(position).getOverTime());
    }

    @Override
    public int getItemCount() {
        return otherList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;

        public MyviewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        @Override
        public void onClick(View v) {
            if (clickListner != null) {
                clickListner.itemClicked(v, getLayoutPosition());
            }
        }
    }

    public void setClickListner(OtherWagesAdapter.ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void itemClicked(View view, int position);

        void itemLongClick(View view, int position);
    }
}

package com.zing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.activity.DashboardActivity;
import com.zing.model.FooterBean;
import com.zing.model.TimeBean;

import java.util.ArrayList;


public class FooterAdapter extends RecyclerView.Adapter<FooterAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<FooterBean> footerList;
    int pos;
    private ClickListner clickListner;

    public FooterAdapter(Context context, ArrayList<FooterBean> footerList) {
        this.context = context;
        this.footerList = footerList;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item, parent,
                false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {

        if (footerList.get(position).isSelected()) {
            holder.ivIcon.setImageResource(footerList.get(position).getIcon_selected());
        } else {
            holder.ivIcon.setImageResource(footerList.get(position).getIcon());
        }
    }

    @Override
    public int getItemCount() {
        return footerList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon;

        public MyviewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivIcon.setOnClickListener(this);
          /*  ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    footerList.get(getLayoutPosition()).setSelected(true);
                    footerList.get(pos).setSelected(false);
                    notifyDataSetChanged();
                    pos = getLayoutPosition();
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            if (clickListner != null) {
                clickListner.itemClicked(v, getLayoutPosition());
            }
        }
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void itemClicked(View view, int position);

        void itemLongClick(View view, int position);
    }
}

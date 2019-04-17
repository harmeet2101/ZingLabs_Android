package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zing.R;
import com.zing.model.response.StatsResponse.Badge;
import com.zing.util.AppTypeface;

import java.util.ArrayList;


public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Badge> list;

    public BadgesAdapter(Context context, ArrayList<Badge> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.badges_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvBadgeName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getBadgeImgUrl()).into(holder.ivBadge);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBadge;
        TextView tvBadgeName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvBadgeName = itemView.findViewById(R.id.tvBadgeName);
            ivBadge = itemView.findViewById(R.id.ivBadge);

            AppTypeface.getTypeFace(context);
            tvBadgeName.setTypeface(AppTypeface.avenieNext_regular);
        }
    }
}

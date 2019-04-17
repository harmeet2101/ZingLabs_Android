package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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


public class GridBadgesAdapter extends RecyclerView.Adapter<GridBadgesAdapter.MyViewHolder> {
    ArrayList<Badge> list;
    private Context context;

    public GridBadgesAdapter(Context context, ArrayList<Badge> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_badges_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvBadgeName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getBadgeImgUrl()).into(holder.ivBadge);

        if (list.get(position).getBadgeStatus().equalsIgnoreCase("1")) {
            holder.ivTick.setVisibility(View.VISIBLE);
        } else {
            holder.ivTick.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBadge, ivTick;
        TextView tvBadgeName, tvBadgeDescription;

        MyViewHolder(View itemView) {
            super(itemView);
            tvBadgeName = itemView.findViewById(R.id.tvBadgeName);
            ivBadge = itemView.findViewById(R.id.ivBadge);
            tvBadgeDescription = itemView.findViewById(R.id.tvBadgeDescription);
            ivTick = itemView.findViewById(R.id.ivTick);

            AppTypeface.getTypeFace(context);
            tvBadgeName.setTypeface(AppTypeface.avenieNext_regular);
            tvBadgeDescription.setTypeface(AppTypeface.avenieNext_regular);
        }
    }
}

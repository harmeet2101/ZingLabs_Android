package com.zing.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zing.R;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.response.BroadcastResponse.BroadcastList;
import com.zing.util.AppTypeface;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<BroadcastList> broadcastLists;

    public NotificationAdapter(Context context, FragmentInterface fragmentInterface,
                               ArrayList<BroadcastList> broadcastLists) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.broadcastLists = broadcastLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.broadcast_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvBroadcastHeading.setText(broadcastLists.get(position).getHeading());
        holder.tvBroadcastTime.setText(broadcastLists.get(position).getTime());
        holder.tvBroadcastMessage.setText(broadcastLists.get(position).getDescription());

        String imgUrl = broadcastLists.get(position).getImageUrl();
        if(imgUrl!=null && !imgUrl.equalsIgnoreCase("")) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .dontTransform().placeholder(R.drawable.oval)).into(holder.ivSenderImg);
        }

        if (broadcastLists.get(position).getOtherLink().equalsIgnoreCase("")) {
            holder.btnView.setVisibility(View.GONE);
        } else {
            holder.btnView.setText("View " + broadcastLists.get(position).getType());
        }

        if (broadcastLists.get(position).getIsMsgPersonal()==1) {
            holder.btnPersonalMessage.setVisibility(View.VISIBLE);
        } else {
            holder.btnPersonalMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return broadcastLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSenderImg;
        Button btnView, btnPersonalMessage;
        TextView tvBroadcastHeading, tvBroadcastTime, tvBroadcastMessage, tvUserName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvBroadcastHeading = itemView.findViewById(R.id.tvBroadcastHeading);
            tvBroadcastTime = itemView.findViewById(R.id.tvBroadcastTime);
            tvBroadcastMessage = itemView.findViewById(R.id.tvBroadcastMessage);
            ivSenderImg = itemView.findViewById(R.id.ivSenderImg);
            btnView = itemView.findViewById(R.id.btnView);
            btnPersonalMessage = itemView.findViewById(R.id.btnPersonalMessage);
            tvUserName = itemView.findViewById(R.id.tvUserName);

            AppTypeface.getTypeFace(context);
            tvBroadcastHeading.setTypeface(AppTypeface.avenieNext_demibold);
            btnPersonalMessage.setTypeface(AppTypeface.avenieNext_demibold);
            tvUserName.setTypeface(AppTypeface.avenieNext_bold);
            tvBroadcastTime.setTypeface(AppTypeface.avenieNext_medium);
            tvBroadcastMessage.setTypeface(AppTypeface.avenieNext_regular);
            btnView.setTypeface(AppTypeface.avenieNext_demibold);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = broadcastLists.get(getLayoutPosition()).getOtherLink();
                    if (!url.startsWith("https://") && !url.startsWith("http://")){
                        url = "http://" + url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(browserIntent);
                    }

                }
            });
        }
    }
}
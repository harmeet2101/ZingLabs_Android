package com.zing.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.activity.PreferenceCalenderActivity;
import com.zing.model.TimeBean;
import com.zing.model.request.TimePreferenceRequest.Preffered;
import com.zing.util.AppTypeface;

import java.sql.Time;
import java.util.ArrayList;


public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<TimeBean> days;
    private ClickListner clickListner;

    public WeekDaysAdapter(Context context, ArrayList<TimeBean> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent,
                false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        holder.tvDay.setText(days.get(position).getTime());

        if (days.get(position).isSelected()) {
            holder.llDay.setBackgroundColor(Color.BLUE);
            holder.tvDay.setTextColor(Color.WHITE);
        } else if (days.get(position).isAvailableSelected()) {
            holder.llDay.setBackgroundColor(context.getResources().getColor(R.color.page_background));
            holder.tvDay.setTextColor(Color.GRAY);
        } else {
            holder.llDay.setBackgroundColor(Color.WHITE);
            holder.tvDay.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay;
        LinearLayout llDay;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            llDay = itemView.findViewById(R.id.llDay);

            AppTypeface.getTypeFace(context);
            tvDay.setTypeface(AppTypeface.avenieNext_demibold);
            llDay.setOnClickListener(this);
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

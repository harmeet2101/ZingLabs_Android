package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.model.response.CalendarSlotResponse.Slot;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

/**
 * Created by savita on 9/4/18.
 */

public class WeekCalendarAdapter extends RecyclerView.Adapter<WeekCalendarAdapter.MyViewHolder> {
    //    private String[] list;
    private ArrayList<Slot> list;
    private Context context;
    private ClickListner clickListner;

    public WeekCalendarAdapter(Context context, ArrayList<Slot> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_calendar_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvWeek.setText(list.get(position).getSlot());
        if (list.get(position).isSelected()) {
            holder.tvWeek.setAlpha(1);
        } else {
            holder.tvWeek.setAlpha((float) 0.13);
        }

      /*  holder.tvWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != pos) {
                    list.get(position).setSelected(true);
                    list.get(pos).setSelected(false);

                    notifyDataSetChanged();
                    pos = position;
                } else {
                    list.get(position).setSelected(true);
                    notifyDataSetChanged();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    int pos = 0;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvWeek;
        LinearLayout llDay;

        MyViewHolder(final View itemView) {
            super(itemView);
            tvWeek = itemView.findViewById(R.id.tvWeek);
            llDay = itemView.findViewById(R.id.llDay);

            AppTypeface.getTypeFace(context);
            tvWeek.setTypeface(AppTypeface.avenieNext_regular);

            llDay.setOnClickListener(this);

        /*    tvWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() != pos) {
                        list.get(getLayoutPosition()).setSelected(true);
                        list.get(pos).setSelected(false);

                        notifyDataSetChanged();
                        pos = getLayoutPosition();
                    } else {
                        list.get(getLayoutPosition()).setSelected(true);
                        notifyDataSetChanged();
                    }
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
    }

}
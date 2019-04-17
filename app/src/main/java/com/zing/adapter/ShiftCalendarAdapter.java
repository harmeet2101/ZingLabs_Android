package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.fragment.CompleteShiftFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

public class ShiftCalendarAdapter extends RecyclerView.Adapter<ShiftCalendarAdapter.MyViewHolder> {
    private Context context;


    public ShiftCalendarAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ShiftCalendarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_custom_row_layout, parent, false);
        return new ShiftCalendarAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftCalendarAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTime;
        LinearLayout llShift;

        MyViewHolder(View itemView) {
            super(itemView);
//            tvDay = itemView.findViewById(R.id.tvDay);
//            tvTime = itemView.findViewById(R.id.tvTime);
//            llShift = itemView.findViewById(R.id.llShift);
//
//            AppTypeface.getTypeFace(context);
//
//            tvTime.setTypeface(AppTypeface.avenieNext_regular);
//            tvDay.setTypeface(AppTypeface.avenieNext_regular);
//
//            llShift.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Fragment fragment = CompleteShiftFragment.newInstance(recomendedShiftList.
//                                    get(getLayoutPosition()).getShiftId()
//                            ,recomendedShiftList.get(getLayoutPosition()).getStoreName()
//                            ,recomendedShiftList.get(getLayoutPosition()).getShiftStatus()
//                            ,recomendedShiftList.get(getLayoutPosition()) .getCheckInTime()
//                            ,recomendedShiftList.get(getLayoutPosition()).getCheckOutTime()
//                            ,recomendedShiftList.get(getLayoutPosition()).getEarningAmount()
//                    );
//                    fragmentInterface.fragmentResult(fragment, "+");
//                }
//            });
        }
    }
}
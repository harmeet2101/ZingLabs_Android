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
import com.zing.fragment.RateShiftFragment;
import com.zing.fragment.ShiftDialogFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.CalendarDataModel;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.util.AppTypeface;

import java.util.ArrayList;


public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<UpcomingShift> scheduledShiftList;

    public ShiftAdapter(Context context, FragmentInterface fragmentInterface,
                        ArrayList<UpcomingShift> scheduledShiftList) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.scheduledShiftList = scheduledShiftList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDay.setText(scheduledShiftList.get(position).getDay());
        if(scheduledShiftList.get(position).getShiftStatus().equalsIgnoreCase("NOSHOW"))
            holder.tvTime.setText("No Show");
        else
        holder.tvTime.setText(scheduledShiftList.get(position).getTimeSlot());
    }

    @Override
    public int getItemCount() {
        return scheduledShiftList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTime;
        LinearLayout llShift;

        MyViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTime = itemView.findViewById(R.id.tvTime);
            llShift = itemView.findViewById(R.id.llShift);

            AppTypeface.getTypeFace(context);

            tvTime.setTypeface(AppTypeface.avenieNext_regular);
            tvDay.setTypeface(AppTypeface.avenieNext_regular);

            llShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(scheduledShiftList.get(getLayoutPosition()).getShiftStatus().equalsIgnoreCase("ONGOING")){

                        Fragment fragment = RateShiftFragment.newInstance(scheduledShiftList.get(getLayoutPosition()).getShiftId()
                                ,scheduledShiftList.get(getLayoutPosition()).getStoreName()
                                ,scheduledShiftList.get(getLayoutPosition()).getCheckInTime()
                                ,scheduledShiftList.get(getLayoutPosition()).getCheckOutTime()
                                ,scheduledShiftList.get(getLayoutPosition()).getShiftStatus()
                                ,String.valueOf(scheduledShiftList.get(getLayoutPosition()).isOnBreak()),"calendar");
                        fragmentInterface.fragmentResult(fragment, "+");
                    }else {
                        Fragment fragment = ShiftDialogFragment.
                                newInstance("calendarWeek",
                                        scheduledShiftList.get(getLayoutPosition()).getShiftId(),
                                        scheduledShiftList.get(getLayoutPosition()).getDate(),
                                        scheduledShiftList.get(getLayoutPosition()).getDay(),
                                        scheduledShiftList.get(getLayoutPosition()).getExpectedEarning(),
                                        scheduledShiftList.get(getLayoutPosition()).getTimeSlot(),
                                        scheduledShiftList.get(getLayoutPosition()).getStoreName(),
                                        scheduledShiftList.get(getLayoutPosition()).getRole(),
                                        scheduledShiftList.get(getLayoutPosition()).getRelease(),
                                        scheduledShiftList.get(getLayoutPosition()).getShiftStatus(),
                                        new ArrayList<CalendarDataModel>());
                        fragmentInterface.fragmentResult(fragment, "+");
                    }

                }
            });
        }
    }
}
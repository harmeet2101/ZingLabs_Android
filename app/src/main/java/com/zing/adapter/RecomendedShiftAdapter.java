package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.fragment.ClaimShiftFragment;
import com.zing.fragment.ShiftDialogFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

/**
 * Created by savita on 5/11/18.
 */

public class RecomendedShiftAdapter extends RecyclerView.Adapter<RecomendedShiftAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<RecommendedShift> recomendedShiftList;

    public RecomendedShiftAdapter(Context context, FragmentInterface fragmentInterface,
                                  ArrayList<RecommendedShift> recomendedShiftList) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.recomendedShiftList = recomendedShiftList;
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
        holder.tvDay.setText(recomendedShiftList.get(position).getDay());
        holder.tvTime.setText(recomendedShiftList.get(position).getTimeSlot());
    }

    @Override
    public int getItemCount() {
        return recomendedShiftList.size();
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
                    Fragment fragment = ClaimShiftFragment.
                            newInstance("calendarWeek",
                                    recomendedShiftList.get(getLayoutPosition()).getShiftId(),
                                    recomendedShiftList.get(getLayoutPosition()).getDate(),
                                    recomendedShiftList.get(getLayoutPosition()).getDay(),
                                    recomendedShiftList.get(getLayoutPosition()).getExpectedEarning(),
                                    recomendedShiftList.get(getLayoutPosition()).getTimeSlot(),
                                    recomendedShiftList.get(getLayoutPosition()).getLocation(),
                                    recomendedShiftList.get(getLayoutPosition()).getRole(),
                                    recomendedShiftList.get(getLayoutPosition()).getRelease());
                    fragmentInterface.fragmentResult(fragment, "+");
                }
            });
        }
    }
}
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
import com.zing.fragment.ShiftDialogFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.model.response.paymentDetailResponse.IncludedShift;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

/**
 * Created by abhishek on 14/5/18.
 */

public class IncludedShiftAdapter extends RecyclerView.Adapter<IncludedShiftAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<UpcomingShift> includedShiftList;

    public IncludedShiftAdapter(Context context, FragmentInterface fragmentInterface, ArrayList<UpcomingShift> includedShiftList) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.includedShiftList = includedShiftList;
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
        holder.tvDay.setText(includedShiftList.get(position).getDay());
        holder.tvTime.setText(includedShiftList.get(position).getTimeSlot());
    }

    @Override
    public int getItemCount() {
        return includedShiftList.size();
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

           /* llShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = ShiftDialogFragment.newInstance("calendar", "");
                    fragmentInterface.fragmentResult(fragment,"");
                }
            });*/
        }
    }
}
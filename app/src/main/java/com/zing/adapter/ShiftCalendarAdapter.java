package com.zing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.model.response.shiftbydate.Shift;
import com.zing.util.AppTypeface;
import com.zing.util.SessionManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftCalendarAdapter extends RecyclerView.Adapter<ShiftCalendarAdapter.MyViewHolder> {

    private Context context;
    private List<Shift> shiftsList;

    public ShiftCalendarAdapter(Context context, List<Shift> shiftsList) {
        this.context = context;
        this.shiftsList = shiftsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_custom_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.tvEarningAmount.setText(""+shiftsList.get(position).getExpectedEarning());
//        holder.tvLocationDetail.setText(shiftsList.get(position).getLocation());
//
//        nextShiftId = session.getNextShift();
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date1 = format.parse(date);
//            String day_no = (String) DateFormat.format("dd", date1); // 20
//            String monthString = (String) DateFormat.format("MMM", date1); // Jun
//            day = (String) DateFormat.format("EEE", date1); // Thursday
//
//            tvDay.setText(monthString + " " + day_no + ", " + day);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String shift_id, date, day;
//         boolean isWithin24hrs = false;
//        try {
//
//            Date currentDate = new Date();
//            String pattern = "yyyy-MM-dd hh:mm a";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//            StringTokenizer tokenizer = new StringTokenizer(timeSlot, "-");
//            String startTime = tokenizer.nextToken();
//            String endTime = tokenizer.nextToken();
//
//            holder.tvStartTime.setText(startTime);
//            holder.tvEndTime.setText(endTime);
//
//            String mDateString = date + " " + startTime.toUpperCase();
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(currentDate);
//            cal.add(Calendar.DATE, 1);
//            Date nxtDate = cal.getTime();
//            String nextDateString = simpleDateFormat.format(nxtDate);
//            Date nextDate = simpleDateFormat.parse(nextDateString);
//            Date nextShiftDate = simpleDateFormat.parse(mDateString);
//
//            if (nextShiftDate.compareTo(nextDate) < 0) {
//                isWithin24hrs = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        session.setDialogData(from, shift_id, date, day, expectedEarning, timeSlot, location, role);
//
//        if (isWithin24hrs) {
//            holder.btnReleaseShift.setVisibility(View.GONE);
//        } else {
//            holder.btnReleaseShift.setVisibility(View.VISIBLE);
//        }
//        if (from.equalsIgnoreCase("home")) {
////            if (release.equalsIgnoreCase("0")) {
////                btnReleaseShift.setText("RELEASE SHIFT");
////            } else if (release.equalsIgnoreCase("1")) {
////                btnReleaseShift.setText("UNDO RELEASE");
////                btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
////            } else {
//            holder.btnReleaseShift.setVisibility(View.VISIBLE);
//            holder.btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
//            holder.btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
////            }
//        } else if (from.equalsIgnoreCase("calendar")) {
//            holder.btnReleaseShift.setVisibility(View.GONE);
//            holder.btnCallManager.setVisibility(View.GONE);
//        } else {
//            if (release.equalsIgnoreCase("0")) {
//
//                holder.btnReleaseShift.setText("RELEASE SHIFT");
//            } else if (release.equalsIgnoreCase("1")) {
//
//                holder.btnReleaseShift.setText("UNDO RELEASE");
//                holder.btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
//            } else {
//
//                holder.btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
//                holder.btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
//            }
//        }
//        if (shift_id.equalsIgnoreCase(nextShiftId)) {
//
//            holder.btnReleaseShift.setVisibility(View.VISIBLE);
//            holder.textviewshiftType.setText("Next Shift");
//            holder.btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
//            holder.btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
//        }
//
//
//        switch (shiftType) {
//            case "NOSHOW":
//                holder.textviewshiftType.setText("No Show");
//                holder.btnReleaseShift.setVisibility(View.GONE);
//                holder.rlDialog.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg));
//                holder.lay01.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
//                holder.lay02.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
//                holder.lay03.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
//                holder.lay04.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
//                break;
//
//            case "COMPLETED":
//                holder.textviewshiftType.setText("Completed Shift");
//                break;
//            case "UPCOMING":
//                holder.textviewshiftType.setText("Upcoming Shift");
//                break;
//        }

    }

    @Override
    public int getItemCount() {
        return shiftsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textviewshiftType)
        TextView textviewshiftType;
        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvStartTime)
        TextView tvStartTime;
        @BindView(R.id.tvEndTime)
        TextView tvEndTime;
        @BindView(R.id.lay01)
        LinearLayout lay01;
        @BindView(R.id.tvEarningAmount)
        TextView tvEarningAmount;
        @BindView(R.id.lay02)
        LinearLayout lay02;
        @BindView(R.id.tvLocationDetail)
        TextView tvLocationDetail;
        @BindView(R.id.lay03)
        LinearLayout lay03;
        @BindView(R.id.ivRectangle)
        ImageView ivRectangle;
        @BindView(R.id.ivDiamond)
        ImageView ivDiamond;
        @BindView(R.id.lay04)
        LinearLayout lay04;
        @BindView(R.id.rlDialog)
        LinearLayout rlDialog;
        @BindView(R.id.btnReleaseShift)
        Button btnReleaseShift;
        @BindView(R.id.btnCallManager)
        Button btnCallManager;
        @BindView(R.id.tvClose)
        TextView tvClose;
        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            AppTypeface.getTypeFace(context);
            tvClose.setTypeface(AppTypeface.avenieNext_regular);
            tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
            tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);

            btnReleaseShift.setTypeface(AppTypeface.avenieNext_demibold);
            btnCallManager.setTypeface(AppTypeface.avenieNext_demibold);

            textviewshiftType.setTypeface(AppTypeface.avenieNext_regular);
            tvDay.setTypeface(AppTypeface.avenieNext_medium);
            tvStartTime.setTypeface(AppTypeface.avenieNext_regular);
            tvEndTime.setTypeface(AppTypeface.avenieNext_regular);
        }
    }
}
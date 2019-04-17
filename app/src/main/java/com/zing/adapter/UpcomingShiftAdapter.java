package com.zing.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.fragment.ShiftDialogFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.CalendarDataModel;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingShiftAdapter extends RecyclerView.Adapter<UpcomingShiftAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<UpcomingShift> upcomingShiftsList;
    private ProgressDialog progressDialog;
    SessionManagement session;

    public UpcomingShiftAdapter(Context context, FragmentInterface fragmentInterface,
                                ArrayList<UpcomingShift> upcomingShiftsList) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.upcomingShiftsList = upcomingShiftsList;
        session = new SessionManagement(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_shift_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDay.setText(upcomingShiftsList.get(position).getDay());
        holder.tvTime.setText(upcomingShiftsList.get(position).getTimeSlot());
    }

    @Override
    public int getItemCount() {
        return upcomingShiftsList.size();
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
                public void onClick(View v) {
                    Fragment fragment = ShiftDialogFragment.newInstance("upcoming",
                            upcomingShiftsList.get(getLayoutPosition()).getShiftId(),
                            upcomingShiftsList.get(getLayoutPosition()).getDate(),
                            upcomingShiftsList.get(getLayoutPosition()).getDay(),
                            upcomingShiftsList.get(getLayoutPosition()).getExpectedEarning(),
                            upcomingShiftsList.get(getLayoutPosition()).getTimeSlot(),
                            upcomingShiftsList.get(getLayoutPosition()).getStoreName(),
                            upcomingShiftsList.get(getLayoutPosition()).getRole(),
                            upcomingShiftsList.get(getLayoutPosition()).getRelease(),upcomingShiftsList.get(getLayoutPosition()).getShiftStatus(), new ArrayList<CalendarDataModel>());
                    fragmentInterface.fragmentResult(fragment, "+");
                }
            });
        }
    }


    public void updateDataSource(ArrayList<UpcomingShift> upcomingShiftsList){
        this.upcomingShiftsList = upcomingShiftsList;
        notifyDataSetChanged();

    }
}
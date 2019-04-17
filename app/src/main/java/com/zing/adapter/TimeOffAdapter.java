package com.zing.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.DeleteBankRequest;
import com.zing.model.request.LeaveCancelRequest;
import com.zing.model.request.LeaveRequest;
import com.zing.model.response.AccountResponse.TimeOff;
import com.zing.model.response.StatsResponse.Badge;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhishek on 14/6/18.
 */

public class TimeOffAdapter extends RecyclerView.Adapter<TimeOffAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TimeOff> timeOffList;
    private ProgressDialog progressDialog;
    SessionManagement session;
    private TextView textView;
    private ClickListner clickListner;

    public TimeOffAdapter(Context context, ArrayList<TimeOff> timeOffList, TextView textView) {
        this.context = context;
        this.timeOffList = timeOffList;
        this.textView = textView;
        session = new SessionManagement(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeoff_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (timeOffList.get(position).getApproved().equalsIgnoreCase("0")) {
            holder.tvApproved.setText("Pending");
        }
        else if (timeOffList.get(position).getApproved().equalsIgnoreCase("2")) {
            holder.tvApproved.setText("Rejected");
            holder.tvCancel.setVisibility(View.GONE);
        } else {
            holder.tvApproved.setText("Approved");
            holder.tvCancel.setVisibility(View.GONE);
        }
        holder.tvStartDateDetail.setText(timeOffList.get(position).getStartDate());
        holder.tvEndDateDetail.setText(timeOffList.get(position).getEndDate());
    }

    @Override
    public int getItemCount() {
        return timeOffList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvApproved, tvCancel, tvStartDateDetail, tvStartDate, tvEndDate, tvEndDateDetail;

        MyViewHolder(View itemView) {
            super(itemView);
            tvApproved = itemView.findViewById(R.id.tvApproved);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            tvStartDateDetail = itemView.findViewById(R.id.tvStartDateDetail);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvEndDateDetail = itemView.findViewById(R.id.tvEndDateDetail);

            AppTypeface.getTypeFace(context);
            tvApproved.setTypeface(AppTypeface.avenieNext_medium);
            tvCancel.setTypeface(AppTypeface.avenieNext_medium);
            tvStartDateDetail.setTypeface(AppTypeface.avenieNext_regular);
            tvStartDate.setTypeface(AppTypeface.avenieNext_regular);
            tvEndDate.setTypeface(AppTypeface.avenieNext_regular);
            tvEndDateDetail.setTypeface(AppTypeface.avenieNext_regular);
            tvCancel.setOnClickListener(this);
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

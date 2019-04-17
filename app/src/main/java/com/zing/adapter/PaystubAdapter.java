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
import com.zing.fragment.PaystubDetailFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.response.PreviousPaystubResponse.PreviousPaystub;
import com.zing.util.AppTypeface;

import java.util.ArrayList;

/**
 * Created by savita on 13/4/18.
 */

public class PaystubAdapter extends RecyclerView.Adapter<PaystubAdapter.MyViewHolder> {
    private Context context;
    private FragmentInterface fragmentInterface;
    private ArrayList<PreviousPaystub> paystubList;

    public PaystubAdapter(Context context, FragmentInterface fragmentInterface, ArrayList<PreviousPaystub> paystubList) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        this.paystubList = paystubList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paystub_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEarning.setText(paystubList.get(position).getEarnings());
        holder.tvMonth.setText(paystubList.get(position).getSlot());

        if (paystubList.get(position).getStatus().equalsIgnoreCase("processing")) {
            holder.tvPaymentStatus.setText("PROCESSING");
            holder.tvPaymentStatus.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        } else if (paystubList.get(position).getStatus().equalsIgnoreCase("completed")) {
            holder.tvPaymentStatus.setText("COMPLETED");
            holder.tvPaymentStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            holder.tvPaymentStatus.setText("INCOMPLETE");
            holder.tvPaymentStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return paystubList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEarning, tvMonth, tvPaymentStatus;
        LinearLayout llPaystubLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            tvEarning = itemView.findViewById(R.id.tvEarning);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            llPaystubLayout = itemView.findViewById(R.id.llPaystubLayout);

            AppTypeface.getTypeFace(context);
            tvEarning.setTypeface(AppTypeface.avenieNext_regular);
            tvMonth.setTypeface(AppTypeface.avenieNext_regular);
            tvPaymentStatus.setTypeface(AppTypeface.avenieNext_medium);

            llPaystubLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = PaystubDetailFragment.newInstance(
                            paystubList.get(getLayoutPosition()).getPaymentId(),
                            paystubList.get(getLayoutPosition()).getEarnings(),
                            paystubList.get(getLayoutPosition()).getStatus(),
                            paystubList.get(getLayoutPosition()).getSlot());
                    fragmentInterface.fragmentResult(fragment,"");
                }
            });
        }
    }
}

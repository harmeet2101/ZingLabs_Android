package com.zing.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.activity.DashboardActivity;
import com.zing.activity.PreferenceCalenderActivity;
import com.zing.dragSelection.DragSelectTouchListener;
import com.zing.model.TimeBean;
import com.zing.model.request.TimePreferenceRequest.Available;
import com.zing.model.request.TimePreferenceRequest.Preffered;
import com.zing.model.request.TimePreferenceRequest.TimePreferenceRequest;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by savita on 26/3/18.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<TimeBean> time;
    private int pos;
    private static ArrayList<String> prefferedTimeSlot = new ArrayList<>();
    private static ArrayList<String> availableTimeSlot = new ArrayList<>();
    private Button btnDone;
    private ProgressDialog progressDialog;
    SessionManagement session;
    private static String day;
    private String from;
    String daysValue[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private ItemClickListener mClickListener;
    boolean isAvailable;

    public TimeAdapter(Context context, ArrayList<TimeBean> time, int position,
                       Button btnDone, String from) {
        this.context = context;
        this.time = time;
        this.pos = position;
        this.btnDone = btnDone;
        session = new SessionManagement(context);
        this.from = from;
//        mSelected = new HashSet<>();
//        mSelected1 = new HashSet<>();
        day = daysValue[pos];

        prefferedTimeSlot = new ArrayList<>();
        availableTimeSlot = new ArrayList<>();

        for (Preffered preffered : PreferenceCalenderActivity.prefferedList) {
            if (preffered.getDay().equalsIgnoreCase(day)) {
                prefferedTimeSlot = preffered.getTimeSlot();
            }
        }

        for (Available available : PreferenceCalenderActivity.availableList) {
            if (available.getDay().equalsIgnoreCase(day)) {
                availableTimeSlot = available.getTimeSlot();
            }
        }
       /* StringBuffer buffer = new StringBuffer("");
        for(TimeBean bean:time){

            buffer.append(bean.getTime()+"\n");
        }
        Toast.makeText(context,buffer.toString(),Toast.LENGTH_LONG).show();*/
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item, parent,
                false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        holder.tvTime.setText(time.get(position).getTime());

        String tValue = "";

        if (position == time.size() - 1) {
            holder.llAvailableBg.setVisibility(View.INVISIBLE);
            holder.llPrefferedBg.setVisibility(View.INVISIBLE);
            holder.view.setVisibility(View.GONE);
            tValue = "";
        } else {
            holder.llAvailableBg.setEnabled(true);
            holder.llPrefferedBg.setEnabled(true);
            holder.view.setVisibility(View.VISIBLE);

            tValue = time.get(position).getTime() + "-" + time.get(position + 1).getTime();
        }

        final String timeValue = tValue;

        if (!availableTimeSlot.contains(timeValue)) {
            holder.llAvailableBg.setBackgroundColor(Color.WHITE);
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.llAvailableBg.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
            holder.view.setVisibility(View.GONE);
        }

        if (!prefferedTimeSlot.contains(timeValue)) {
            holder.llPrefferedBg.setBackgroundColor(Color.WHITE);
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.llPrefferedBg.setBackgroundColor(Color.BLUE);
            holder.view.setVisibility(View.GONE);
            holder.llAvailableBg.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
        }

/*
        holder.llAvailableBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (availableTimeSlot.contains(timeValue)) {
                    holder.llAvailableBg.setBackgroundColor(Color.WHITE);
                    holder.view.setVisibility(View.VISIBLE);
                    availableTimeSlot.remove(timeValue);

                    holder.llPrefferedBg.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.llAvailableBg.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                    holder.view.setVisibility(View.GONE);
                    availableTimeSlot.add(timeValue);
                }
            }
        });

        holder.llPrefferedBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prefferedTimeSlot.contains(timeValue)) {
                    holder.llPrefferedBg.setBackgroundColor(Color.WHITE);
                    holder.view.setVisibility(View.VISIBLE);
                    prefferedTimeSlot.remove(timeValue);

                } else {
                    holder.llPrefferedBg.setBackgroundColor(Color.BLUE);
                    holder.view.setVisibility(View.GONE);
                    prefferedTimeSlot.add(timeValue);
                    if (!availableTimeSlot.contains(timeValue)) {
                        availableTimeSlot.add(timeValue);
                    }

                    holder.llAvailableBg.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                }
            }
        });
*/

        holder.llPrefferedBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAvailable = false;
                if (mClickListener != null)
                    mClickListener.onSlotItemClick(v, holder.getAdapterPosition(), timeValue);
            }
        });

        holder.llAvailableBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAvailable = true;
                if (mClickListener != null)
                    mClickListener.onSlotItemClick(v, holder.getAdapterPosition(), timeValue);
            }
        });

        holder.llAvailableBg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isAvailable = true;
                if (mClickListener != null)
                    return mClickListener.onSlotItemLongClick(v, holder.getAdapterPosition());
                return false;
            }
        });

        holder.llPrefferedBg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isAvailable = false;
                if (mClickListener != null)
                    return mClickListener.onSlotItemLongClick(v, holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return time.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        LinearLayout llAvailableBg, llPrefferedBg;
        View view, view1, view2;

        public MyviewHolder(View itemView) {
            super(itemView);

            AppTypeface.getTypeFace(context);
            tvTime = itemView.findViewById(R.id.tvTime);
            llAvailableBg = itemView.findViewById(R.id.llAvailableBg);
            llPrefferedBg = itemView.findViewById(R.id.llPrefferedBg);
            view = itemView.findViewById(R.id.view);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            tvTime.setTypeface(AppTypeface.avenieNext_demibold);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData();
                    setTimePreference();
                }
            });
        }

        private void setTimePreference() {
            progressDialog = CommonUtils.getProgressBar(context);
            ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

            try {

                TimePreferenceRequest timePreferenceRequest = new TimePreferenceRequest();
                timePreferenceRequest.setAvailable(PreferenceCalenderActivity.availableList);
                timePreferenceRequest.setPreffered(PreferenceCalenderActivity.prefferedList);

                Call<JsonElement> call = api.setPreferenceApi("Bearer " + session.getUserToken(),
                        timePreferenceRequest);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonElement> call,
                                           @NonNull Response<JsonElement> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());

                                JSONObject responseObj = jsonObject.optJSONObject("response");
                                String code = responseObj.optString("code");
                                String message = responseObj.optString("message");
                                if (code.equalsIgnoreCase("200")) {
                                   // CommonUtils.showSnackbar(tvTime, message);

                                    Intent intent = new Intent(context, DashboardActivity.class);
                                    intent.putExtra("from", from);
                                    if (from.equalsIgnoreCase("notification")){
                                        intent.putExtra("deeplink", "account");
                                    }
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                } else {
                                    CommonUtils.showSnackbar(btnDone, message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.showSnackbar(btnDone, response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        CommonUtils.showSnakBar(btnDone, t.getMessage());
                    }
                });
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

        }
    }

    public void toggleSelection(int pos, String timeValue) {
        if (isAvailable) {
            if (availableTimeSlot.contains(timeValue)) {
                availableTimeSlot.remove(timeValue);
                prefferedTimeSlot.remove(timeValue);
            } else
                availableTimeSlot.add(timeValue);
        } else {
            if (prefferedTimeSlot.contains(timeValue))
                prefferedTimeSlot.remove(timeValue);
            else {
                prefferedTimeSlot.add(timeValue);
                availableTimeSlot.add(timeValue);
            }
        }
        notifyItemChanged(pos);
    }

    String timeSlot;

    public void selectRange(int start, int end, boolean selected) {
        for (int i = start; i <= end; i++) {

            if (end == time.size() - 1) {
                timeSlot = "";
            } else
                timeSlot = time.get(start).getTime() + "-" + time.get(start + 1).getTime();

            if (selected) {
                if (isAvailable) {
                    availableTimeSlot.add(timeSlot);
                } else {
                    prefferedTimeSlot.add(timeSlot);
                    availableTimeSlot.add(timeSlot);
                }
            } else {
                if (isAvailable) {
                    availableTimeSlot.remove(timeSlot);
                    prefferedTimeSlot.remove(timeSlot);
                } else
                    prefferedTimeSlot.remove(timeSlot);
            }
        }
        notifyItemRangeChanged(start, end - start + 1);
    }

    public ArrayList<String> getSelection() {
        if (isAvailable)
            return availableTimeSlot;
        else
            return prefferedTimeSlot;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onSlotItemClick(View view, int position, String timeValue);

        boolean onSlotItemLongClick(View view, int position);
    }

    public static void setData() {
        if (prefferedTimeSlot != null && prefferedTimeSlot.size() > 0) {
            boolean pref = true;
            boolean avail = true;

            for (Preffered preffered : PreferenceCalenderActivity.prefferedList) {
                if (preffered.getDay() == day) {
                    preffered.setTimeSlot(prefferedTimeSlot);
                    pref = false;
                }
            }

            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(availableTimeSlot);
            availableTimeSlot.clear();
            availableTimeSlot.addAll(hashSet);

            for (Available available : PreferenceCalenderActivity.availableList) {
                if (available.getDay() == day) {
                    available.setTimeSlot(availableTimeSlot);
                    avail = false;
                }
            }

            if (pref) {
                Preffered preffered1 = new Preffered();
                preffered1.setDay(day);
                preffered1.setTimeSlot(prefferedTimeSlot);
                PreferenceCalenderActivity.prefferedList.add(preffered1);
            }

            if (avail) {
                Available available = new Available();
                available.setDay(day);
                available.setTimeSlot(availableTimeSlot);
                PreferenceCalenderActivity.availableList.add(available);
            }
        }
    }
}

package com.zing.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.fragment.HomeFragment;
import com.zing.fragment.VerifyOtpFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.request.RateShiftRequest;
import com.zing.model.request.ReleaseShiftRequest;
import com.zing.model.request.ShiftBreak;
import com.zing.model.response.breakShift.ShiftBreakResponse;
import com.zing.model.response.shiftbydate.Shift;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Shift> shiftsList;
    SessionManagement session;
    public FragmentInterface fragmentInterface;
    private static final int VIEW_TYPE_COMPLETED = 0;
    private static final int VIEW_TYPE_ONGOING = 1;
    private static final int VIEW_TYPE_NO_SHOW = 2;
    private static final int VIEW_TYPE_UPCOMING = 3;
    public ShiftCalendarAdapter(Context context, List<Shift> shiftsList,FragmentInterface fragmentInterface) {
        this.context = context;
        this.shiftsList = shiftsList;
        this.session = new SessionManagement(context);
        this.fragmentInterface = fragmentInterface;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder =null;
        View itemView= null;

        switch (viewType){

            case VIEW_TYPE_UPCOMING:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shift_custom_row_layout, parent, false);

                viewHolder = new UpcomingViewHolder(itemView);
                break;
            case VIEW_TYPE_NO_SHOW:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shift_custom_row_layout, parent, false);

                viewHolder = new NoShowViewHolder(itemView);
                break;
            case VIEW_TYPE_ONGOING:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rate_shift, parent, false);

                viewHolder = new OnGoingViewHolder(itemView);
                break;
            case VIEW_TYPE_COMPLETED:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.complete_shift, parent, false);
                viewHolder = new CompletedViewHolder(itemView);
                break;
        }
       return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = holder.getItemViewType();
        switch (viewType){

            case VIEW_TYPE_NO_SHOW:
                bindNoShowViewHolder(holder,shiftsList.get(position),position);
                break;

            case VIEW_TYPE_COMPLETED:
                bindCompletedViewHolder(holder,shiftsList.get(position),position);
                break;

            case VIEW_TYPE_UPCOMING:
                bindUpcomingViewHolder(holder,shiftsList.get(position),position);
                break;

            case VIEW_TYPE_ONGOING:
                bindOnGoingViewHolder(holder,shiftsList.get(position),position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return shiftsList.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (shiftsList.get(position).getShiftStatus()){

            case "NOSHOW":
                return  VIEW_TYPE_NO_SHOW;

            case "COMPLETED":
                return VIEW_TYPE_COMPLETED;

            case "UPCOMING":
                return VIEW_TYPE_UPCOMING;

            case "ONGOING":
                return VIEW_TYPE_ONGOING;

            default:
                return VIEW_TYPE_UPCOMING;
        }
    }

    class UpcomingViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private Dialog dialog;
        private String release;
        private String nextShiftId;
        private ProgressDialog progressDialog;
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
        Shift shf;

        @BindView(R.id.counterTextview)
        TextView counterTextview;

        @BindView(R.id.tvRoleDetail)
        TextView tvRoleDetail;
        private int PERMISSION = 0;
        UpcomingViewHolder(@NonNull View itemView) {
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

            btnCallManager.setOnClickListener(this);
            tvClose.setOnClickListener(this);
            btnReleaseShift.setOnClickListener(this);
            ivRectangle.setOnClickListener(this);
            ivDiamond.setOnClickListener(this);
        }


        public void setData(Shift shift,int position){

            this.shf = shift;

            counterTextview.setVisibility(View.VISIBLE);
            counterTextview.setText((position+1)+" of "+shiftsList.size());

            tvEarningAmount.setText(""+shift.getExpectedEarning());
            tvLocationDetail.setText(shift.getStoreName());
            tvRoleDetail.setText(shift.getRole());
            nextShiftId = session.getNextShift();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(shift.getDate());
                String day_no = (String) DateFormat.format("dd", date1); // 20
                String monthString = (String) DateFormat.format("MMM", date1); // Jun
                String day = (String) DateFormat.format("EEE", date1); // Thursday

                tvDay.setText(monthString + " " + day_no + ", " + day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean isWithin24hrs = false;
            try {

                Date currentDate = new Date();
                String pattern = "yyyy-MM-dd hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                StringTokenizer tokenizer = new StringTokenizer(shift.getTimeSlot(), "-");
                String startTime = tokenizer.nextToken();
                String endTime = tokenizer.nextToken();

                tvStartTime.setText(startTime);
                tvEndTime.setText(endTime);

                String mDateString = shift.getDate() + " " + startTime.toUpperCase();
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 1);
                Date nxtDate = cal.getTime();
                String nextDateString = simpleDateFormat.format(nxtDate);
                Date nextDate = simpleDateFormat.parse(nextDateString);
                Date nextShiftDate = simpleDateFormat.parse(mDateString);

                if (nextShiftDate.compareTo(nextDate) < 0) {
                    isWithin24hrs = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isWithin24hrs) {
                btnReleaseShift.setVisibility(View.GONE);
            } else {
                btnReleaseShift.setVisibility(View.VISIBLE);
            }


            if(shift.getRelease()==0 && isWithin24hrs){
                btnReleaseShift.setVisibility(View.GONE);
            }
            else if (shift.getRelease().toString().equalsIgnoreCase("0")) {

                btnReleaseShift.setText("RELEASE SHIFT");
            } else if (shift.getRelease().toString().equalsIgnoreCase("1")) {

                btnReleaseShift.setText("UNDO RELEASE");
                btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
            } else {

                btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
                btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }
            if (shift.getShiftId().equalsIgnoreCase(nextShiftId) && isWithin24hrs) {

                btnReleaseShift.setVisibility(View.VISIBLE);
                textviewshiftType.setText("Next Shift");
                btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
                btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }else if(!shift.getShiftId().equalsIgnoreCase(nextShiftId) && isWithin24hrs){

                btnReleaseShift.setVisibility(View.GONE);
            }else{
                btnReleaseShift.setVisibility(View.VISIBLE);
                textviewshiftType.setText("Upcoming Shift");
                btnReleaseShift.setText("RELEASE SHIFT");
                btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.black_btn));
            }

            textviewshiftType.setText("Upcoming Shift");

            if(btnReleaseShift.getText().toString().equalsIgnoreCase(context.getResources().
                    getString(R.string.check_in) )
                    &&shf.getAuto_checkin()){
                btnReleaseShift.setVisibility(View.GONE);
            }

            /*if (shf.getShiftId().equalsIgnoreCase(nextShiftId)) {

                btnReleaseShift.setVisibility(View.VISIBLE);
                textviewshiftType.setText("Next Shift");
                btnReleaseShift.setText(context.getResources().getString(R.string.check_in));
                btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }*/

        }
        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id){

                case  R.id.tvClose:
                    ((Activity)context).onBackPressed();
                    break;

                case R.id.btnCallManager:
                    String phone = session.getManagerNumber();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                            phone, null));
                    context.startActivity(intent);
                    break;

                case R.id.btnReleaseShift:
                    if (btnReleaseShift.getText().toString().equalsIgnoreCase("RELEASE SHIFT")) {
                        release = "1";
                        openDialog("Release Confirmation", "Once released the shift will be open for the rest of the team to claim.");
                    } else if (btnReleaseShift.getText().toString().equalsIgnoreCase("UNDO RELEASE")) {
                        release = "0";
                        if (NetworkUtils.isNetworkConnected(context)) {
                            releaseShift();
                        }
                    } else {
                        Fragment fragment = VerifyOtpFragment.newInstance(shf.getShiftId(), "Calendar");

                        fragmentInterface.fragmentResult(fragment, "");
                    }
                    break;

                case R.id.ivRectangle:
                   /* Intent LaunchIntent = context.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.calendar");
                    context.startActivity(LaunchIntent);*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.WRITE_CALENDAR)
                                        != PackageManager.PERMISSION_GRANTED) {


                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_CALENDAR,
                                            Manifest.permission.WRITE_CALENDAR},
                                    PERMISSION);
                        } else {
                            syncCalendar(shf);
                        }
                    } else {
                        syncCalendar(shf);
                    }
                    break;
                case R.id.ivDiamond:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (shf.getStoreName().equalsIgnoreCase("")) {
                                CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                            } else {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shf.getStoreName());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                context.startActivity(mapIntent);
                            }
                        }
                    }, 1000);
                    break;
            }
        }

        private void openDialog(String heading, String Description) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);

            TextView tvHeading = dialog.findViewById(R.id.tvHeading);
            TextView tvDescription = dialog.findViewById(R.id.tvDescription);
            TextView tvGoBack = dialog.findViewById(R.id.tvGoBack);
            Button btnYes = dialog.findViewById(R.id.btnYes);

            tvHeading.setText(heading);
            tvDescription.setText(Description);
            tvGoBack.setText(context.getResources().getString(R.string.cancel));
            btnYes.setText(context.getResources().getString(R.string.release));

            tvHeading.setTypeface(AppTypeface.avenieNext_medium);
            tvDescription.setTypeface(AppTypeface.avenieNext_medium);
            tvGoBack.setTypeface(AppTypeface.avenieNext_demibold);
            btnYes.setTypeface(AppTypeface.avenieNext_demibold);

            dialog.show();

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (NetworkUtils.isNetworkConnected(context)) {
                        releaseShift();
                    }
                }
            });

            tvGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        private void releaseShift() {

            progressDialog = CommonUtils.getProgressBar(context);
            ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
            try {
                ReleaseShiftRequest shiftCheckInRequest = new ReleaseShiftRequest();
                shiftCheckInRequest.setShiftId(shf.getShiftId());
                shiftCheckInRequest.setRelease(release);

                Call<JsonElement> call = api.releaseShiftApi("Bearer " + session.getUserToken(),
                        shiftCheckInRequest);
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

                                CommonUtils.showSnackbar(tvClose, message);

                                if (code.equalsIgnoreCase("200")) {

                                    if (release.equalsIgnoreCase("1")) {
                                        btnReleaseShift.setText(context.getString(R.string.undo_release));
                                        btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.blue));
                                    } else {
                                        btnReleaseShift.setText(context.getString(R.string.release_shift_caps));
                                        btnReleaseShift.setBackgroundColor(context.getResources().getColor(R.color.black_btn));
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.showSnackbar(tvClose, response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        CommonUtils.showSnakBar(tvClose, t.getMessage());
                    }
                });
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

        }

        private void syncCalendar(Shift shift) {
            StringTokenizer tokenizer = new StringTokenizer(shift.getTimeSlot(),"-");
            String startTime = tokenizer.nextToken().toUpperCase();
            String endTme = tokenizer.nextToken().toUpperCase();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(shift.getDate());
                long startDate = date1.getTime();

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                Date stestDate = sdf1.parse(shift.getDate()+" "+startTime);
                Date eTestDate = sdf1.parse(shift.getDate()+" "+endTme);
                Log.d("testDate: ",stestDate.toString()+" : "+eTestDate.toString());

                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(stestDate);
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(eTestDate);
                //addToCalendar(getActivity(), "shift Event", startDate, startDate);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, shift.getRole()+" shift - "+shift.getStoreName())
                        .putExtra(CalendarContract.Events.ALLOWED_REMINDERS,CalendarContract.Reminders.METHOD_ALERT)
                        .putExtra(CalendarContract.Events.HAS_ALARM,1)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_TENTATIVE);

                context.startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class OnGoingViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvEarningAmount)
        TextView tvEarningAmount;
        @BindView(R.id.tvLocationDetail)
        TextView tvLocationDetail;
        @BindView(R.id.ivRectangle)
        ImageView ivRectangle;
        @BindView(R.id.ivDiamond)
        ImageView ivDiamond;
        @BindView(R.id.rlDialog)
        ViewGroup rlDialog;
        @BindView(R.id.tvRateShift)
        TextView tvRateShift;
        Unbinder unbinder;
        @BindView(R.id.tvClose)
        TextView tvClose;
        @BindView(R.id.overflowImageview)
        ImageView overflowImageview;
        @BindView(R.id.tvStartTime)
        TextView tvStartTime;
        @BindView(R.id.tvEndTime)
        TextView tvEndTime;
        @BindView(R.id.textviewshiftType)
        TextView textviewshiftType;
        @BindView(R.id.btncheckoutShift)
        Button btncheckoutShift;
        @BindView(R.id.btnbreakShift)
        Button btnbreakShift;
        @BindView(R.id.checkinTime)
        TextView checkinTime;
        @BindView(R.id.checkoutTime)
        TextView checkoutTime;
        @BindView(R.id.counterTextview)
        TextView counterTextview;
        @BindView(R.id.tvRoleDetail)
        TextView tvRoleDetail;

        private  Shift shf;
        private String breakStatus;
        OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AppTypeface.getTypeFace(context);
            tvClose.setTypeface(AppTypeface.avenieNext_regular);
            tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
            tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);
            tvDay.setTypeface(AppTypeface.avenieNext_medium);
            tvRateShift.setTypeface(AppTypeface.avenieNext_medium);
            tvClose.setOnClickListener(this);
            ivRectangle.setOnClickListener(this);
            ivDiamond.setOnClickListener(this);
            btnbreakShift.setOnClickListener(this);
            btncheckoutShift.setOnClickListener(this);
        }

        protected void setData(Shift shift,int position){

            shf = shift;

            counterTextview.setVisibility(View.VISIBLE);
            counterTextview.setText((position+1)+" of "+shiftsList.size());

            tvEarningAmount.setText(/*"$" +*/ shift.getExpectedEarning());
            tvLocationDetail.setText(shift.getStoreName());
            checkinTime.setText(shift.getCheckinTime());
            checkoutTime.setText("-");
            textviewshiftType.setText("Ongoing Shift");
            tvRoleDetail.setText(shift.getRole());
            if(shift.getIsOnBreak().toString().equalsIgnoreCase("0")){
                btnbreakShift.setText("Take a break");
                breakStatus = "0";
            }else if(shift.getIsOnBreak().toString().equalsIgnoreCase("1")){

                btnbreakShift.setText("End break");
                breakStatus = "1";
            }

            boolean isMobileCheckedin = shift.getAuto_checkin();
            if(isMobileCheckedin){
                btnbreakShift.setVisibility(View.GONE);
                btncheckoutShift.setVisibility(View.GONE);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(shift.getDate());
                String day_no = (String) DateFormat.format("dd", date1); // 20
                String monthString = (String) DateFormat.format("MMM", date1); // Jun
                String day = (String) DateFormat.format("EEE", date1); // Thursday
                tvDay.setText(monthString + " " + day_no + ", " + day);
                StringTokenizer tokenizer = new StringTokenizer(shift.getTimeSlot(),"-");
                String startTime = tokenizer.nextToken();
                String endTime = tokenizer.nextToken();
                tvStartTime.setText(startTime);
                tvEndTime.setText(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tvClose:
                    ((Activity)context).onBackPressed();
                    break;
                case R.id.btncheckoutShift:
                        rateCompletedShift();
                    break;
                case R.id.ivRectangle:
                    Intent LaunchIntent = context.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.calendar");
                    context.startActivity(LaunchIntent);
                    break;
                case R.id.ivDiamond:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (shf.getStoreName().equalsIgnoreCase("")) {
                                CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                            } else {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shf.getStoreName());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                context.startActivity(mapIntent);
                            }
                        }
                    }, 1000);
                    break;


                case R.id.btnbreakShift:
                    if(breakStatus.equalsIgnoreCase("0")){

                        callBreakShiftApi("1");
                    }else if(breakStatus.equalsIgnoreCase("1")){

                        callBreakShiftApi("0");
                    }
                    break;
            }
        }

        private void rateCompletedShift() {

            final ProgressDialog progressDialog = CommonUtils.getProgressBar(context);
            ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

            try {

                RateShiftRequest rateShiftRequest = new RateShiftRequest();
                rateShiftRequest.setShiftId(shf.getShiftId());
                rateShiftRequest.setRating(String.valueOf(5));

                Call<JsonElement> call = api.shiftCheckOutRatingApi("Bearer " + session.getUserToken(),
                        rateShiftRequest);
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

                                //CommonUtils.showSnackbar(btnCompleted, message);

                                if (code.equalsIgnoreCase("200")) {
                                    Fragment fragment = HomeFragment.newInstance(shf.getShiftId(), "");
                                    fragmentInterface.fragmentResult(fragment, "");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.showSnackbar(tvClose, response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        CommonUtils.showSnakBar(tvClose, t.getMessage());
                    }
                });
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        private  void callBreakShiftApi(final String breakSts){

            final  ProgressDialog progressDialog = CommonUtils.getProgressBar(context);
            ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

            try{
                ShiftBreak shiftBreak = new ShiftBreak(breakSts,shf.getShiftId());
                Call<ShiftBreakResponse> call = api.shiftBreakApi("Bearer " + session.getUserToken(),
                        shiftBreak);

                call.enqueue(new Callback<ShiftBreakResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ShiftBreakResponse> call,
                                           @NonNull Response<ShiftBreakResponse> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            try {


                                CommonUtils.showSnackbar(tvClose, response.body().getResponse().getMessage());
                                if(shf.getIsOnBreak().toString().equalsIgnoreCase("0")){
                                    breakStatus = "1";
                                    btnbreakShift.setText("End break");
                                }else {
                                    breakStatus = "0";
                                    btnbreakShift.setText("Take a break");
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.showSnackbar(tvClose, response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ShiftBreakResponse> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        CommonUtils.showSnakBar(tvClose, t.getMessage());
                    }
                });
            }catch (Exception e){
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }
    }
    class CompletedViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvEarningAmount)
        TextView tvEarningAmount;
        @BindView(R.id.tvLocationDetail)
        TextView tvLocationDetail;
        @BindView(R.id.ivRectangle)
        ImageView ivRectangle;
        @BindView(R.id.ivDiamond)
        ImageView ivDiamond;
        @BindView(R.id.rlDialog)
        ViewGroup rlDialog;
        @BindView(R.id.tvClose)
        TextView tvClose;
        @BindView(R.id.overflowImageview)
        ImageView overflowImageview;
        @BindView(R.id.tvStartTime)
        TextView tvStartTime;
        @BindView(R.id.tvEndTime)
        TextView tvEndTime;
        @BindView(R.id.textviewshiftType)
        TextView textviewshiftType;
        @BindView(R.id.checkinTime)
        TextView tvcheckinTime;
        @BindView(R.id.checkoutTime)
        TextView tvcheckoutTime;
        Shift shf;

        @BindView(R.id.counterTextview)
        TextView counterTextview;

        @BindView(R.id.tvRoleDetail)
        TextView tvRoleDetail;

        CompletedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AppTypeface.getTypeFace(context);
            tvClose.setTypeface(AppTypeface.avenieNext_regular);
            tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
            tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);
            tvDay.setTypeface(AppTypeface.avenieNext_medium);
            tvClose.setOnClickListener(this);
            ivDiamond.setOnClickListener(this);
            ivRectangle.setOnClickListener(this);
        }

        protected  void setData(Shift shift,int position){
            this.shf =shift;


                counterTextview.setVisibility(View.VISIBLE);
                counterTextview.setText((position+1)+" of "+shiftsList.size());

            /*counterTextview.setVisibility(View.VISIBLE);
            counterTextview.setText((position+1)+" of "+shiftsList.size());*/

            tvEarningAmount.setText(shift.getEarningAmount());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(shift.getDate());
                String day_no = (String) DateFormat.format("dd", date1); // 20
                String monthString = (String) DateFormat.format("MMM", date1); // Jun
                String day = (String) DateFormat.format("EEE", date1); // Thursday
                tvDay.setText(monthString + " " + day_no + ", " + day);


                StringTokenizer tokenizer = new StringTokenizer(shift.getTimeSlot(),"-");
                String startTime = tokenizer.nextToken();
                String endTime = tokenizer.nextToken();

                tvStartTime.setText(startTime);
                tvEndTime.setText(endTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            textviewshiftType.setText("Completed Shift");
            tvcheckinTime.setText(shift.getCheckinTime());
            tvcheckoutTime.setText(shift.getCheckoutTime());
            tvLocationDetail.setText(shift.getStoreName());
            tvRoleDetail.setText(shift.getRole());
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tvClose:
                    ((Activity)context).onBackPressed();
                    break;

                case R.id.ivRectangle:
                    Intent LaunchIntent = context.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.calendar");
                    context.startActivity(LaunchIntent);
                    break;
                case R.id.ivDiamond:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (shf.getStoreName().equalsIgnoreCase("")) {
                                CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                            } else {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shf.getStoreName());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                context.startActivity(mapIntent);
                            }
                        }
                    }, 1000);
                    break;
            }
        }
    }
    class NoShowViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

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
        Shift shf;

        @BindView(R.id.counterTextview)
        TextView counterTextview;

        @BindView(R.id.tvRoleDetail)
        TextView tvRoleDetail;

        @BindView(R.id.llcheckin)
        ViewGroup llCheckin;
        @BindView(R.id.llcheckout)
        ViewGroup llCheckout;

        NoShowViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AppTypeface.getTypeFace(context);
            tvClose.setTypeface(AppTypeface.avenieNext_regular);
            tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
            tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);
            btnCallManager.setTypeface(AppTypeface.avenieNext_demibold);
            btnReleaseShift.setTypeface(AppTypeface.avenieNext_demibold);
            textviewshiftType.setTypeface(AppTypeface.avenieNext_regular);
            tvDay.setTypeface(AppTypeface.avenieNext_medium);
            tvStartTime.setTypeface(AppTypeface.avenieNext_regular);
            tvEndTime.setTypeface(AppTypeface.avenieNext_regular);

            btnCallManager.setOnClickListener(this);
            tvClose.setOnClickListener(this);
            ivDiamond.setOnClickListener(this);
            ivRectangle.setOnClickListener(this);
        }

        public void setData(Shift shift,int position){

            this.shf =shift;


            counterTextview.setVisibility(View.VISIBLE);
            counterTextview.setText((position+1)+" of "+shiftsList.size());
            tvEarningAmount.setText("$0.00");
            tvLocationDetail.setText(shift.getStoreName());
            btnReleaseShift.setVisibility(View.GONE);
            btnCallManager.setVisibility(View.GONE);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(shift.getDate());
                String day_no = (String) DateFormat.format("dd", date1); // 20
                String monthString = (String) DateFormat.format("MMM", date1); // Jun
                String day = (String) DateFormat.format("EEE", date1); // Thursday

                tvDay.setText(monthString + " " + day_no + ", " + day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {

                StringTokenizer tokenizer = new StringTokenizer(shift.getTimeSlot(), "-");
                String startTime = tokenizer.nextToken();
                String endTime = tokenizer.nextToken();

                tvStartTime.setText(startTime);
                tvEndTime.setText(endTime);
                rlDialog.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg));
                lay01.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
                lay02.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
                lay03.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
                lay04.setBackgroundColor(context.getResources().getColor(R.color.now_show_bg_dark));
                llCheckin.setVisibility(View.VISIBLE);
                llCheckout.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            textviewshiftType.setText("No Show");
            tvRoleDetail.setText(shift.getRole());

        }
        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id){

                case  R.id.tvClose:
                    ((Activity)context).onBackPressed();
                    break;

                case R.id.btnCallManager:
                    String phone = session.getManagerNumber();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                            phone, null));
                    context.startActivity(intent);
                    break;

                case R.id.ivRectangle:
                    Intent LaunchIntent = context.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.calendar");
                    context.startActivity(LaunchIntent);
                    break;
                case R.id.ivDiamond:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (shf.getStoreName().equalsIgnoreCase("")) {
                                CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                            } else {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shf.getStoreName());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                context.startActivity(mapIntent);
                            }
                        }
                    }, 1000);
                    break;
            }
        }
    }



    private void bindOnGoingViewHolder(RecyclerView.ViewHolder viewHolder,Shift shift,int position){
        OnGoingViewHolder onGoingViewHolder = (OnGoingViewHolder)viewHolder;
        onGoingViewHolder.setData(shift,position);
    }
    private void bindCompletedViewHolder(RecyclerView.ViewHolder viewHolder,Shift shift,int position){

        CompletedViewHolder completedViewHolder = (CompletedViewHolder)viewHolder;
        completedViewHolder.setData(shift,position);
    }
    private void bindUpcomingViewHolder(RecyclerView.ViewHolder viewHolder,Shift shift,int position){
        UpcomingViewHolder upcomingViewHolder = (UpcomingViewHolder)viewHolder;
        upcomingViewHolder.setData(shift,position);
    }
    private void bindNoShowViewHolder(RecyclerView.ViewHolder viewHolder,Shift shift,int position){

        NoShowViewHolder noShowViewHolder = (NoShowViewHolder)viewHolder;
        noShowViewHolder.setData(shift,position);
    }



}
package com.zing.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.RateShiftRequest;
import com.zing.model.request.ShiftBreak;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.model.response.breakShift.ShiftBreakResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateShiftFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    /*@BindView(R.id.close)
    ImageView close;
    @BindView(R.id.llClose)
    LinearLayout llClose;*/
    @BindView(R.id.tvDay)
    TextView tvDay;
  /*  @BindView(R.id.tvMyShift)
    TextView tvMyShift;*/
   /* @BindView(R.id.btnCompleted)
    Button btnCompleted;*/
   /* @BindView(R.id.tvTime)
    TextView tvTime;*/
    /*@BindView(R.id.tvTimeDetail)
    TextView tvTimeDetail;*/
   /* @BindView(R.id.tvRole)
    TextView tvRole;*/
   /* @BindView(R.id.tvRoleDetail)
    TextView tvRoleDetail;*/
    /*@BindView(R.id.tvEarning)
    TextView tvEarning;*/
    @BindView(R.id.tvEarningAmount)
    TextView tvEarningAmount;
   /* @BindView(R.id.tvLocation)
    TextView tvLocation;*/
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

   /* @BindView(R.id.rbShiftRating)
    RatingBar rbShiftRating;*/

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


    private String shift_id, location,checkinTimeText,checkoutTimeText,breakStatus,from;
    private String mParam2,shiftType;
    float rating = 0;
    private ProgressDialog progressDialog;
    SessionManagement session;

    public static RateShiftFragment newInstance(String param1, String param2,String param3,
                                                String param4,String param5,String param6,String param7) {
        RateShiftFragment fragment = new RateShiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shift_id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            checkinTimeText = getArguments().getString(ARG_PARAM3);
            checkoutTimeText = getArguments().getString(ARG_PARAM4);
            shiftType = getArguments().getString(ARG_PARAM5);
            breakStatus = getArguments().getString(ARG_PARAM6);
            from = getArguments().getString(ARG_PARAM7);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rate_shift, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.GONE);
        getShiftDetail();
    }

    private void getShiftDetail() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            ShiftCheckInRequest shiftCheckInRequest = new ShiftCheckInRequest();
            shiftCheckInRequest.setShiftId(shift_id);

            Call<ShiftDetailResponse> call = api.shiftDetailApi("Bearer " +
                    session.getUserToken(), shiftCheckInRequest);
            call.enqueue(new Callback<ShiftDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShiftDetailResponse> call,
                                       @NonNull Response<ShiftDetailResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            ShiftDetailResponse shiftDetailResponse = response.body();
                            if (shiftDetailResponse != null && shiftDetailResponse.getResponse().getCode() == 200) {

                               // tvTimeDetail.setText(shiftDetailResponse.getResponse().getData().getTimeSlot());
                                //tvRoleDetail.setText(shiftDetailResponse.getResponse().getData().getRole());

                                try {


                                    StringTokenizer tokenizer = new StringTokenizer(shiftDetailResponse.getResponse().getData().getTimeSlot(),"-");
                                    String startTime = tokenizer.nextToken();
                                    String endTime = tokenizer.nextToken();

                                    tvStartTime.setText(startTime);
                                    tvEndTime.setText(endTime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                tvEarningAmount.setText(/*"$" + */shiftDetailResponse.getResponse().getData().getExpectedEarning());
                                //tvLocationDetail.setText(shiftDetailResponse.getResponse().getData().getLocation());
                                shift_id = shiftDetailResponse.getResponse().getData().getShiftId();
                                location = shiftDetailResponse.getResponse().getData().getLocation();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date1 = format.parse(shiftDetailResponse.getResponse().getData().getDate());
                                    String day_no = (String) DateFormat.format("dd", date1); // 20
                                    String monthString = (String) DateFormat.format("MMM", date1); // Jun
                                    String day = (String) DateFormat.format("EEE", date1); // Thursday

                                    tvDay.setText(monthString + " " + day_no + ", " + day);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                CommonUtils.showSnackbar(tvClose, shiftDetailResponse.getResponse().getMessage());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvClose, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShiftDetailResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvClose, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());

        tvClose.setTypeface(AppTypeface.avenieNext_regular);
        //tvTimeDetail.setTypeface(AppTypeface.avenieNext_regular);
        //tvRoleDetail.setTypeface(AppTypeface.avenieNext_regular);
        tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
        //tvLocation.setTypeface(AppTypeface.avenieNext_regular);
        tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);

        //tvMyShift.setTypeface(AppTypeface.avenieNext_demibold);
       // btnCompleted.setTypeface(AppTypeface.avenieNext_demibold);

        tvDay.setTypeface(AppTypeface.avenieNext_medium);
        tvStartTime.setTypeface(AppTypeface.avenieNext_regular);
        tvEndTime.setTypeface(AppTypeface.avenieNext_regular);
        //tvTime.setTypeface(AppTypeface.avenieNext_medium);
        //tvRole.setTypeface(AppTypeface.avenieNext_medium);
        //tvEarning.setTypeface(AppTypeface.avenieNext_medium);
        tvRateShift.setTypeface(AppTypeface.avenieNext_medium);

        //tvTimeDetail.setText(session.getTimeSlot());
        //tvRoleDetail.setText(session.getRole());
        tvEarningAmount.setText(/*"$" +*/ session.getExpectedEarning());
       // tvLocationDetail.setText(session.getLocation());
        //location = session.getLocation();
        tvLocationDetail.setText(mParam2);
        checkinTime.setText(checkinTimeText);
        checkoutTime.setText("-");
        if(shiftType.equalsIgnoreCase("ONGOING"))
        textviewshiftType.setText("Ongoing Shift");

        if(breakStatus.equalsIgnoreCase("0")){
            btnbreakShift.setText("Take a break");
        }else if(breakStatus.equalsIgnoreCase("1")){

            btnbreakShift.setText("End break");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(session.getDate());
            String day_no = (String) DateFormat.format("dd", date1); // 20
            String monthString = (String) DateFormat.format("MMM", date1); // Jun
            String day = (String) DateFormat.format("EEE", date1); // Thursday

            tvDay.setText(monthString + " " + day_no + ", " + day);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*rbShiftRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar bar = (RatingBar) v;
                rating = bar.getRating();
            }
        });*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvClose, R.id.btncheckoutShift, R.id.ivRectangle, R.id.ivDiamond,R.id.btnbreakShift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvClose:

                if (from.equalsIgnoreCase("home")||from.equalsIgnoreCase("upcoming")) {
                    Fragment fragment = HomeFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment, "");
                } else if (from.equalsIgnoreCase("calendar") ||
                        (from.equalsIgnoreCase("calendarWeek"))) {
                    Fragment fragment = CalenderFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment, "");
                }
                //getActivity().onBackPressed();
                break;
            case R.id.btncheckoutShift:
                //showRateDialog();
                rateCompletedShift();
                break;
            case R.id.ivRectangle:
                Intent LaunchIntent = getActivity().getPackageManager()
                        .getLaunchIntentForPackage("com.google.android.calendar");
                startActivity(LaunchIntent);
                break;
            case R.id.ivDiamond:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (location.equalsIgnoreCase("")) {
                            CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                        } else {
                            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
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

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {

            RateShiftRequest rateShiftRequest = new RateShiftRequest();
            rateShiftRequest.setShiftId(shift_id);
            rateShiftRequest.setRating(String.valueOf(rating));

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
                                Fragment fragment = HomeFragment.newInstance(shift_id, "");
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

    private void showRateDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rate_shift);

        final RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.rbShiftRating);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnNext);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = ratingBar.getRating();
                //rateCompletedShift();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private  void callBreakShiftApi(final String breakSts){

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try{
            ShiftBreak shiftBreak = new ShiftBreak(breakSts,shift_id);
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
                            if(breakStatus.equalsIgnoreCase("0")){
                                breakStatus = "1";
                                btnbreakShift.setText("Break Out");
                            }else {
                                breakStatus = "0";
                                btnbreakShift.setText("Break In");
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

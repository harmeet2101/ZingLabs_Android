package com.zing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.response.BroadcastResponse.BroadcastList;
import com.zing.model.response.BroadcastResponse.BroadcastResponse;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONException;
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

public class ClaimShiftFragment extends BaseFragment {

    @BindView(R.id.tvClose)
    TextView tvClose;
   /* @BindView(R.id.close)
    ImageView close;*/
   /* @BindView(R.id.llClose)
    LinearLayout llClose;*/
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvOpenShift)
    TextView tvOpenShift;
    /*@BindView(R.id.tvUnclaimed)
    TextView tvUnclaimed;*/
    /*@BindView(R.id.tvTime)
    TextView tvTime;*/
   /* @BindView(R.id.tvTimeDetail)
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
    LinearLayout rlDialog;
    @BindView(R.id.btnClaimShift)
    Button btnClaimShift;
    @BindView(R.id.btnCallManager)
    Button btnCallManager;
    Unbinder unbinder;

    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;

    private String shift_id, date, day, expectedEarning, timeSlot, location, role, release = "";
    private String from;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";

    private ProgressDialog progressDialog;
    private boolean isWithin24hrs;
    SessionManagement session;

    public static ClaimShiftFragment newInstance(String param1, String param2) {
        ClaimShiftFragment fragment = new ClaimShiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    public static ClaimShiftFragment newInstance(String param1, String param2,
                                                  String param3, String param4,
                                                  String param5, String param6,
                                                  String param7, String param8,
                                                  String param9) {
        ClaimShiftFragment fragment = new ClaimShiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString(ARG_PARAM1);
            shift_id = getArguments().getString(ARG_PARAM2);
            date = getArguments().getString(ARG_PARAM3);
            day = getArguments().getString(ARG_PARAM4);
            expectedEarning = getArguments().getString(ARG_PARAM5);
            timeSlot = getArguments().getString(ARG_PARAM6);
            location = getArguments().getString(ARG_PARAM7);
            role = getArguments().getString(ARG_PARAM8);
            release = getArguments().getString(ARG_PARAM9);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_claim_shift, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnected(getActivity()))
            getShiftDetails();



    }

    private void getShiftDetails() {
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
                               // tvRoleDetail.setText(shiftDetailResponse.getResponse().getData().getRole());
                                tvEarningAmount.setText(/*"$" + */shiftDetailResponse.getResponse().getData().getExpectedEarning());
                                tvLocationDetail.setText(shiftDetailResponse.getResponse().getData().getStore_name());
                                shift_id = shiftDetailResponse.getResponse().getData().getShiftId();

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date1 = format.parse(shiftDetailResponse.getResponse().getData().getDate());
                                    String day_no = (String) DateFormat.format("dd", date1); // 20
                                    String monthString = (String) DateFormat.format("MMM", date1); // Jun
                                    String day = (String) DateFormat.format("EEE", date1); // Thursday

                                    tvDay.setText(monthString + " " + day_no + ", " + day);

                                    StringTokenizer tokenizer = new StringTokenizer(shiftDetailResponse.getResponse().getData().getTimeSlot(),"-");
                                    String startTime = tokenizer.nextToken();
                                    String endTime = tokenizer.nextToken();

                                    tvStartTime.setText(startTime);
                                    tvEndTime.setText(endTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if(from.equalsIgnoreCase("true")){
                                    btnClaimShift.setVisibility(View.GONE);
                                   // tvUnclaimed.setText("claimed");
                                }



                                try {

                                    Date currentDate = new Date();
                                    String pattern = "yyyy-MM-dd hh:mm a";
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                    StringTokenizer tokenizer = new StringTokenizer(shiftDetailResponse.getResponse().getData().getTimeSlot(), "-");
                                    String startTime = tokenizer.nextToken();
                                    String endTime = tokenizer.nextToken();

                                    String mDateString = shiftDetailResponse.getResponse().getData().getDate() + " " + startTime.toUpperCase();
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
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                                if (isWithin24hrs) {
                                    btnClaimShift.setVisibility(View.GONE);
                                } else {
                                    btnClaimShift.setVisibility(View.VISIBLE);
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
       // tvTimeDetail.setTypeface(AppTypeface.avenieNext_regular);
       // tvRoleDetail.setTypeface(AppTypeface.avenieNext_regular);
        tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);

        btnClaimShift.setTypeface(AppTypeface.avenieNext_demibold);
        btnCallManager.setTypeface(AppTypeface.avenieNext_demibold);
        tvOpenShift.setTypeface(AppTypeface.avenieNext_demibold);
        //tvUnclaimed.setTypeface(AppTypeface.avenieNext_demibold);

        tvDay.setTypeface(AppTypeface.avenieNext_medium);
       // tvTime.setTypeface(AppTypeface.avenieNext_medium);
       // tvEarning.setTypeface(AppTypeface.avenieNext_medium);
       // tvRole.setTypeface(AppTypeface.avenieNext_medium);
      //  tvLocation.setTypeface(AppTypeface.avenieNext_medium);

      //  tvTimeDetail.setText(session.getTimeSlot());
      //  tvRoleDetail.setText(session.getRole());
        tvLocationDetail.setText(location);

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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvClose,R.id.btnClaimShift, R.id.btnCallManager, R.id.ivRectangle,R.id.ivDiamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tvClose:
                    getActivity().onBackPressed();

                break;
            case R.id.btnClaimShift:
                claimShift();
                break;
            case R.id.btnCallManager:
                String phone = session.getManagerNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        phone, null));
                startActivity(intent);
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
        }
    }

    private void claimShift() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            ShiftCheckInRequest shiftCheckInRequest = new ShiftCheckInRequest();
            shiftCheckInRequest.setShiftId(shift_id);

            Call<JsonElement> call = api.claimShiftApi("Bearer " + session.getUserToken(),
                    shiftCheckInRequest);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject responseObj = new JSONObject(response.body().toString());
                            JSONObject jsonObject = responseObj.optJSONObject("response");
                            String code = jsonObject.optString("code");
                            String message = jsonObject.optString("message");

                            CommonUtils.showSnackbar(tvClose, message);

                            if (code.equalsIgnoreCase("200")) {
                                Fragment fragment = CalenderFragment.newInstance("", "");
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
}

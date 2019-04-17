package com.zing.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.RateShiftRequest;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteShiftFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

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

    @BindView(R.id.rbShiftRating)
    RatingBar rbShiftRating;

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

    private String shift_id, location;
    private String mParam2,shiftType,checkInTime,checkOutTime,earningAmount;
    float rating = 0;
    private ProgressDialog progressDialog;
    SessionManagement session;

    public static CompleteShiftFragment newInstance(String param1, String param2,String param3,String param4,String param5,String param6) {
        CompleteShiftFragment fragment = new CompleteShiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shift_id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            shiftType = getArguments().getString(ARG_PARAM3);
            checkInTime = getArguments().getString(ARG_PARAM4);
            checkOutTime = getArguments().getString(ARG_PARAM5);
            earningAmount = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complete_shift, container, false);
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
                                    tvEarningAmount.setText(earningAmount);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                               // tvEarningAmount.setText(/*"$" + */shiftDetailResponse.getResponse().getData().getExpectedEarning());
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

        tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);

        tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);



        tvDay.setTypeface(AppTypeface.avenieNext_medium);

        tvRateShift.setTypeface(AppTypeface.avenieNext_medium);


        tvEarningAmount.setText(/*"$" +*/ session.getExpectedEarning());

        location = session.getLocation();

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
        if(shiftType.equalsIgnoreCase("COMPLETED"))
        textviewshiftType.setText("Completed Shift");
        tvcheckinTime.setText(checkInTime);
        tvcheckoutTime.setText(checkOutTime);
        tvLocationDetail.setText(mParam2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvClose, R.id.ivRectangle, R.id.ivDiamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvClose:
                getActivity().onBackPressed();
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
}

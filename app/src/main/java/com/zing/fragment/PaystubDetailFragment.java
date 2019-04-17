package com.zing.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.adapter.IncludedShiftAdapter;
import com.zing.adapter.ListPopupWindowAdapter;
import com.zing.adapter.OtherWagesAdapter;
import com.zing.model.request.PaymentIdRequest;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.model.response.paymentDetailResponse.IncludedShift;
import com.zing.model.response.paymentDetailResponse.Other;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaystubDetailFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvPaymentDetails)
    TextView tvPaymentDetails;
    @BindView(R.id.tvPaymentAmount)
    TextView tvPaymentAmount;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.btnStatus)
    Button btnStatus;
    @BindView(R.id.tvPaystubs)
    TextView tvPaystubs;
    @BindView(R.id.tvTotalWages)
    TextView tvTotalWages;
    @BindView(R.id.tvTotalWagesAmount)
    TextView tvTotalWagesAmount;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTipsAmount)
    TextView tvTipsAmount;
    @BindView(R.id.tvOther)
    TextView tvOther;
    @BindView(R.id.tvOtherAmount)
    TextView tvOtherAmount;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tvTransferDetails)
    TextView tvTransferDetails;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvAccountNo)
    TextView tvAccountNo;
    @BindView(R.id.tvEncryptDescription)
    TextView tvEncryptDescription;
    @BindView(R.id.tvIncludedShift)
    TextView tvIncludedShift;
    @BindView(R.id.rvIncludedShift)
    RecyclerView rvIncludedShift;
    Unbinder unbinder;

    @BindView(R.id.expandableLayout)
    ExpandableRelativeLayout expandableLayout;
    @BindView(R.id.ivDrop)
    ImageView ivDrop;
    @BindView(R.id.rvOthers)
    RecyclerView rvOthers;
    private ProgressDialog progressDialog;
    SessionManagement session;
    //    ArrayList<BankDetail> bankDetailsList;
    ArrayList<UpcomingShift> includedShiftList;
    ArrayList<Other> otherList;
    private OtherWagesAdapter otherWagesAdapter;
    private IncludedShiftAdapter includedShiftAdapter;
    private String earnings, paymentStatus, payment_id, paymentSlot;
    private float total = 0;

    public static PaystubDetailFragment newInstance(String param1, String param2, String param3, String param4) {
        PaystubDetailFragment fragment = new PaystubDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            payment_id = getArguments().getString(ARG_PARAM1);
            earnings = getArguments().getString(ARG_PARAM2);
            paymentStatus = getArguments().getString(ARG_PARAM3);
            paymentSlot = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paystub_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
//        bankDetailsList = new ArrayList<>();
        includedShiftList = new ArrayList<>();
        otherList = new ArrayList<>();

        session = new SessionManagement(getActivity());

        tvPaymentDetails.setTypeface(AppTypeface.avenieNext_medium);
        tvDuration.setTypeface(AppTypeface.avenieNext_regular);
        btnStatus.setTypeface(AppTypeface.avenieNext_medium);
        tvTotalWages.setTypeface(AppTypeface.avenieNext_regular);
        tvTotalWagesAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvTips.setTypeface(AppTypeface.avenieNext_regular);
        tvTipsAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvOther.setTypeface(AppTypeface.avenieNext_regular);
        tvOtherAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvTotal.setTypeface(AppTypeface.avenieNext_regular);
        tvTotalAmount.setTypeface(AppTypeface.avenieNext_medium);
        tvAccountNo.setTypeface(AppTypeface.avenieNext_regular);
        tvEncryptDescription.setTypeface(AppTypeface.avenieNext_medium);

        tvPaymentAmount.setTypeface(AppTypeface.avenieNext_demibold);
        tvPaystubs.setTypeface(AppTypeface.avenieNext_demibold);
        tvTransferDetails.setTypeface(AppTypeface.avenieNext_demibold);
        tvBankName.setTypeface(AppTypeface.avenieNext_medium);
        tvIncludedShift.setTypeface(AppTypeface.avenieNext_demibold);

        expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
//                createRotateAnimator(tvOther, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
//                createRotateAnimator(tvOther, 180f, 0f).start();
            }
        });
        tvPaymentAmount.setText(earnings);
        tvDuration.setText(paymentSlot.replace("to", "-"));
        if (paymentStatus.equalsIgnoreCase("processing")) {
            btnStatus.setText(paymentStatus.toUpperCase());
            btnStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_button));
        } else {
            btnStatus.setText(paymentStatus.toUpperCase());
            btnStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button));
        }

        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
        getPaymentDetails();
    }

    private void getPaymentDetails() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            PaymentIdRequest paymentIdRequest = new PaymentIdRequest();
            paymentIdRequest.setPaymentId(payment_id);

            Call<JsonElement> call = api.paymentDetailApi("Bearer " + session.getUserToken(),
                    paymentIdRequest);
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
                            String upcoming_payment = responseObj.optString("upcoming_payment");
//                            String payment_id = responseObj.optString("payment_id");
                            String total_wages = responseObj.optString("total_wages");
                            String tips = responseObj.optString("tips");

                            tvTotalWagesAmount.setText("$" + total_wages);
                            tvTipsAmount.setText("$" + tips);

                            int cost = 0;
                            otherList.clear();

                            JSONArray othersArr = responseObj.optJSONArray("other");
                            for (int i = 0; i < othersArr.length(); i++) {
                                JSONObject otherObj = othersArr.optJSONObject(i);
                                String over_time = otherObj.optString("over_time");

                                Other other = new Other();
                                other.setOverTime(over_time);
                                otherList.add(other);

                                cost = cost + Integer.parseInt(over_time);
                            }

                            rvOthers.setLayoutManager(new LinearLayoutManager(getActivity()));
                            otherWagesAdapter = new OtherWagesAdapter(getActivity(), otherList);
                            rvOthers.setAdapter(otherWagesAdapter);

                            tvOtherAmount.setText("" + cost);
                            total = cost + Integer.parseInt(tips) + Float.parseFloat(total_wages);
                            tvTotalAmount.setText("$" + String.valueOf(total));

                            JSONObject bank_detailsObj = responseObj.optJSONObject("bank_detail");

                            if (bank_detailsObj != null) {
                                String account_no = bank_detailsObj.optString("account_no");
                                String bank_name = bank_detailsObj.optString("bank_name");
                                String bank_id = bank_detailsObj.optString("bank_id");
                                String routing_no = bank_detailsObj.optString("routing_no");

                                tvBankName.setText(bank_name);
                                tvAccountNo.setText(account_no);
                            }

                            includedShiftList.clear();
                            JSONArray included_shiftsArr = responseObj.optJSONArray("included_shifts");
                            for (int i = 0; i < included_shiftsArr.length(); i++) {
                                JSONObject included_shiftsObj = included_shiftsArr.optJSONObject(i);
                                String date = included_shiftsObj.optString("date");
                                String day = included_shiftsObj.optString("day");
                                String shift_id = included_shiftsObj.optString("shift_id");
                                String expected_earning = included_shiftsObj.optString("expected_earning");
                                String location = included_shiftsObj.optString("location");
                                String role = included_shiftsObj.optString("role");
                                String time_slot = included_shiftsObj.optString("time_slot");

                                UpcomingShift upcomingShift = new UpcomingShift();
                                upcomingShift.setDay(day);
                                upcomingShift.setDate(date);
                                upcomingShift.setExpectedEarning(expected_earning);
                                upcomingShift.setLocation(location);
                                upcomingShift.setRole(role);
                                upcomingShift.setShiftId(shift_id);
                                upcomingShift.setTimeSlot(time_slot);
                                includedShiftList.add(upcomingShift);
                            }

                            rvIncludedShift.setLayoutManager(new LinearLayoutManager(getActivity()));
                            includedShiftAdapter = new IncludedShiftAdapter(getActivity(), fragmentInterface, includedShiftList);
                            rvIncludedShift.setAdapter(includedShiftAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvAccountNo, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvAccountNo, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        Fragment fragment = EarningFragment.newInstance("", "");
        fragmentInterface.fragmentResult(fragment, "");
    }
}

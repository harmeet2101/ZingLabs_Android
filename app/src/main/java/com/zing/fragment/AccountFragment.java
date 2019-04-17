package com.zing.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.activity.MainActivity;
import com.zing.activity.PreferenceCalenderActivity;
import com.zing.adapter.TimeOffAdapter;
import com.zing.model.request.DeleteBankRequest;
import com.zing.model.request.LeaveCancelRequest;
import com.zing.model.response.AccountResponse.AccountResponse;
import com.zing.model.response.AccountResponse.TimeOff;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends BaseFragment implements TimeOffAdapter.ClickListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvPersonalInfo)
    TextView tvPersonalInfo;
    @BindView(R.id.tvDob)
    TextView tvDob;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvPhoneNo)
    TextView tvPhoneNo;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAddressDetail)
    TextView tvAddressDetail;
    @BindView(R.id.btnPersonalInfo)
    Button btnPersonalInfo;
    @BindView(R.id.tvPaymentDetail)
    TextView tvPaymentDetail;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvDelete)
    TextView tvDelete;
    @BindView(R.id.tvAccountNo)
    TextView tvAccountNo;
    @BindView(R.id.tvAccountDetail)
    TextView tvAccountDetail;
    @BindView(R.id.btnPaymentSetting)
    Button btnPaymentSetting;
    @BindView(R.id.tvTimePreferences)
    TextView tvTimePreferences;
    @BindView(R.id.tvTimePreferencesDescription)
    TextView tvTimePreferencesDescription;
    @BindView(R.id.btnManage)
    Button btnManage;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.tvTimeOff)
    TextView tvTimeOff;
    //    @BindView(R.id.tvApproved)
//    TextView tvApproved;
//    @BindView(R.id.tvCancel)
//    TextView tvCancel;
//    @BindView(R.id.tvStartDate)
//    TextView tvStartDate;
//    @BindView(R.id.tvStartDateDetail)
//    TextView tvStartDateDetail;
//    @BindView(R.id.tvEndDate)
//    TextView tvEndDate;
//    @BindView(R.id.tvEndDateDetail)
//    TextView tvEndDateDetail;
    Unbinder unbinder;
    @BindView(R.id.cvPaymentDetails)
    LinearLayout cvPaymentDetails;
    @BindView(R.id.llLayout)
    LinearLayout llLayout;
    @BindView(R.id.rvTimeOff)
    RecyclerView rvTimeOff;
    @BindView(R.id.tvTimeOffHeading)
    TextView tvTimeOffHeading;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.btnRequestNew)
    Button btnRequestNew;

    private String mParam1;
    private String mParam2, bank_id = "", routingNo = "", accountNo = "";

    Fragment fragment;
    private ProgressDialog progressDialog;
    SessionManagement session;
    ArrayList<TimeOff> timeOffList;
    private TimeOffAdapter timeOffAdapter;

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        timeOffList = new ArrayList<>();
        session = new SessionManagement(getActivity());
        tvPersonalInfo.setTypeface(AppTypeface.avenieNext_medium);
        tvTimeOff.setTypeface(AppTypeface.avenieNext_medium);
        tvPaymentDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvBankName.setTypeface(AppTypeface.avenieNext_medium);
        tvDelete.setTypeface(AppTypeface.avenieNext_medium);
        tvTimePreferences.setTypeface(AppTypeface.avenieNext_medium);

        tvAccountNo.setTypeface(AppTypeface.avenieNext_regular);
        tvDob.setTypeface(AppTypeface.avenieNext_regular);
        tvDate.setTypeface(AppTypeface.avenieNext_regular);
        tvPhone.setTypeface(AppTypeface.avenieNext_regular);
        tvPhoneNo.setTypeface(AppTypeface.avenieNext_regular);
        tvAddress.setTypeface(AppTypeface.avenieNext_regular);
        tvAddressDetail.setTypeface(AppTypeface.avenieNext_regular);
        btnPersonalInfo.setTypeface(AppTypeface.avenieNext_demibold);
        btnPaymentSetting.setTypeface(AppTypeface.avenieNext_demibold);
        tvTimePreferencesDescription.setTypeface(AppTypeface.avenieNext_medium);
        btnManage.setTypeface(AppTypeface.avenieNext_demibold);
        btnRequestNew.setTypeface(AppTypeface.avenieNext_demibold);

        rvTimeOff.setLayoutManager(new LinearLayoutManager(getActivity()));
        timeOffAdapter = new TimeOffAdapter(getActivity(), timeOffList, tvTimeOff);
        rvTimeOff.setAdapter(timeOffAdapter);
        timeOffAdapter.setClickListner(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);
        if (NetworkUtils.isNetworkConnected(getActivity()))
        getAccountDetails();
    }

    private void getAccountDetails() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {

            Call<AccountResponse> call = api.accountDetailsApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(@NonNull Call<AccountResponse> call,
                                       @NonNull Response<AccountResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            AccountResponse accountResponse = response.body();

                            llLayout.setVisibility(View.VISIBLE);
                            if (accountResponse != null && accountResponse.getResponse().getCode().equalsIgnoreCase("200")) {
                                timeOffList.clear();
                                for (int i = 0; i < accountResponse.getResponse().getData().getTimeOff().size(); i++) {
                                    TimeOff timeOff = new TimeOff();
                                    timeOff.setStartDate(accountResponse.getResponse().getData().getTimeOff().get(i).getStartDate());
                                    timeOff.setEndDate(accountResponse.getResponse().getData().getTimeOff().get(i).getEndDate());
                                    timeOff.setLeaveId(accountResponse.getResponse().getData().getTimeOff().get(i).getLeaveId());
                                    timeOff.setApproved(accountResponse.getResponse().getData().getTimeOff().get(i).getApproved());
                                    timeOffList.add(timeOff);
                                }

                                tvDate.setText(accountResponse.getResponse().getData().getPersonalInfo().getDOB());
                                tvPhoneNo.setText(accountResponse.getResponse().getData().getPersonalInfo().getPhone());
                                tvAddressDetail.setText(accountResponse.getResponse().getData().getPersonalInfo().getAddress());
                                tvBankName.setText(accountResponse.getResponse().getData().getPaymentDetails().getBankName());
                                routingNo = accountResponse.getResponse().getData().getPaymentDetails().getRoutingNo();

                                if (accountResponse.getResponse().getData().getPaymentDetails() == null) {
                                    llBank.setVisibility(View.GONE);
                                }

                                if (accountResponse.getResponse().getData().getPaymentDetails().getAccountNo() != null) {
                                    String strLastFourDi = accountResponse.getResponse().getData().getPaymentDetails().getAccountNo()
                                            .length() >= 4 ? accountResponse.getResponse().getData().getPaymentDetails().getAccountNo()
                                            .substring(accountResponse.getResponse().getData().getPaymentDetails().getAccountNo()
                                                    .length() - 4) : "";

                                    accountNo = accountResponse.getResponse().getData().getPaymentDetails().getAccountNo();
                                    tvAccountDetail.setText("xxxx xxxx xxxx " + strLastFourDi);
                                }

                                bank_id = accountResponse.getResponse().getData().getPaymentDetails().getBankId();

                                if (timeOffList.size() == 0) {
                                    tvTimeOffHeading.setVisibility(View.VISIBLE);
                                } else {
                                    tvTimeOffHeading.setVisibility(View.GONE);
                                }

                                timeOffAdapter.notifyDataSetChanged();

                                if (bank_id == null) {
                                    cvPaymentDetails.setVisibility(View.GONE);
                                } else {
                                    cvPaymentDetails.setVisibility(View.VISIBLE);
                                }

                            } else {
                                CommonUtils.showSnackbar(tvAccountDetail, accountResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvAccountDetail, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvAccountDetail, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    Intent intent;

    @OnClick({R.id.btnLogout, R.id.btnPersonalInfo, R.id.tvDelete, R.id.btnPaymentSetting,
            R.id.btnManage, R.id.btnRequestNew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPersonalInfo:
                fragment = EditProfileFragment.newInstance("", "");
                fragmentInterface.fragmentResult(fragment, "");
                break;
            case R.id.tvDelete:
                openDialog(getContext().getString(R.string.delete_payment_profile),
                        getContext().getString(R.string.are_you_sure_you_of_want_to_delete_the_payment_profile));
                break;
            case R.id.btnPaymentSetting:
                fragment = SetPaymentDetailsFragment.newInstance(bank_id, routingNo,
                        tvBankName.getText().toString(), accountNo, "account");
                fragmentInterface.fragmentResult(fragment, "");
                break;
            case R.id.btnManage:
                intent = new Intent(getActivity(), PreferenceCalenderActivity.class);
                intent.putExtra("from", "account");
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnRequestNew:
                fragment = TimeOffFragment.newInstance("", "");
                fragmentInterface.fragmentResult(fragment, "");
                break;
            case R.id.btnLogout:
                logout();
                break;
        }
    }

    private void logout() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<JsonElement> call = api.logoutApi("Bearer " + session.getUserToken());
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

                            CommonUtils.showSnackbar(tvAccountDetail, message);

                            if (code.equalsIgnoreCase("200")) {
                                session.clearSession();
                                session.logoutUser();
                                getActivity().finishAffinity();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvAccountDetail, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvAccountDetail, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private Dialog dialog;

    private void openDialog(String heading, String Description) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(400, 350);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        final TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        TextView tvGoBack = dialog.findViewById(R.id.tvGoBack);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        tvHeading.setText(heading);
        tvDescription.setText(Description);

        AppTypeface.getTypeFace(getActivity());
        tvHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvDescription.setTypeface(AppTypeface.avenieNext_medium);
        tvGoBack.setTypeface(AppTypeface.avenieNext_demibold);
        btnYes.setTypeface(AppTypeface.avenieNext_demibold);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (tvHeading.getText().toString().equalsIgnoreCase("Delete Payment Profile")) {
                    deletePaymentProfile(bank_id);
                } else {
                    cancelTimeOff("");
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

    private void cancelTimeOff(String leave_id) {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            LeaveCancelRequest leaveCancelRequest = new LeaveCancelRequest();
            leaveCancelRequest.setLeaveId(leave_id);

            Call<JsonElement> call = api.cancelLeaveRequestApi("Bearer " + session.getUserToken(),
                    leaveCancelRequest);
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

                            CommonUtils.showSnackbar(tvAccountDetail, message);
                            onResume();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvAccountDetail, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvAccountDetail, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void deletePaymentProfile(String bank_id) {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            DeleteBankRequest jsonObject = new DeleteBankRequest();
            jsonObject.setBankId(bank_id);

            Call<JsonElement> call = api.deleteBankRequestApi("Bearer " + session.getUserToken(), jsonObject);
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

                            CommonUtils.showSnackbar(tvAccountDetail, message);

                            Fragment fragment = AccountFragment.newInstance("", "");
                            fragmentInterface.fragmentResult(fragment, "");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvAccountDetail, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvAccountDetail, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        openDialog(getResources().getString(R.string.cancel_time_off),
                getResources().getString(R.string.are_you_sure_you_want_to_cancel_your_time_off),
                timeOffList.get(position).getLeaveId());
    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    private void openDialog(String heading, String Description, final String leave_id) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(400, 350);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        final TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        TextView tvGoBack = dialog.findViewById(R.id.tvGoBack);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        tvHeading.setText(heading);
        tvDescription.setText(Description);

        tvHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvDescription.setTypeface(AppTypeface.avenieNext_medium);
        tvGoBack.setTypeface(AppTypeface.avenieNext_demibold);
        btnYes.setTypeface(AppTypeface.avenieNext_demibold);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cancelTimeOff(leave_id);

            }
        });

        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}

package com.zing.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.AddBankRequest;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPaymentDetailsFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.tvPayment)
    TextView tvPayment;
    @BindView(R.id.llHeading)
    LinearLayout llHeading;
    @BindView(R.id.tvBank)
    TextView tvBank;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.tvAccNo)
    TextView tvAccNo;
    @BindView(R.id.etAccNo)
    EditText etAccNo;
    @BindView(R.id.tvRoutingNo)
    TextView tvRoutingNo;
    @BindView(R.id.etRoutingNo)
    EditText etRoutingNo;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    Unbinder unbinder;

    private ProgressDialog progressDialog;
    SessionManagement session;

    private String mParam1, from;
    private String mParam2, mParam3, mParam4;

    public static SetPaymentDetailsFragment newInstance(String param1, String param2, String param3,
                                                        String param4, String from) {
        SetPaymentDetailsFragment fragment = new SetPaymentDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            from = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_payment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());
        tvSelect.setTypeface(AppTypeface.avenieNext_demibold);
        tvPayment.setTypeface(AppTypeface.avenieNext_demibold);

        etBankName.setTypeface(AppTypeface.avenieNext_regular);
        tvBank.setTypeface(AppTypeface.avenieNext_demibold);
        tvAccNo.setTypeface(AppTypeface.avenieNext_demibold);
        etAccNo.setTypeface(AppTypeface.avenieNext_regular);
        tvRoutingNo.setTypeface(AppTypeface.avenieNext_demibold);
        etRoutingNo.setTypeface(AppTypeface.avenieNext_regular);
        btnSave.setTypeface(AppTypeface.avenieNext_demibold);
        tvCancel.setTypeface(AppTypeface.avenieNext_medium);

        if (mParam2 != null) {
            if (mParam2.equalsIgnoreCase("")) {
                etRoutingNo.setText(mParam2);
            } else {
                String strLastFourDi = mParam2.length() >= 4 ? mParam2.substring(mParam2.length() - 4) : "";
                etRoutingNo.setText("xxxxx" + strLastFourDi);
            }
        }

        if (mParam3 != null || (!mParam3.equalsIgnoreCase("")))
            etBankName.setText(mParam3);

        if (mParam4 != null || (!mParam4.equalsIgnoreCase("")))
            etAccNo.setText(mParam4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.GONE);
    }

    Fragment fragment;

    @OnClick({R.id.ivBack, R.id.btnSave, R.id.tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
//                getActivity().onBackPressed();
                switch (from) {
                    case "account":
                        fragment = AccountFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                    case "home":
                        fragment = HomeFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                    case "earning":
                        fragment = EarningFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                }
                break;
            case R.id.btnSave:
                if (etBankName.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar(etBankName, getActivity().getString(R.string.validate_emptyField));
                } else if (etAccNo.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar(etBankName, getActivity().getString(R.string.validate_emptyField));
                } else if (etRoutingNo.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar(etBankName, getActivity().getString(R.string.validate_emptyField));
                } else
                    saveBankDetails(etBankName.getText().toString(), etAccNo.getText().toString(),
                            etRoutingNo.getText().toString());
                break;
            case R.id.tvCancel:
//                getActivity().onBackPressed();
                switch (from) {
                    case "account":
                        fragment = AccountFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                    case "home":
                        fragment = HomeFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                    case "earning":
                        fragment = EarningFragment.newInstance("", "");
                        fragmentInterface.fragmentResult(fragment, "");
                        break;
                }
                break;
        }
    }

    private void saveBankDetails(String bankName, String accountNo, String routingNo) {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            AddBankRequest addBankRequest = new AddBankRequest();
           /* if (mParam2.contains("x")) {*/
                addBankRequest.setAccountNo(etAccNo.getText().toString());
                addBankRequest.setBankName(etBankName.getText().toString());
                addBankRequest.setRoutingNo(etRoutingNo.getText().toString());
        /*    } else {
                addBankRequest.setAccountNo(mParam4);
                addBankRequest.setBankName(mParam3);
                addBankRequest.setRoutingNo(mParam2);
            }*/

            Call<JsonElement> call = api.saveBankDetailsApi("Bearer " + session.getUserToken(),
                    addBankRequest);
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

                            CommonUtils.showSnackbar(btnSave, message);

                            if (code.equalsIgnoreCase("200")) {
                                etBankName.setText("");
                                etAccNo.setText("");
                                etRoutingNo.setText("");

                                switch (from) {
                                    case "account":
                                        fragment = AccountFragment.newInstance("", "");
                                        fragmentInterface.fragmentResult(fragment, "");
                                        break;
                                    case "home":
                                        fragment = HomeFragment.newInstance("", "");
                                        fragmentInterface.fragmentResult(fragment, "");
                                        break;
                                    case "earning":
                                        fragment = EarningFragment.newInstance("", "");
                                        fragmentInterface.fragmentResult(fragment, "");
                                        break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvPayment, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvPayment, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
      /*  } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}

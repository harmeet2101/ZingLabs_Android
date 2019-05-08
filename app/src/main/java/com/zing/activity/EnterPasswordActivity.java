package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.request.SetPasswordRequest;
import com.zing.model.response.otpVerifyResponse.BasicInfo;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhishek on 24/5/18.
 */

public class EnterPasswordActivity extends BaseActivity {
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvHeading)
    TextView tvHeading;
    @BindView(R.id.tvPassword)
    TextView tvPassword;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.viewMobile)
    View viewMobile;


    @BindView(R.id.tvConfirmPassword)
    TextView tvConfirmPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.viewConfirmMobile)
    View viewConfirmMobile;


    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvBack)
    TextView tvBack;



    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;

    @BindView(R.id.tvConfirmErrorDetail)
    TextView tvConfirmErrorDetail;
    private ProgressDialog progressDialog;

    private String from = "", phone;
    private BasicInfo basicInfo;
    //private PreferenceInfo preferenceInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        ButterKnife.bind(this);

        tvWelcome.setTypeface(AppTypeface.avenieNext_demibold);
        tvHeading.setTypeface(AppTypeface.avenieNext_demibold);
        tvPassword.setTypeface(AppTypeface.avenieNext_demibold);
        etPassword.setTypeface(AppTypeface.avenieNext_medium);
        tvErrorDetail.setTypeface(AppTypeface.avenieNext_medium);

        tvConfirmPassword.setTypeface(AppTypeface.avenieNext_demibold);
        etConfirmPassword.setTypeface(AppTypeface.avenieNext_medium);
        tvConfirmErrorDetail.setTypeface(AppTypeface.avenieNext_medium);

        btnSubmit.setTypeface(AppTypeface.avenieNext_demibold);

        tvBack.setTypeface(AppTypeface.avenieNext_medium);

        from = getIntent().getStringExtra("from");
        phone = getIntent().getStringExtra("phone");
        if(getIntent().hasExtra("basicInfo"))
        basicInfo=(BasicInfo) getIntent().getSerializableExtra("basicInfo");
       // preferenceInfo = (PreferenceInfo) getIntent().getSerializableExtra("preferences");
        if (from.equalsIgnoreCase("forgotPassword")) {
            tvWelcome.setText(getResources().getString(R.string.reset));
            tvHeading.setText(getResources().getString(R.string.small_password));
            tvPassword.setText(getResources().getString(R.string.new_password));
            tvBack.setVisibility(View.GONE);
        }

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.btnSubmit, R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (etPassword.getText().length() < 8) {
                    viewMobile.setBackgroundColor(getResources().getColor(R.color.red));
                    etPassword.setTextColor(getResources().getColor(R.color.red));
                    tvErrorDetail.setText(R.string.password_validation);
                }if(etConfirmPassword.getText().length() < 8){

                viewConfirmMobile.setBackgroundColor(getResources().getColor(R.color.red));
                etConfirmPassword.setTextColor(getResources().getColor(R.color.red));
                tvConfirmErrorDetail.setText(R.string.password_validation);
                }

                else if(!etPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())){

                    Toast.makeText(getBaseContext(),"Passwords doesnot match",Toast.LENGTH_SHORT).show();
                }

                else {
                    if (from.equalsIgnoreCase("forgotPassword")) {
                        if (NetworkUtils.isNetworkConnected(this))
                            changePassword();

                    } else {
                        Intent intent = new Intent(EnterPasswordActivity.this, MainActivity.class);
                        intent.putExtra("phone", phone);
                        intent.putExtra("basicInfo",basicInfo);
                       // intent.putExtra("preferences",preferenceInfo);
                        intent.putExtra("from", "enterPassword");
                        intent.putExtra("password", etPassword.getText().toString());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

    private void changePassword() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            SetPasswordRequest jsonObject = new SetPasswordRequest();
            jsonObject.setPassword(etPassword.getText().toString());
            jsonObject.setPhone(phone);

            Call<JsonElement> call = api.forgotPasswordApi(jsonObject);
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
                                CommonUtils.showSnackbar(etPassword, message);

                                Intent intent = new Intent(EnterPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                viewMobile.setBackgroundColor(getResources().getColor(R.color.red));
                                etPassword.setTextColor(getResources().getColor(R.color.red));
                                tvErrorDetail.setText(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(etPassword, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(etPassword, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

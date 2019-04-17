package com.zing.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.request.LoginRequest;
import com.zing.model.response.LoginResponse.LoginResponse;
import com.zing.model.response.RegisterResponse.RegisterResponse;
import com.zing.notification.MyFirebaseInstanceIDService;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.tvPassword)
    TextView tvPassword;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvClickHere)
    TextView tvClickHere;
    @BindView(R.id.llForgotPass)
    LinearLayout llForgotPass;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvMobileErrorDetail)
    TextView tvMobileErrorDetail;
    @BindView(R.id.tvPasswordErrorDetail)
    TextView tvPasswordErrorDetail;
    private ProgressDialog progressDialog;
    SessionManagement session;
    @BindView(R.id.tvBack)
    TextView tvBackView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        AppTypeface.getTypeFace(this);
        session = new SessionManagement(this);

        tvWelcome.setTypeface(AppTypeface.avenieNext_demibold);
        tvLogin.setTypeface(AppTypeface.avenieNext_demibold);
        etMobile.setTypeface(AppTypeface.avenieNext_medium);
        etPassword.setTypeface(AppTypeface.avenieNext_medium);
        tvClickHere.setTypeface(AppTypeface.avenieNext_demibold);
        btnSubmit.setTypeface(AppTypeface.avenieNext_demibold);
        tvMobile.setTypeface(AppTypeface.avenieNext_demibold);
        tvPassword.setTypeface(AppTypeface.avenieNext_demibold);
        tvForgotPassword.setTypeface(AppTypeface.avenieNext_medium);
        tvMobileErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvPasswordErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvBackView.setTypeface(AppTypeface.avenieNext_demibold);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(LoginActivity.this, MyFirebaseInstanceIDService.class);
        startService(intent);
    }

    private void loginData() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {
            String deviceOs = String.valueOf(android.os.Build.VERSION.SDK_INT);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setPhone(etMobile.getText().toString());
            loginRequest.setPassword(etPassword.getText().toString());
            loginRequest.setDeviceOSversion(deviceOs);
            loginRequest.setDeviceType("Android");
            loginRequest.setDeviceToken(session.getDeviceToken());

            Call<LoginResponse> call = api.loginApi(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call,
                                       @NonNull Response<LoginResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {

                        try {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse != null && loginResponse.getResponse().getCode() == 200) {
                                session.setUserData(loginResponse.getResponse().getData().getUserId(),
                                        loginResponse.getResponse().getData().getFirstName(),
                                        loginResponse.getResponse().getData().getLastName(),
                                        loginResponse.getResponse().getData().getPhone(),
                                        loginResponse.getResponse().getData().getDataUpdated(),
                                        loginResponse.getResponse().getData().getUserToken(),
                                        loginResponse.getResponse().getData().getStatus(),
                                        loginResponse.getResponse().getData().getApt(),
                                        loginResponse.getResponse().getData().getStreetAddress(),
                                        loginResponse.getResponse().getData().getState(),
                                        loginResponse.getResponse().getData().getZipCode(),
                                        loginResponse.getResponse().getData().getSsn(),
                                        loginResponse.getResponse().getData().getProfilePic(),
                                        etPassword.getText().toString(), loginResponse.getResponse().getData().getCountryName(),
                                        loginResponse.getResponse().getData().getCountryId());

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("from", "");
                                startActivity(intent);
                                finish();

                            } else {
                                loginFailDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(etMobile, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(etMobile, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void loginFailDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);

        TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        TextView tvErrorDetail = dialog.findViewById(R.id.tvErrorDetail);
        TextView tvAccount = dialog.findViewById(R.id.tvAccount);
        TextView tvLogIn = dialog.findViewById(R.id.tvLogIn);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        tvHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvAccount.setTypeface(AppTypeface.avenieNext_medium);
        tvLogIn.setTypeface(AppTypeface.avenieNext_medium);
        btnOk.setTypeface(AppTypeface.avenieNext_demibold);

        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

    }

    @OnClick({R.id.tvClickHere, R.id.btnSubmit,R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvClickHere:
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSubmit:
                if (etMobile.getText().toString().isEmpty()) {
                    tvMobileErrorDetail.setVisibility(View.VISIBLE);
                    tvMobileErrorDetail.setText(getApplicationContext().getString(R.string.validate_emptyField));
                } else if (etPassword.getText().toString().isEmpty()) {
                    tvPasswordErrorDetail.setText(getApplicationContext().getString(R.string.validate_emptyField));
                    tvPasswordErrorDetail.setVisibility(View.VISIBLE);
                } else {
                    if (NetworkUtils.isNetworkConnected(this))
                    loginData();
                }
                break;

            case R.id.tvBack:
                finish();
                break;
        }
    }
}

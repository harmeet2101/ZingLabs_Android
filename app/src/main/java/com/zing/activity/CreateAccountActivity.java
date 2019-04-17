package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.request.PhoneModel;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
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
 * Created by savita on 3/4/18.
 */

public class CreateAccountActivity extends BaseActivity {
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvHeading)
    TextView tvHeading;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.viewMobile)
    View viewMobile;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_details);
        ButterKnife.bind(this);

        AppTypeface.getTypeFace(this);

        tvWelcome.setTypeface(AppTypeface.avenieNext_demibold);
        tvErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvHeading.setTypeface(AppTypeface.avenieNext_demibold);
        tvMobile.setTypeface(AppTypeface.avenieNext_demibold);
        etMobile.setTypeface(AppTypeface.avenieNext_medium);
        btnSubmit.setTypeface(AppTypeface.avenieNext_demibold);
    }

    private void sendOtp() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            PhoneModel jsonObject = new PhoneModel();
            jsonObject.setPhone(etMobile.getText().toString());

            Call<JsonElement> call = api.sendOtpApi(jsonObject);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {

                        try {
                            JSONObject jsonObj = new JSONObject(response.body().toString());

                            JSONObject responseObj = jsonObj.optJSONObject("response");
                            String code = responseObj.optString("code");
                            String message = responseObj.optString("message");
                            JSONObject dataObj = responseObj.optJSONObject("data");
                            String OTP = dataObj.optString("OTP");


                            if (code.equalsIgnoreCase("200")) {
                                Intent intent = new Intent(CreateAccountActivity.this, VerifyNumberActivity.class);
                                intent.putExtra("from", "registar");
                                intent.putExtra("otp", OTP);
                                intent.putExtra("phone", etMobile.getText().toString());
                                startActivity(intent);
                            } else {
                                tvErrorDetail.setText(message);
                                tvErrorDetail.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(etMobile, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(etMobile, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }

    @OnClick({R.id.btnSubmit,R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (etMobile.getText().toString().isEmpty()) {
                    tvErrorDetail.setText(getApplicationContext().getString(R.string.validate_emptyField));
                    viewMobile.setBackgroundColor(getResources().getColor(R.color.red));
                    etMobile.setTextColor(getResources().getColor(R.color.red));
                } else {
                    sendOtp();
                }
                break;

            case R.id.tvBack:
                finish();
                break;

        }
    }
}

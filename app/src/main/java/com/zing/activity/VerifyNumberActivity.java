package com.zing.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.request.PhoneModel;
import com.zing.model.request.VerifyNumberRequest;
import com.zing.model.response.otpVerifyResponse.OtpVerifyResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.Otp_screen.OnOtpCompletionListener;
import com.zing.util.Otp_screen.OtpView;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyNumberActivity extends BaseActivity {

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvHeading)
    TextView tvHeading;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    /*@BindView(R.id.etPin1)
    EditText etPin1;
    @BindView(R.id.etPin2)
    EditText etPin2;
    @BindView(R.id.etPin3)
    EditText etPin3;
    @BindView(R.id.etPin4)
    EditText etPin4;*/
    @BindView(R.id.tvResendCode)
    TextView tvResendCode;
    /*@BindView(R.id.llCode)
    LinearLayout llCode;*/
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;
   /* @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;*/
    /*@BindView(R.id.view3)
    View view3;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.llView)
    LinearLayout llView;*/
    private ProgressDialog progressDialog;
    SessionManagement session;
    private String otp, phone, from;
    private OtpView otpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.verify_number );
        ButterKnife.bind( this );
        AppTypeface.getTypeFace( this );
        session = new SessionManagement( this );
        otpView = findViewById(R.id.otp_view);
        tvWelcome.setTypeface( AppTypeface.avenieNext_demibold );
        tvHeading.setTypeface( AppTypeface.avenieNext_demibold );
        tvCode.setTypeface( AppTypeface.avenieNext_demibold );
       /* etPin1.setTypeface( AppTypeface.avenieNext_medium );
        etPin2.setTypeface( AppTypeface.avenieNext_medium );
        etPin3.setTypeface( AppTypeface.avenieNext_medium );
        etPin4.setTypeface( AppTypeface.avenieNext_medium );*/
        tvResendCode.setTypeface( AppTypeface.avenieNext_demibold );
        btnSubmit.setTypeface( AppTypeface.avenieNext_demibold );
        tvErrorDetail.setTypeface( AppTypeface.avenieNext_medium );

        tvBack.setTypeface( AppTypeface.avenieNext_medium );

        otp = getIntent().getStringExtra( "otp" );
        phone = getIntent().getStringExtra( "phone" );
        from = getIntent().getStringExtra( "from" );

        CommonUtils.showSnackbar( tvBack, otp );

        if (from.equalsIgnoreCase( "forgotPassword" )) {
            tvWelcome.setText( getResources().getString( R.string.resetPassword ) );
            tvHeading.setText( getResources().getString( R.string.verify_number ) );
           // tvBack.setVisibility( View.GONE );
        }


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                CommonUtils.hideSoftKeyboard(getApplicationContext(),otpView);
            }
        });

       /* etPin1.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

               *//* if (etPin1.getText().toString().length() == 0) {
                    etPin1.requestFocus();
                }*//*
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etPin1.getText().toString().length() == 1) {
                    etPin2.requestFocus();
                }
            }
        } );

        etPin2.addTextChangedListener( new TextWatcher() {

            boolean isBackspaceClicked = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (after < count) {
                    isBackspaceClicked = true;
                } else {
                    isBackspaceClicked = false;user_token
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               *//* if (etPin2.getText().toString().length() == 0) {
                    etPin1.requestFocus();
                }*//*
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isBackspaceClicked) {

                    if (etPin2.getText().toString().length() == 1) {
                        etPin3.requestFocus();
                    }
                } else {
                    // Your "backspace" handling
                    etPin1.requestFocus();
                }



            }
        } );

        etPin3.addTextChangedListener( new TextWatcher() {


            boolean isBackspaceClicked = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                if (after < count) {
                    isBackspaceClicked = true;
                } else {
                    isBackspaceClicked = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               *//* if (etPin3.getText().toString().length() == 0) {
                    etPin2.requestFocus();
                }*//*
            }

            @Overrideuser_token
            public void afterTextChanged(Editable s) {

                if(!isBackspaceClicked){
                    if (etPin3.getText().toString().length() == 1) {
                        etPin4.requestFocus();
                    }
                }else {
                    etPin2.requestFocus();
                }

            }
        } );


        etPin4.addTextChangedListener( new TextWatcher() {

            boolean isBackspaceClicked = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (after < count) {
                    isBackspaceClicked = true;
                } else {
                    isBackspaceClicked = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               *//* if (etPin4.getText().toString().length() == 0) {
                    etPin3.requestFocus();
                }*//*
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isBackspaceClicked){

                    CommonUtils.hideSoftKeyboard(getApplicationContext(),etPin4);
                }else {
                    etPin3.requestFocus();
                }

            }
        } );*/

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase( "otp" )) {
                final String message = intent.getStringExtra( "message" );
            }
        }
    };

    private void resendOtp() {

        otpView.setLineColor(getResources().getColor(R.color.blue));
        //setViewBlue();
        progressDialog = CommonUtils.getProgressBar( this );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );

        try {

            PhoneModel jsonObject = new PhoneModel();
            jsonObject.setPhone( phone );

            Call<JsonElement> call = api.sendOtpApi( jsonObject );
            call.enqueue( new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject( response.body().toString() );

                            JSONObject responseObj = jsonObject.optJSONObject( "response" );
                            String code = responseObj.optString( "code" );
                            String message = responseObj.optString( "message" );
                            JSONObject dataObj = responseObj.optJSONObject( "data" );
                            String OTP = dataObj.optString( "OTP" );

                            CommonUtils.showSnackbar( otpView, message );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar( otpView, response.message() );
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar( otpView, t.getMessage() );
                }
            } );
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }

    private void verifyNumber() {

        progressDialog = CommonUtils.getProgressBar( this );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );
        try {

            VerifyNumberRequest jsonObject = new VerifyNumberRequest();
            jsonObject.setOtp( otp );
            jsonObject.setPhone( phone );

            Call<OtpVerifyResponse> call = api.verifyOtpApi( jsonObject );
            call.enqueue( new Callback<OtpVerifyResponse>() {
                @Override
                public void onResponse(@NonNull Call<OtpVerifyResponse> call,
                                       @NonNull Response<OtpVerifyResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {

                            OtpVerifyResponse otpVerifyResponse = response.body();
                           /* JSONObject jsonObject = new JSONObject(response.body().toString());

                            JSONObject responseObj = jsonObject.optJSONObject("response");
                            String code = responseObj.optString("code");
                            String message = responseObj.optString("message");
*/
                            assert otpVerifyResponse != null;
                            if (otpVerifyResponse.getResponse() != null) {
                                if (otpVerifyResponse.getResponse().getCode() == 200) {
                                    CommonUtils.showSnackbar( otpView, otpVerifyResponse.getResponse().getMessage() );

                                    Intent intent = new Intent( VerifyNumberActivity.this, EnterPasswordActivity.class );
                                    intent.putExtra( "basicInfo", otpVerifyResponse.getResponse().getData().getBasicInfo() );
                                    //intent.putExtra("preferences",otpVerifyResponse.getResponse().getData().getPreferenceInfo());
                                    intent.putExtra( "from", from );
                                    intent.putExtra( "phone", phone );
                                    startActivity( intent );
                                } else {
                                    //setViewRed();78
                                    otpView.setLineColor(getResources().getColor(R.color.red));
                                    tvErrorDetail.setText( otpVerifyResponse.getResponse().getMessage() );
                                }
                            } else {
                                //setViewRed();
                                otpView.setLineColor(getResources().getColor(R.color.red));
                                tvErrorDetail.setText( otpVerifyResponse.getResponse().getMessage() );
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar( otpView, response.message() );
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OtpVerifyResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar( otpView, t.getMessage() );
                }
            } );
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

   /* private void setViewRed() {
        view1.setBackgroundColor( getResources().getColor( R.color.red ) );
        view2.setBackgroundColor( getResources().getColor( R.color.red ) );
        view3.setBackgroundColor( getResources().getColor( R.color.red ) );
        view4.setBackgroundColor( getResources().getColor( R.color.red ) );
        etPin1.setTextColor( getResources().getColor( R.color.red ) );
        etPin2.setTextColor( getResources().getColor( R.color.red ) );
        etPin3.setTextColor( getResources().getColor( R.color.red ) );
        etPin4.setTextColor( getResources().getColor( R.color.red ) );
    }

    void setViewBlue() {
        tvErrorDetail.setText( "" );
        etPin1.setFocusable( true );
        view1.setBackgroundColor( getResources().getColor( R.color.blue ) );
        view2.setBackgroundColor( getResources().getColor( R.color.blue ) );
        view3.setBackgroundColor( getResources().getColor( R.color.blue ) );
        view4.setBackgroundColor( getResources().getColor( R.color.blue ) );
        etPin1.setTextColor( getResources().getColor( R.color.blue ) );
        etPin2.setTextColor( getResources().getColor( R.color.blue ) );
        etPin3.setTextColor( getResources().getColor( R.color.blue ) );
        etPin4.setTextColor( getResources().getColor( R.color.blue ) );
    }*/

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission( this,
                Manifest.permission.SEND_SMS );

        int receiveSMS = ContextCompat.checkSelfPermission( this,
                Manifest.permission.RECEIVE_SMS );

        int readSMS = ContextCompat.checkSelfPermission( this,
                Manifest.permission.READ_SMS );
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.RECEIVE_MMS );
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.READ_SMS );
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.SEND_SMS );
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions( this,
                    listPermissionsNeeded.toArray( new String[listPermissionsNeeded.size()] ),
                    REQUEST_ID_MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance( this ).
                registerReceiver( receiver, new IntentFilter( "otp" ) );
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance( this ).unregisterReceiver( receiver );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.tvResendCode, R.id.btnSubmit, R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvResendCode:
               /* etPin1.setText( "" );
                etPin2.setText( "" );
                etPin3.setText( "" );
                etPin4.setText( "" );*/
                otpView.setText("");
                if (NetworkUtils.isNetworkConnected( this ))
                    resendOtp();
                break;
            case R.id.btnSubmit:
                otp = otpView.getText().toString();
/*                otp = etPin1.getText().toString() + etPin2.getText().toString()
                        + etPin3.getText().toString() + etPin4.getText().toString();*/
                if (otp.length() < 4) {
                    //setViewRed();
                    otpView.setLineColor(getResources().getColor(R.color.red));
                    tvErrorDetail.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                } else {
                    if (NetworkUtils.isNetworkConnected( this ))
                        verifyNumber();
                }
                break;
            case R.id.tvBack:
                onBackPressed();
                break;

        }
    }







}
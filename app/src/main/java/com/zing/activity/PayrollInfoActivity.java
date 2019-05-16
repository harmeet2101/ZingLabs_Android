package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.request.CompleteProfileRequest;
import com.zing.model.request.LoginRequest;
import com.zing.model.response.LoginResponse.LoginResponse;
import com.zing.model.response.RegisterResponse.RegisterResponse;
import com.zing.model.response.otpVerifyResponse.BasicInfo;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.notification.MyFirebaseInstanceIDService;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zing.util.CommonUtils.showSnackbar;

/**
 * Created by abhishek on 28/5/18.
 */

public class PayrollInfoActivity extends BaseActivity {
    @BindView(R.id.tvPayroll)
    TextView tvPayroll;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;
    @BindView(R.id.tvSsn)
    TextView tvSsn;
    @BindView(R.id.etSsn)
    EditText etSsn;
    @BindView(R.id.viewFirstName)
    View viewFirstName;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;
    private ProgressDialog progressDialog;
    SessionManagement session;
    private String password, phone, imgString, firstName, lastName, streetAddress, apt, state, zip;
    BasicInfo basicInfo;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.payroll_info_activity );
        ButterKnife.bind( this );
        mContext = PayrollInfoActivity.this;
        session = new SessionManagement( PayrollInfoActivity.this );
        AppTypeface.getTypeFace( this );
        tvPayroll.setTypeface( AppTypeface.avenieNext_demibold );
        tvInfo.setTypeface( AppTypeface.avenieNext_demibold );
        tvSsn.setTypeface( AppTypeface.avenieNext_demibold );
        etSsn.setTypeface( AppTypeface.avenieNext_medium );
        btnNext.setTypeface( AppTypeface.avenieNext_demibold );
        tvBack.setTypeface( AppTypeface.avenieNext_medium );
        tvErrorDetail.setTypeface( AppTypeface.avenieNext_medium );

        if (getIntent().hasExtra( "basicInfo" ))
            basicInfo = (BasicInfo) getIntent().getSerializableExtra( "basicInfo" );
        setData( basicInfo );

        password = getIntent().getStringExtra( "password" );
        phone = getIntent().getStringExtra( "phone" );
        firstName = getIntent().getStringExtra( "firstName" );
        lastName = getIntent().getStringExtra( "lastName" );
        streetAddress = getIntent().getStringExtra( "streetAddress" );
        apt = getIntent().getStringExtra( "apt" );
        state = getIntent().getStringExtra( "state" );
        zip = getIntent().getStringExtra( "zip" );


        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File directory = wrapper.getDir("Images", Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile_pic.jpg");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(mypath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }if (b!=null) {
            civProfileImage.setImageBitmap(b);
            imgString = getImageString(b);
        }
    }


    void setData(BasicInfo basicInfo) {
        etSsn.setText( String.format( "%s", basicInfo.getSsn() ) );

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(PayrollInfoActivity.this, MyFirebaseInstanceIDService.class);
        startService(intent);
    }

    @OnClick({ R.id.btnNext, R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (etSsn.getText().toString().isEmpty()) {
                    tvErrorDetail.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                    viewFirstName.setBackgroundColor( getResources().getColor( R.color.red ) );
                } else {
                    if (NetworkUtils.isNetworkConnected( this ))
                        updateProfile();
                }
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private String getImageString(Bitmap image) {

        String imgString="";
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        }catch (Exception e){
            e.printStackTrace();
        }
        return imgString;

    }


    private void updateProfile() {
        TimeZone timezone = TimeZone.getDefault();
        String timeZone = timezone.getID();

        progressDialog = CommonUtils.getProgressBar( this );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );
        try {

            CompleteProfileRequest completeProfileRequest = new CompleteProfileRequest();
            completeProfileRequest.setDataUpdated( "1" );
            completeProfileRequest.setCountryId( basicInfo.getCountryId() );
            completeProfileRequest.setPassword( password );
            completeProfileRequest.setFirstName( firstName );
            completeProfileRequest.setLastName( lastName );
            completeProfileRequest.setProfileImage( Constants.imageType + imgString );
            completeProfileRequest.setApt( apt );
            completeProfileRequest.setPhone( phone );
            completeProfileRequest.setSsn( etSsn.getText().toString() );
            completeProfileRequest.setState( state );
            completeProfileRequest.setStreetAddress( streetAddress );
            completeProfileRequest.setZipCode( zip );
            completeProfileRequest.setTimeZone( timeZone );
            completeProfileRequest.setIs_new_image( "1" );
            completeProfileRequest.setDeviceToken(session.getDeviceToken());
            Call<RegisterResponse> call = api.updateProfileApi( "Bearer " + basicInfo.getUserToken(),
                    completeProfileRequest );
            call.enqueue( new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call,
                                       @NonNull Response<RegisterResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            RegisterResponse registerResponse = response.body();
                            if (registerResponse != null && registerResponse.getResponse().getCode() == 200) {
                                session.setUserData( registerResponse.getResponse().getData().getUserId(),
                                        registerResponse.getResponse().getData().getFirstName(),
                                        registerResponse.getResponse().getData().getLastName(),
                                        registerResponse.getResponse().getData().getPhone(),
                                        registerResponse.getResponse().getData().getDataUpdated(),
                                        basicInfo.getUserToken(),
                                        registerResponse.getResponse().getData().getStatus(),
                                        registerResponse.getResponse().getData().getApt(),
                                        registerResponse.getResponse().getData().getStreetAddress(),
                                        registerResponse.getResponse().getData().getState(),
                                        registerResponse.getResponse().getData().getZipCode(),
                                        registerResponse.getResponse().getData().getSsn(),
                                        registerResponse.getResponse().getData().getProfilePic(),
                                        password,
                                        registerResponse.getResponse().getData().getCountryName(),
                                        registerResponse.getResponse().getData().getCountryId() );

                                loginData();
                               // Toast.makeText( mContext, registerResponse.getResponse().getData().getCountryName(), Toast.LENGTH_LONG ).show();
                                Intent intent = new Intent( PayrollInfoActivity.this, TimePreferencesActivity.class );
                                //intent.putExtra("preferences",preferenceInfo);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                showSnackbar( etSsn, registerResponse.getResponse().getMessage() );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showSnackbar( etSsn, response.message() );
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar( etSsn, t.getMessage() );
                }
            } );
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }


    private void loginData() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {
            String deviceOs = String.valueOf(android.os.Build.VERSION.SDK_INT);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setPhone(phone);
            loginRequest.setPassword(password);
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
                                        password, loginResponse.getResponse().getData().getCountryName(),
                                        loginResponse.getResponse().getData().getCountryId());


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(etSsn, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}

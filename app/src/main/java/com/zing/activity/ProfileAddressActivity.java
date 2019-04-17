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
import android.support.v7.widget.ListPopupWindow;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zing.R;
import com.zing.adapter.CountryAdapter;
import com.zing.base.BaseActivity;
import com.zing.model.response.countryListResponse.CountryResponse;
import com.zing.model.response.countryListResponse.Data;
import com.zing.model.response.otpVerifyResponse.BasicInfo;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhishek on 28/5/18.
 */

public class ProfileAddressActivity extends BaseActivity {
    @BindView(R.id.tvPersonal)
    TextView tvPersonal;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;
    @BindView(R.id.tvStreetAddress)
    TextView tvStreetAddress;
    @BindView(R.id.etStreetAddress)
    EditText etStreetAddress;
    @BindView(R.id.viewStreetAddress)
    View viewStreetAddress;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.viewState)
    View viewState;
    @BindView(R.id.tvZipCode)
    TextView tvZipCode;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.viewZipCode)
    View viewZipCode;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvStreetError)
    TextView tvStreetError;
    @BindView(R.id.tvApt)
    TextView tvApt;
    @BindView(R.id.etApt)
    EditText etApt;
    @BindView(R.id.viewApt)
    View viewApt;
    @BindView(R.id.tvAptError)
    TextView tvAptError;
    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.viewCounty)
    View viewCounty;
    @BindView(R.id.tvCountryError)
    TextView tvCountryError;
    BasicInfo basicInfo;
    //private PreferenceInfo preferenceInfo;
    ListPopupWindow selectCountry;
    Context mContext;
    CountryAdapter countryAdapter;
    List<Data> countryList = new ArrayList<>();
    int selectCountryPos;
    String countryId;
    private String password, phone, firstName, lastName;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.profile_address_activity );
        ButterKnife.bind( this );
        countryListApi();
        mContext = ProfileAddressActivity.this;
        if (getIntent().hasExtra( "basicInfo" ))
            basicInfo = (BasicInfo) getIntent().getSerializableExtra( "basicInfo" );
        //preferenceInfo = (PreferenceInfo)getIntent().getSerializableExtra("preferences");
        setData( basicInfo );
        AppTypeface.getTypeFace( this );
        tvPersonal.setTypeface( AppTypeface.avenieNext_demibold );
        tvInfo.setTypeface( AppTypeface.avenieNext_demibold );
        tvStreetAddress.setTypeface( AppTypeface.avenieNext_demibold );
        etStreetAddress.setTypeface( AppTypeface.avenieNext_medium );
        tvApt.setTypeface( AppTypeface.avenieNext_demibold );
        etApt.setTypeface( AppTypeface.avenieNext_medium );
        tvState.setTypeface( AppTypeface.avenieNext_demibold );
        etState.setTypeface( AppTypeface.avenieNext_medium );
        tvZipCode.setTypeface( AppTypeface.avenieNext_demibold );
        etZipCode.setTypeface( AppTypeface.avenieNext_medium );

        //
        tvCountry.setTypeface( AppTypeface.avenieNext_demibold );
        etCountry.setTypeface( AppTypeface.avenieNext_medium );

        btnNext.setTypeface( AppTypeface.avenieNext_demibold );

        tvBack.setTypeface( AppTypeface.avenieNext_medium );
        tvErrorDetail.setTypeface( AppTypeface.avenieNext_medium );
        tvStreetError.setTypeface( AppTypeface.avenieNext_medium );
        tvAptError.setTypeface( AppTypeface.avenieNext_medium );

        password = getIntent().getStringExtra( "password" );
        phone = getIntent().getStringExtra( "phone" );
        firstName = getIntent().getStringExtra( "firstName" );
        lastName = getIntent().getStringExtra( "lastName" );

        setListPopup();

       /* byte[] decodedString = Base64.decode( imgString, Base64.DEFAULT );
        Bitmap decodedByte = BitmapFactory.decodeByteArray( decodedString, 0, decodedString.length );
        civProfileImage.setImageBitmap( decodedByte );*/

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File directory = wrapper.getDir("Images", Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile_pic.jpg");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(mypath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        if(b!=null)
            civProfileImage.setImageBitmap( b );
    }

    void setData(BasicInfo basicInfo) {
        etStreetAddress.setText( String.format( "%s", basicInfo.getStreetAddress() ) );
        etState.setText( String.format( "%s", basicInfo.getState() ) );
        etZipCode.setText( String.format( "%s", basicInfo.getZipCode() ) );
        etApt.setText( String.format( "%s", basicInfo.getApt() ) );
        if (!basicInfo.getCountryName().isEmpty()) {

            etCountry.setText( basicInfo.getCountryName() );
            countryId = basicInfo.getCountryId();
        }

    }

    @OnClick({R.id.btnNext, R.id.tvBack, R.id.etCountry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (etStreetAddress.getText().toString().isEmpty()) {
                    tvStreetError.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                    viewStreetAddress.setBackgroundColor( getResources().getColor( R.color.red ) );
                } else if (etState.getText().toString().isEmpty()) {
                    tvErrorDetail.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                    viewState.setBackgroundColor( getResources().getColor( R.color.red ) );
                } else if (etCountry.getText().toString().isEmpty()) {
                    tvErrorDetail.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                    viewCounty.setBackgroundColor( getResources().getColor( R.color.red ) );

                } else if (etZipCode.getText().toString().isEmpty()) {
                    tvErrorDetail.setText( getApplicationContext().getString( R.string.validate_emptyField ) );
                    viewZipCode.setBackgroundColor( getResources().getColor( R.color.red ) );
                } else {

                    basicInfo.setCountryId( countryList.get( selectCountryPos ).getCountryId() );
                    basicInfo.setCountryName( countryList.get( selectCountryPos ).getName() );


                    Intent intent = new Intent( ProfileAddressActivity.this, PayrollInfoActivity.class );
                    intent.putExtra( "phone", phone );
                    intent.putExtra( "password", password );
                    intent.putExtra( "firstName", firstName );
                    intent.putExtra( "lastName", lastName );
                    intent.putExtra( "basicInfo", basicInfo );
                    //intent.putExtra("preferences",preferenceInfo);
                    intent.putExtra( "streetAddress", etStreetAddress.getText().toString() );
                    intent.putExtra( "apt", etApt.getText().toString() );
                    intent.putExtra( "state", etState.getText().toString() );
                    intent.putExtra( "zip", etZipCode.getText().toString() );
                    startActivity( intent );
                }
                break;
            case R.id.tvBack:
                onBackPressed();
                break;
            case R.id.etCountry:
                selectCountry.show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void setListPopup() {


        selectCountry = new ListPopupWindow( mContext );


        countryAdapter = new CountryAdapter( mContext );

        selectCountry.setAdapter( countryAdapter );
        selectCountry.setModal( true );
        selectCountry.setAnchorView( etCountry );
        selectCountry.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCountry.dismiss();
                etCountry.setText( countryList.get( position ).getName() );
                selectCountryPos = position;

            }
        } );


    }

    private void countryListApi() {


        progressDialog = CommonUtils.getProgressBar( this );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );
        Call<CountryResponse> call = api.getCountryListApi();
        call.enqueue( new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call,
                                   @NonNull Response<CountryResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    CountryResponse countryResponse = response.body();

                    assert countryResponse != null;
                    countryList.addAll( countryResponse.getData() );
                    if (!basicInfo.getCountryName().isEmpty()) {
                        for (int i = 0; i < countryList.size(); i++) {
                            if (countryList.get( i ).getName().equalsIgnoreCase( basicInfo.getCountryName() )) {
                                selectCountryPos = i;
                                break;
                            }
                        }
                    }

                    if (countryAdapter != null)
                        countryAdapter.addAllData( countryList );


                } else {
//                        CommonUtils.showSnackbar(etSsn, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
//                    CommonUtils.showSnakBar(etSsn, t.getMessage());
            }
        } );


    }

}

package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.response.otpVerifyResponse.BasicInfo;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class CompleteProfileActivity extends BaseActivity {

    @BindView(R.id.tvCreate)
    TextView tvCreate;
    @BindView(R.id.tvAcc)
    TextView tvAcc;
    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;
    @BindView(R.id.tvEditPhoto)
    TextView tvEditPhoto;
    @BindView(R.id.tvFirstName)
    TextView tvFirstName;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.viewFirstName)
    View viewFirstName;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.viewLastName)
    View viewLastName;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;
    @BindView(R.id.tvLastNameErrorDetail)
    TextView tvLastNameErrorDetail;
    @BindView(R.id.editPhoto)
    View editPhotoView;
    private String  password, phone;
    private BasicInfo basicInfo;
    @BindView(R.id.tvBack)
    TextView tvBackView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_profile);
        ButterKnife.bind(this);

        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");


        if(getIntent().hasExtra("basicInfo"))
        basicInfo=(BasicInfo) getIntent().getSerializableExtra("basicInfo");
        setData(basicInfo);

        AppTypeface.getTypeFace(this);
        tvCreate.setTypeface(AppTypeface.avenieNext_demibold);
        tvAcc.setTypeface(AppTypeface.avenieNext_demibold);

        tvEditPhoto.setTypeface(AppTypeface.avenieNext_demibold);
        tvLastName.setTypeface(AppTypeface.avenieNext_demibold);
        tvFirstName.setTypeface(AppTypeface.avenieNext_demibold);
        btnNext.setTypeface(AppTypeface.avenieNext_demibold);
        etFirstName.setTypeface(AppTypeface.avenieNext_demibold);
        etLastName.setTypeface(AppTypeface.avenieNext_demibold);
        tvErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvLastNameErrorDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvBackView.setTypeface(AppTypeface.avenieNext_medium);

        addProfilePic();
    }


    private void addProfilePic(){


        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File directory = wrapper.getDir("Images", Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile_pic.jpg");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(mypath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        civProfileImage.setImageBitmap(b);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        addProfilePic();

    }

    void setData(BasicInfo basicInfo){

        etFirstName.setText(""+basicInfo.getFirstName());
        etLastName.setText(""+basicInfo.getLastName());

    }

    @OnClick({R.id.editPhoto, R.id.btnNext,R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tvBack:
                finish();
                break;

            case R.id.editPhoto:
                Intent editIntent = new Intent(CompleteProfileActivity.this, MainActivity.class);
                startActivity(editIntent);
                break;
            case R.id.btnNext:
                if (etFirstName.getText().toString().isEmpty()) {
                    tvErrorDetail.setText(getApplicationContext().getString(R.string.validate_emptyField));
                    viewFirstName.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (etLastName.getText().toString().isEmpty()) {
                    tvLastNameErrorDetail.setText(getApplicationContext().getString(R.string.validate_emptyField));
                    viewLastName.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    basicInfo.setFirstName( etFirstName.getText().toString() );
                    basicInfo.setLastName( etLastName.getText().toString() );
                    Intent intent = new Intent(CompleteProfileActivity.this, ProfileAddressActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("password", password);
                    intent.putExtra("basicInfo",basicInfo);
                    intent.putExtra("firstName", etFirstName.getText().toString());
                    intent.putExtra("lastName", etLastName.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }

}

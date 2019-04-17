package com.zing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.util.AppTypeface;
import com.zing.util.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abhishek on 7/8/18.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.create_account)
    Button createAccount;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvLogIn)
    TextView tvLogIn;
    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        AppTypeface.getTypeFace(this);
        session = new SessionManagement(this);

        tvInfo.setTypeface(AppTypeface.avenieNext_regular);
        createAccount.setTypeface(AppTypeface.avenieNext_demibold);
        tvAccount.setTypeface(AppTypeface.avenieNext_medium);
        tvLogIn.setTypeface(AppTypeface.avenieNext_medium);
    }

    Intent intent;

    @OnClick({R.id.create_account, R.id.tvLogIn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_account:
                intent = new Intent(RegisterActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tvLogIn:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

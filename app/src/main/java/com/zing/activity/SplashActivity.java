package com.zing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.util.AppTypeface;
import com.zing.util.SessionManagement;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {
    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        AppTypeface.getTypeFace(this);
        session = new SessionManagement(this);


        
        if (session.isLoggedIn().equalsIgnoreCase("1")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.putExtra("from", "splash");
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
//                    intent.putExtra("from", "splash");
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

}

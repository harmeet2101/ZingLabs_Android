package com.zing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.notification.MyFirebaseInstanceIDService;
import com.zing.util.AppTypeface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TimePreferencesActivity extends BaseActivity {
    @BindView(R.id.btnSetPreference)
    Button btnSetPreference;
    @BindView(R.id.tvSkipStep)
    TextView tvSkipStep;
    @BindView(R.id.tvTimePreferences)
    TextView tvTimePreferences;
    @BindView(R.id.tvTimePreferencesDescription)
    TextView tvTimePreferencesDescription;
    private Intent intent;

    //private PreferenceInfo preferenceInfo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_preferences);
        ButterKnife.bind(this);

        AppTypeface.getTypeFace(this);

        tvTimePreferencesDescription.setTypeface(AppTypeface.avenieNext_demibold);
        tvTimePreferences.setTypeface(AppTypeface.avenieNext_demibold);
        btnSetPreference.setTypeface(AppTypeface.avenieNext_demibold);
        tvSkipStep.setTypeface(AppTypeface.avenieNext_demibold);
      //  preferenceInfo = (PreferenceInfo) getIntent().getSerializableExtra("preferences");
    }

    @OnClick({R.id.btnSetPreference, R.id.tvSkipStep})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSetPreference:
                intent = new Intent(TimePreferencesActivity.this, PreferenceCalenderActivity.class);
                intent.putExtra("from", "account");
                //intent.putExtra("preferences",preferenceInfo);
                startActivity(intent);
                finish();
                break;
            case R.id.tvSkipStep:
                intent = new Intent(TimePreferencesActivity.this, DashboardActivity.class);
                intent.putExtra("from", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
}

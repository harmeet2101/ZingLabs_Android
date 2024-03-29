package com.zing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zing.R;
import com.zing.base.BaseActivity;

import androidx.annotation.Nullable;

public class BadgeDetailsActivity extends BaseActivity implements View.OnClickListener {


    ImageView closeImageview;
    ImageView badgeImageView;
    TextView tvBadgeInfo,tvBadgeDetail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_badges_details);
        closeImageview = (ImageView)findViewById(R.id.close);
        badgeImageView = (ImageView)findViewById(R.id.badgeImage);
        tvBadgeInfo = (TextView)findViewById(R.id.tvBadgeInfo);
        tvBadgeDetail = (TextView)findViewById(R.id.tvBadgeDetail);
        closeImageview.setOnClickListener(this);

        String badgeType = getIntent().getExtras().getString("badgeType");
        String badgeInfo = getIntent().getExtras().getString("badgeInfo");
        String badgeDetail = getIntent().getExtras().getString("badgeDetail");

        setUpData(badgeType);

        tvBadgeInfo.setText(badgeInfo);
        tvBadgeDetail.setText(badgeDetail);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.close:
                finish();
                break;
        }
    }

    private  void setUpData(String type){

        switch (type){

            case "Completed":
                tvBadgeInfo.setText("First Shift Completed");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_first_shift_completed_144x144));
                break;
            case "OnTime_5":
                tvBadgeInfo.setText("Show up on time");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_show_up_on_time_5_144x144));
                break;
            case "OnTime_10":
                tvBadgeInfo.setText("Show up on time");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_show_up_on_time_10_144x144));
                break;
            case "OnTime_20":
                tvBadgeInfo.setText("Show up on time");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_show_up_on_time_20_144x144));
                break;
            case "OnTime_50":
                tvBadgeInfo.setText("Show up on time");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_show_up_on_time_50_144x144));
                break;
            case "OnTime_100":
                tvBadgeInfo.setText("Show up on time");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_show_up_on_time_100_144x144));
                break;
            case "Perfect_w":
                tvBadgeInfo.setText("Perfect");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_perfect_week_144x144));
                break;

            case "Perfect_m":
                tvBadgeInfo.setText("Perfect");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_perfect_month_144x144));
                break;
            case "Recommended_1":
                tvBadgeInfo.setText("Recommended Shift");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_picked_up_recommended_shifts_1_144x144));
                break;
            case "Recommended_5":
                tvBadgeInfo.setText("Recommended Shift");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_rec_5_144x144));
                break;

            case "Recommended_10":
                tvBadgeInfo.setText("Recommended Shift");
                badgeImageView.setImageDrawable(getResources().getDrawable(R.drawable.badge_rec_10_144x144));
                break;
        }
    }
}

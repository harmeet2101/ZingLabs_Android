package com.zing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.activity.BadgeDetailsActivity;
import com.zing.adapter.BadgesAdapter;
import com.zing.model.response.StatsResponse.Badge;
import com.zing.model.response.StatsResponse.StatsResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyStatsFragment extends BaseFragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;

    @BindView(R.id.tvShowUpRate)
    TextView tvShowUpRate;
    @BindView(R.id.tvShowUpDescription)
    TextView tvShowUpDescription;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvShowUpRateHeading)
    TextView tvShowUpRateHeading;

    @BindView(R.id.firstShiftLayout)
    ViewGroup firstShiftLayout;
    @BindView(R.id.onTimeShiftLayout)
    ViewGroup onTimeShiftLayout;
    @BindView(R.id.perfectShiftLayout)
    ViewGroup perfectShiftLayout;
    @BindView(R.id.recommendShiftLayout)
    ViewGroup recommendShiftLayout;
    @BindView(R.id.ImageView_show_up_on_time_5)
    ImageView imageView_show_up_on_time_5;
    @BindView(R.id.ImageView_show_up_on_time_10)
    ImageView imageView_show_up_on_time_10;
    @BindView(R.id.ImageView_show_up_on_time_20)
    ImageView imageView_show_up_on_time_20;
    @BindView(R.id.ImageView_show_up_on_time_50)
    ImageView imageView_show_up_on_time_50;
    @BindView(R.id.ImageView_show_up_on_time_100)
    ImageView imageView_show_up_on_time_100;
    @BindView(R.id.ImageView_perfect_week)
    ImageView imageView_perfect_week;
    @BindView(R.id.ImageView_perfect_month)
    ImageView imageView_perfect_month;

    @BindView(R.id.ImageView_recommended_shift_1)
    ImageView imageView_recommended_shift_1;
    @BindView(R.id.ImageView_recommended_shift_5)
    ImageView imageView_recommended_shift_5;
    @BindView(R.id.ImageView_recommended_shift_10)
    ImageView imageView_recommended_shift_10;

    @BindView(R.id.ImageView_shift_completed)
    ImageView imageView_shift_completed;
    @BindView(R.id.tvHeading)
    TextView tvBadges;

    ViewGroup weekView;
    ViewGroup monthView;

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    SessionManagement session;

    public static MyStatsFragment newInstance(String param1, String param2) {
        MyStatsFragment fragment = new MyStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_stats, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());

        weekView = (ViewGroup)getActivity().findViewById(R.id.weekView);
        monthView = (ViewGroup)getActivity().findViewById(R.id.monthView);
        session = new SessionManagement(getActivity());
        tvShowUpRate.setTypeface(AppTypeface.avenieNext_medium);
        tvShowUpRateHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvRate.setTypeface(AppTypeface.avenieNext_medium);
        tvShowUpDescription.setTypeface(AppTypeface.avenieNext_regular);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
        statsDetails();
    }

    private void statsDetails() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<StatsResponse> call = api.statsDetailApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<StatsResponse>() {
                @Override
                public void onResponse(@NonNull Call<StatsResponse> call,
                                       @NonNull Response<StatsResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            StatsResponse statsResponse = response.body();
                            tvRate.setText(statsResponse.getResponse().getShow_rate().getRate()+"%");
                            tvShowUpDescription.setText(statsResponse.getResponse().getShow_rate().getDescription());
                            tvShowUpRate.setText(statsResponse.getResponse().getShow_rate().getLabel());


                            ((TextView)weekView.findViewById(R.id.counterTextview)).setText(""+statsResponse.getResponse().getBadges().getPerfect_week());
                            ((TextView)monthView.findViewById(R.id.counterTextview)).setText(""+statsResponse.getResponse().getBadges().getPerfect_month());

                           /* if(statsResponse.getResponse().getBadges().getFirst_shift_completed()
                                ==0 && statsResponse.getResponse().getBadges().getShow_up_on_time()<5

                                    && statsResponse.getResponse().getBadges().getPerfect_week()==0
                                    && statsResponse.getResponse().getBadges().getPerfect_month()==0
                                    && statsResponse.getResponse().getBadges().getRecommended_shift_picked()==0

                            ){
                                tvBadges.setText("No Badges Found");
                            }*/
                            if(statsResponse.getResponse().getBadges().getFirst_shift_completed()==0) {
                                imageView_shift_completed.setImageResource(R.drawable.badge_first_shift_completed_144x144_grey);
                                imageView_shift_completed.setClickable(true);
                            }
                            else {
                                imageView_shift_completed.setImageResource(R.drawable.badge_first_shift_completed_144x144);
                                imageView_shift_completed.setClickable(false);
                            }
                            /*if(statsResponse.getResponse().getBadges().getShow_up_on_time()==0)
                                onTimeShiftLayout.setVisibility(View.GONE);
                            else
                                onTimeShiftLayout.setVisibility(View.VISIBLE);*/

                           /* if(statsResponse.getResponse().getBadges().getRecommended_shift_picked()==0)
                                recommendShiftLayout.setVisibility(View.GONE);
                            else
                                recommendShiftLayout.setVisibility(View.VISIBLE);*/

                            if(statsResponse.getResponse().getBadges().getPerfect_week()==0
                                    && statsResponse.getResponse().getBadges().getPerfect_month() ==0){

                                imageView_perfect_week.setImageResource(R.drawable.badge_perfect_week_144x144_grey);
                                imageView_perfect_month.setImageResource(R.drawable.badge_perfect_month_144x144_grey);
                                imageView_perfect_week.setClickable(false);
                                imageView_perfect_month.setClickable(false);

                            }

                            else if(statsResponse.getResponse().getBadges().getPerfect_week()==0
                                    && statsResponse.getResponse().getBadges().getPerfect_month()!=0){

                                imageView_perfect_week.setImageResource(R.drawable.badge_perfect_week_144x144_grey);
                                imageView_perfect_month.setImageResource(R.drawable.badge_perfect_month_144x144);

                                imageView_perfect_week.setClickable(false);
                                imageView_perfect_month.setClickable(true);

                            }else if(statsResponse.getResponse().getBadges().getPerfect_month()==0
                                    && statsResponse.getResponse().getBadges().getPerfect_week()!=0){
                                imageView_perfect_month.setImageResource(R.drawable.badge_perfect_month_144x144_grey);
                                imageView_perfect_week.setImageResource(R.drawable.badge_perfect_week_144x144);

                                imageView_perfect_week.setClickable(true);
                                imageView_perfect_month.setClickable(false);
                            }


                            imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144_grey);
                            imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144_grey);
                            imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144_grey);
                            imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144_grey);
                            imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                            if(statsResponse.getResponse().getBadges().getShow_up_on_time()<5){

                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144_grey);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144_grey);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144_grey);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144_grey);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                                imageView_show_up_on_time_5.setClickable(false);
                                imageView_show_up_on_time_10.setClickable(false);
                                imageView_show_up_on_time_20.setClickable(false);
                                imageView_show_up_on_time_50.setClickable(false);
                                imageView_show_up_on_time_100.setClickable(false);

                            }

                            else if(statsResponse.getResponse().getBadges().getShow_up_on_time()>=5
                                    && statsResponse.getResponse().getBadges().getShow_up_on_time() < 10) {
                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144_grey);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144_grey);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144_grey);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                                imageView_show_up_on_time_5.setClickable(true);
                                imageView_show_up_on_time_10.setClickable(false);
                                imageView_show_up_on_time_20.setClickable(false);
                                imageView_show_up_on_time_50.setClickable(false);
                                imageView_show_up_on_time_100.setClickable(false);

                            }
                            else if(statsResponse.getResponse().getBadges().getShow_up_on_time() >=10
                                    && statsResponse.getResponse().getBadges().getShow_up_on_time() <= 19) {

                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144_grey);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144_grey);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                                imageView_show_up_on_time_5.setClickable(true);
                                imageView_show_up_on_time_10.setClickable(true);
                                imageView_show_up_on_time_20.setClickable(false);
                                imageView_show_up_on_time_50.setClickable(false);
                                imageView_show_up_on_time_100.setClickable(false);
                            }
                            else if(statsResponse.getResponse().getBadges().getShow_up_on_time() >=20
                                    && statsResponse.getResponse().getBadges().getShow_up_on_time() <49) {

                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144_grey);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                                imageView_show_up_on_time_5.setClickable(true);
                                imageView_show_up_on_time_10.setClickable(true);
                                imageView_show_up_on_time_20.setClickable(true);
                                imageView_show_up_on_time_50.setClickable(false);
                                imageView_show_up_on_time_100.setClickable(false);

                            }
                            else  if(statsResponse.getResponse().getBadges().getShow_up_on_time() >= 50
                                    &&  statsResponse.getResponse().getBadges().getShow_up_on_time() < 100) {
                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144_grey);

                                imageView_show_up_on_time_5.setClickable(true);
                                imageView_show_up_on_time_10.setClickable(true);
                                imageView_show_up_on_time_20.setClickable(true);
                                imageView_show_up_on_time_50.setClickable(true);
                                imageView_show_up_on_time_100.setClickable(false);

                            }
                            else  {
                                imageView_show_up_on_time_5.setImageResource(R.drawable.badge_show_up_on_time_5_144x144);
                                imageView_show_up_on_time_10.setImageResource(R.drawable.badge_show_up_on_time_10_144x144);
                                imageView_show_up_on_time_20.setImageResource(R.drawable.badge_show_up_on_time_20_144x144);
                                imageView_show_up_on_time_50.setImageResource(R.drawable.badge_show_up_on_time_50_144x144);
                                imageView_show_up_on_time_100.setImageResource(R.drawable.badge_show_up_on_time_100_144x144);

                                imageView_show_up_on_time_5.setClickable(true);
                                imageView_show_up_on_time_10.setClickable(true);
                                imageView_show_up_on_time_20.setClickable(true);
                                imageView_show_up_on_time_50.setClickable(true);
                                imageView_show_up_on_time_100.setClickable(true);
                            }



                            imageView_recommended_shift_1.setImageResource(R.drawable.badge_picked_up_recommended_shifts_1_144x144_grey);
                            imageView_recommended_shift_5.setImageResource(R.drawable.badge_rec_5_144x144_grey);
                            imageView_recommended_shift_10.setImageResource(R.drawable.badge_rec_10_144x144_grey);

                            imageView_recommended_shift_1.setClickable(false);
                            imageView_recommended_shift_5.setClickable(false);
                            imageView_recommended_shift_10.setClickable(false);
                            if(statsResponse.getResponse().getBadges().getRecommended_shift_picked() > 0
                                    && statsResponse.getResponse().getBadges().getRecommended_shift_picked() < 5) {
                                imageView_recommended_shift_1.setImageResource(R.drawable.badge_picked_up_recommended_shifts_1_144x144);
                                imageView_recommended_shift_5.setImageResource(R.drawable.badge_rec_5_144x144_grey);
                                imageView_recommended_shift_10.setImageResource(R.drawable.badge_rec_10_144x144_grey);

                                imageView_recommended_shift_1.setClickable(true);
                                imageView_recommended_shift_5.setClickable(false);
                                imageView_recommended_shift_10.setClickable(false);
                            }

                            else if(statsResponse.getResponse().getBadges().getRecommended_shift_picked() >=5
                                    && statsResponse.getResponse().getBadges().getRecommended_shift_picked() <10) {

                                imageView_recommended_shift_1.setImageResource(R.drawable.badge_picked_up_recommended_shifts_1_144x144);
                                imageView_recommended_shift_5.setImageResource(R.drawable.badge_rec_5_144x144);
                                imageView_recommended_shift_10.setImageResource(R.drawable.badge_rec_10_144x144_grey);
                                imageView_recommended_shift_1.setClickable(true);
                                imageView_recommended_shift_5.setClickable(true);
                                imageView_recommended_shift_10.setClickable(false);
                            }
                            else if(statsResponse.getResponse().getBadges().getRecommended_shift_picked() >=10) {
                                imageView_recommended_shift_1.setImageResource(R.drawable.badge_picked_up_recommended_shifts_1_144x144);
                                imageView_recommended_shift_5.setImageResource(R.drawable.badge_rec_5_144x144);
                                imageView_recommended_shift_10.setImageResource(R.drawable.badge_rec_10_144x144);

                                imageView_recommended_shift_1.setClickable(true);
                                imageView_recommended_shift_5.setClickable(true);
                                imageView_recommended_shift_10.setClickable(true);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if( progressDialog!=null && progressDialog.isShowing())
                        CommonUtils.showSnackbar(tvRate, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StatsResponse> call, @NonNull Throwable t) {
                    if( progressDialog!=null && progressDialog.isShowing())
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvRate, t.getMessage());
                }
            });
        } catch (Exception e) {
            if( progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
            e.printStackTrace();
        }finally {
            if( progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }



    @OnClick({R.id.firstShiftLayout, R.id.ImageView_show_up_on_time_5,R.id.ImageView_show_up_on_time_10
            ,R.id.ImageView_show_up_on_time_20,R.id.ImageView_show_up_on_time_50,
            R.id.ImageView_show_up_on_time_100,R.id.ImageView_perfect_week,R.id.ImageView_perfect_month,
            R.id.ImageView_recommended_shift_1,R.id.ImageView_recommended_shift_5,R.id.ImageView_recommended_shift_10

    })
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), BadgeDetailsActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()){

            case R.id.firstShiftLayout:
                bundle.putString("badgeType","Completed");
                bundle.putString("badgeInfo","Just Getting Started");
                bundle.putString("badgeDetail","Congrats on completing your first shift on Zira.");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_5:
                bundle.putString("badgeType","OnTime_5");
                bundle.putString("badgeInfo","On Time Streak: 5x");
                bundle.putString("badgeDetail","You’ve checked in on time for 5 shifts in a row!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_10:
                bundle.putString("badgeType","OnTime_10");
                bundle.putString("badgeInfo","On Time Streak: 10x");
                bundle.putString("badgeDetail","You’ve checked in on time for 10 shifts in a row! Keep that streak going!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_20:
                bundle.putString("badgeType","OnTime_20");
                bundle.putString("badgeInfo","On Time Streak: 20x");
                bundle.putString("badgeDetail","You’ve checked in on time for 20 shifts in a row! Keep that streak going!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_50:
                bundle.putString("badgeType","OnTime_50");
                bundle.putString("badgeInfo","On Time Streak: 50x");
                bundle.putString("badgeDetail","You’ve checked in on time for 50 shifts in a row! This is no joke, you are on fire, how long could this possibly go!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_100:
                bundle.putString("badgeType","OnTime_100");
                bundle.putString("badgeInfo","Just Getting Started");
                bundle.putString("badgeDetail","You’ve checked in on time for 100 shifts in a row! This is no joke, you are on fire, how long could this possibly go!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;

            case R.id.ImageView_perfect_week:
                bundle.putString("badgeType","Perfect_w");
                bundle.putString("badgeInfo","You just completed a perfect week");
                bundle.putString("badgeDetail","They said it couldn’t be done! But you went and did it, with on time check ins for every shift this week.");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_perfect_month:
                bundle.putString("badgeType","Perfect_m");
                bundle.putString("badgeInfo","You just completed a perfect month");
                bundle.putString("badgeDetail","What kind of a machine shows up on time for every shift for an entire month! You that is who. Congrats on a perfect month. Truly one for the record books.");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_1:
                bundle.putString("badgeType","Recommended_1");
                bundle.putString("badgeInfo","You claimed your first shift");
                bundle.putString("badgeDetail","Congrats on claiming your first shift. Claim more to earn more badges and help your team running smooth as butter.");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_5:
                bundle.putString("badgeType","Recommended_5");
                bundle.putString("badgeInfo","You claimed 5 shifts");
                bundle.putString("badgeDetail","Ok shift master, we see you. Keep claiming shifts and your team will never have an uncovered shift.");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_10:
                bundle.putString("badgeType","Recommended_10");
                bundle.putString("badgeInfo","You claimed 10 shifts");
                bundle.putString("badgeDetail","You really like picking up more shifts. You have proven yourself worthy of your team’s gratitude and praise. Keep it up!");
                intent.putExtras(bundle);
                getContext().startActivity(intent);

                break;
        }
    }
}

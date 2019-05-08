package com.zing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.activity.BadgeDetailsActivity;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgesFragment extends BaseFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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



    @BindView(R.id.tvHeading)
    TextView tvBadges;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    SessionManagement session;
    //ArrayList<Badge> badgesList;
    //private GridBadgesAdapter myAdapter;

    public static BadgesFragment newInstance(String param1, String param2) {
        BadgesFragment fragment = new BadgesFragment();
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
        View view = inflater.inflate(R.layout.layout_badges, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvBadges.setTypeface(AppTypeface.avenieNext_demibold);
        session = new SessionManagement(getActivity());
        //badgesList = new ArrayList<>();

        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvBadges.setHasFixedSize(true);
        rvBadges.setLayoutManager(gridLayoutManager);
         myAdapter = new GridBadgesAdapter(getActivity(), badgesList);
        rvBadges.setAdapter(myAdapter);*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (NetworkUtils.isNetworkConnected(getActivity()))
        statsDetails();*/
    }

    /*private void statsDetails() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<JsonElement> call = api.statsDetailApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject responseObject = new JSONObject(response.body().toString());
                            JSONObject jsonObject = responseObject.optJSONObject("response");
                            String code = jsonObject.optString("code");
                            String message = jsonObject.optString("message");
                            //String rate = jsonObject.optString("rate");
                            JSONObject show_rate = jsonObject.optJSONObject("show_rate");
                            String label = show_rate.optString("label");
                            String description = show_rate.optString("description");
                            String rate = show_rate.optString("rate");

                            // CommonUtils.showSnackbar(tvBadges, message);
                            //badgesList.clear();

                            JSONObject badgesObj = jsonObject.optJSONObject("badges");
                            int first_shift_completed = badgesObj.optInt("first_shift_completed");
                            int show_up_on_time = badgesObj.optInt("show_up_on_time");
                            int perfect_week = badgesObj.optInt("perfect_week");
                            int perfect_month = badgesObj.optInt("perfect_month");
                            int recommended_shift_picked = badgesObj.optInt("recommended_shift_picked");


                            if(first_shift_completed==0 && show_up_on_time<5

                            && perfect_week==0 && perfect_month==0
                                    && recommended_shift_picked==0

                            ){
                                tvBadges.setText("No Badges Found");
                            }
                            if(first_shift_completed==0)
                                firstShiftLayout.setVisibility(View.GONE);
                            else
                                firstShiftLayout.setVisibility(View.VISIBLE);

                            if(show_up_on_time==0)
                                onTimeShiftLayout.setVisibility(View.GONE);
                            else
                                onTimeShiftLayout.setVisibility(View.VISIBLE);

                            if(recommended_shift_picked==0)
                                recommendShiftLayout.setVisibility(View.GONE);
                            else
                                recommendShiftLayout.setVisibility(View.VISIBLE);

                            if(perfect_week==0 && perfect_month ==0)
                                perfectShiftLayout.setVisibility(View.GONE);
                                else if(perfect_week==0 && perfect_month!=0){
                                    perfectShiftLayout.setVisibility(View.VISIBLE);
                                    imageView_perfect_week.setVisibility(View.GONE);
                                    imageView_perfect_month.setVisibility(View.VISIBLE);

                            }else if(perfect_month==0 && perfect_week!=0){
                                perfectShiftLayout.setVisibility(View.VISIBLE);
                                imageView_perfect_month.setVisibility(View.GONE);
                                imageView_perfect_week.setVisibility(View.VISIBLE);
                            }


                            imageView_show_up_on_time_5.setVisibility(View.GONE);
                            imageView_show_up_on_time_10.setVisibility(View.GONE);
                            imageView_show_up_on_time_20.setVisibility(View.GONE);
                            imageView_show_up_on_time_50.setVisibility(View.GONE);
                            imageView_show_up_on_time_100.setVisibility(View.GONE);

                            if(show_up_on_time<5){
                                onTimeShiftLayout.setVisibility(View.GONE);
                                imageView_show_up_on_time_5.setVisibility(View.GONE);
                                imageView_show_up_on_time_10.setVisibility(View.GONE);
                                imageView_show_up_on_time_20.setVisibility(View.GONE);
                                imageView_show_up_on_time_50.setVisibility(View.GONE);
                                imageView_show_up_on_time_100.setVisibility(View.GONE);
                            }

                            else if(show_up_on_time>=5 && show_up_on_time < 10)
                                imageView_show_up_on_time_5.setVisibility(View.VISIBLE);

                            else if(show_up_on_time >=10 && show_up_on_time <= 20) {
                                imageView_show_up_on_time_5.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_10.setVisibility(View.VISIBLE);
                            }
                            else if(show_up_on_time >21 && show_up_on_time <49) {
                                imageView_show_up_on_time_5.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_10.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_20.setVisibility(View.VISIBLE);
                            }
                            else  if(show_up_on_time >= 50 &&  show_up_on_time < 100) {
                                imageView_show_up_on_time_5.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_10.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_20.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_50.setVisibility(View.VISIBLE);
                            }
                            else  {
                                imageView_show_up_on_time_5.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_10.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_20.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_50.setVisibility(View.VISIBLE);
                                imageView_show_up_on_time_100.setVisibility(View.VISIBLE);
                            }


                            imageView_recommended_shift_1.setVisibility(View.GONE);
                            imageView_recommended_shift_5.setVisibility(View.GONE);
                            imageView_recommended_shift_10.setVisibility(View.GONE);



                            if(recommended_shift_picked > 0 && recommended_shift_picked < 5)
                                imageView_recommended_shift_1.setVisibility(View.VISIBLE);

                            else if(recommended_shift_picked >=5 && recommended_shift_picked <10) {
                                imageView_recommended_shift_1.setVisibility(View.VISIBLE);
                                imageView_recommended_shift_5.setVisibility(View.VISIBLE);
                            }
                            else if(recommended_shift_picked >=10) {
                                imageView_recommended_shift_1.setVisibility(View.VISIBLE);
                                imageView_recommended_shift_5.setVisibility(View.VISIBLE);
                                imageView_recommended_shift_10.setVisibility(View.VISIBLE);
                            }


                           *//*JSONArray badgeArr = jsonObject.optJSONArray("badges");
                            for (int i = 0; i < badgeArr.length(); i++) {
                                JSONObject badgesObj = badgeArr.optJSONObject(i);
                                Badge badge = new Badge();
                                badge.setBadgeId(badgesObj.optString("badge_id"));
                                badge.setBadgeImgUrl(badgesObj.optString("badge_img_url"));
                                badge.setBadgeStatus(badgesObj.optString("badge_status"));
                                badge.setDescription(badgesObj.optString("description"));
                                badge.setName(badgesObj.optString("name"));

                                badgesList.add(badge);
                            }*//*

                            //myAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvBadges, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvBadges, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }*/

    @OnClick({R.id.firstShiftLayout, R.id.ImageView_show_up_on_time_5,R.id.ImageView_show_up_on_time_10
              ,R.id.ImageView_show_up_on_time_20,R.id.ImageView_show_up_on_time_50,
                R.id.ImageView_show_up_on_time_100,R.id.ImageView_perfect_week,R.id.ImageView_perfect_month,
            R.id.ImageView_recommended_shift_1,R.id.ImageView_recommended_shift_5,R.id.ImageView_recommended_shift_10

    })
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(),BadgeDetailsActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()){

            case R.id.firstShiftLayout:
                bundle.putString("badgeType","Completed");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_5:
                bundle.putString("badgeType","OnTime_5");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_10:
                bundle.putString("badgeType","OnTime_10");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_20:
                bundle.putString("badgeType","OnTime_20");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_50:
                bundle.putString("badgeType","OnTime_50");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case  R.id.ImageView_show_up_on_time_100:
                bundle.putString("badgeType","OnTime_100");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;

            case R.id.ImageView_perfect_week:
                bundle.putString("badgeType","Perfect_w");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_perfect_month:
                bundle.putString("badgeType","Perfect_m");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_1:
                bundle.putString("badgeType","Recommended_1");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_5:
                bundle.putString("badgeType","Recommended_5");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.ImageView_recommended_shift_10:
                bundle.putString("badgeType","Recommended_10");
                intent.putExtras(bundle);
                getContext().startActivity(intent);

                break;
        }
    }
}

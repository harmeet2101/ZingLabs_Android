package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zing.R;
import com.zing.adapter.FooterAdapter;
import com.zing.base.BaseActivity;
import com.zing.fragment.BroadcastFragment;
import com.zing.fragment.CalenderFragment;
import com.zing.fragment.ClaimShiftFragment;
import com.zing.fragment.EarningFragment;
import com.zing.fragment.HomeFragment;
import com.zing.fragment.NotificationFragment;
import com.zing.fragment.PaymentFragment;
import com.zing.fragment.ProfileFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.FooterBean;
import com.zing.model.request.HomeRequest;
import com.zing.model.response.BroadcastResponse.BroadcastList;
import com.zing.model.response.BroadcastResponse.BroadcastResponse;
import com.zing.model.response.HomeResponse.HomeResponse;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by savita on 27/3/18.
 */

public class DashboardActivity extends BaseActivity implements FragmentInterface, FooterAdapter.ClickListner , HomeFragment.IHomFragListner {
    @BindView(R.id.rvFooter)
    RecyclerView rvFooter;
    FooterBean footerBean;
    ArrayList<FooterBean> footerList;
    private String from = "", shift_id = "";
    @BindView(R.id.container)
    FrameLayout container;
    SessionManagement session;
    private Fragment fragment = null;

    private ProgressDialog progressDialog;
    private ArrayList<BroadcastList> broadcastLists;

    boolean exit = false;
    private FooterAdapter adapter;
    public static int n = 0;
    private int icons[] = {R.drawable.ic_home, R.drawable.ic_cal, R.drawable.ic_payment_filled, R.drawable.ic_bell,
            R.drawable.ic_person};

    private int icons_selected[] = {R.drawable.ic_home_filled, R.drawable.ic_cal_filled, R.drawable.ic_payment,
            R.drawable.ic_bell_filled, R.drawable.ic_person_filled};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard);
        ButterKnife.bind(this);
        footerList = new ArrayList<>();
        session = new SessionManagement(this);
        broadcastLists = new ArrayList<>();
        broadcastLists.clear();
        from = getIntent().getStringExtra("from");

        for (int i = 0; i < icons.length; i++) {
            footerBean = new FooterBean(icons[i], icons_selected[i], false);
            footerList.add(footerBean);
        }

        rvFooter.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new FooterAdapter(this, footerList);
        rvFooter.setAdapter(adapter);
        adapter.setClickListner(this);



        if (from.equalsIgnoreCase("notification")) {
            shift_id = getIntent().getStringExtra("shift_id");
            String deepLink = getIntent().getStringExtra("deeplink");
            String dateStamp = getIntent().getStringExtra("dateStamp");
            boolean isShiftClaimed = getIntent().getBooleanExtra("isClaimed",false);
            switch (deepLink){

                case "broadcast":{
                    fragment = BroadcastFragment.newInstance(shift_id, "");
                    addFragment(fragment, "+");
                    footerList.get(3).setSelected(true);
                    pos=3;
                }
                    break;

                case "schedule":{

                    TimeZone tz = TimeZone.getDefault();
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if (NetworkUtils.isNetworkConnected(this))
                        getShiftsAndEarnings(date, tz.getDisplayName());
                    fragment = CalenderFragment.newInstance(dateStamp, "");
                    addFragment(fragment, "+");
                    footerList.get(1).setSelected(true);
                    pos=1;
                }break;

                case "account":{
                    fragment = ProfileFragment.newInstance("", "");
                    addFragment(fragment, "+");
                    footerList.get(4).setSelected(true);
                    pos=4;
                }
                break;

                case "shift_card": {
                    fragment = ClaimShiftFragment.newInstance(String.valueOf(isShiftClaimed),shift_id);
                    addFragment(fragment, "+");
                    footerList.get(1).setSelected(true);
                    pos = 1;
                }
                break;


                case "account_time_preference": {

                    Intent intent = new Intent(getApplicationContext(), PreferenceCalenderActivity.class);
                    intent.putExtra("from", "notification");
                    startActivity(intent);
                    finish();
                }
                break;

                default:{
                    fragment = HomeFragment.newInstance("", "");
                    addFragment(fragment, "+");
                    footerList.get(0).setSelected(true);
                    pos=0;
                    }


            }

        } else {
            fragment = HomeFragment.newInstance("", "");
            addFragment(fragment, "");
            footerList.get(0).setSelected(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void addFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.move_btt, R.anim.move_ttb);
        fragmentTransaction.replace(R.id.container, fragment, title);
        fragmentTransaction.commit();
    }

    private void addFragmentBackStack(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.move_btt, R.anim.move_ttb);
        fragmentTransaction.add(R.id.container, fragment, title);
        fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void fragmentResult(Fragment fragment, String title) {
        if (title.contains("+")) {
//            btmNavigation.setVisibility(View.GONE);
            rvFooter.setVisibility(View.GONE);
            addFragmentBackStack(fragment, title);
        } else {
            addFragment(fragment, title);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            rvFooter.setVisibility(View.VISIBLE);
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            rvFooter.setVisibility(View.VISIBLE);
            if (exit) {
                super.onBackPressed();
                return;
            }
            exit = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            rvFooter.setVisibility(View.GONE);
            super.onBackPressed();
        }

        if(pos==0){


        }if(pos==1){

        }
    }

    int pos;

    @Override
    public void itemClicked(View view, int position) {
        footerList.get(position).setSelected(true);
        if (position != pos)
            footerList.get(pos).setSelected(false);

        switch (position) {
            case 0:
//                menu.findItem(R.id.menu_home).setIcon(icons_selected[0]);
                fragment = HomeFragment.newInstance("", "");
                addFragment(fragment, "");

                break;
            case 1:
//                int n = container.getWidth();
//                menu.findItem(R.id.menu_calender).setIcon(icons_selected[1]);
                fragment = CalenderFragment.newInstance("", "");
                addFragment(fragment, "");
                break;
            case 2:
                fragment = PaymentFragment.newInstance("", "");
                addFragment(fragment, "");
//                getPreviousPaystub();
//                menu.findItem(R.id.menu_pay).setIcon(icons_selected[2]);
                break;
            case 3:
                getNotificationList();
//                menu.findItem(R.id.menu_bell).setIcon(icons_selected[3]);
                break;
            case 4:
//                menu.findItem(R.id.menu_account).setIcon(icons_selected[4]);
                fragment = ProfileFragment.newInstance("", "");
                addFragment(fragment, "");
                break;
        }

        adapter.notifyDataSetChanged();
        pos = position;
    }

    @Override
    public void itemLongClick(View view, int position) {

    }

//    private void getPreviousPaystub() {
//
//        progressDialog = CommonUtils.getProgressBar(this);
//        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
//        try {
//
//            Call<JsonElement> call = api.previousPaystubApi("Bearer " + session.getUserToken());
//            call.enqueue(new Callback<JsonElement>() {
//                @Override
//                public void onResponse(@NonNull Call<JsonElement> call,
//                                       @NonNull Response<JsonElement> response) {
//                    progressDialog.dismiss();
//                    if (response.code() == 200) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            JSONObject responseObj = jsonObject.optJSONObject("response");
//                            String code = responseObj.optString("code");
//                            String message = responseObj.optString("message");
//                            String upcoming_payment = responseObj.optString("upcoming_payment");
//                            String bank_detail = responseObj.optString("bank_detail");
//
//                            if (upcoming_payment.equalsIgnoreCase("0.00")) {
//                                fragment = PayFragment.newInstance("", "");
//                                addFragment(fragment, "");
//                            } else {
//
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        CommonUtils.showSnackbar(rvFooter, response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
//                    CommonUtils.showSnakBar(rvFooter, t.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            progressDialog.dismiss();
//            e.printStackTrace();
//        }
//    }

    private void getNotificationList() {

//        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<BroadcastResponse> call = api.broadcastApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<BroadcastResponse>() {
                @Override
                public void onResponse(@NonNull Call<BroadcastResponse> call,
                                       @NonNull Response<BroadcastResponse> response) {
//                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {

                            BroadcastResponse broadcastResponse = response.body();
                            if (broadcastResponse != null && broadcastResponse.getResponse().getCode().equalsIgnoreCase("200")) {
                                broadcastLists.clear();
                                for (int i = 0; i < broadcastResponse.getResponse().getBroadcastList().size(); i++) {
                                    BroadcastList broadcastList = new BroadcastList();
                                    broadcastList.setDescription(broadcastResponse.getResponse().getBroadcastList().get(i).getDescription());
                                    broadcastList.setImageUrl(broadcastResponse.getResponse().getBroadcastList().get(i).getImageUrl());
                                    broadcastList.setHeading(broadcastResponse.getResponse().getBroadcastList().get(i).getHeading());
                                    broadcastList.setTime(broadcastResponse.getResponse().getBroadcastList().get(i).getTime());
                                    broadcastList.setOtherLink(broadcastResponse.getResponse().getBroadcastList().get(i).getOtherLink());
                                    broadcastList.setType(broadcastResponse.getResponse().getBroadcastList().get(i).getType());
                                    broadcastLists.add(broadcastList);
                                }

                                if (broadcastLists.size() > 0) {
                                    fragment = BroadcastFragment.newInstance("", "");
                                    addFragment(fragment, "");
                                } else {
                                    fragment = NotificationFragment.newInstance("", "");
                                    addFragment(fragment, "");
                                }

//                                myAdapter.notifyDataSetChanged();
                            } else {
                                CommonUtils.showSnackbar(rvFooter, broadcastResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(rvFooter, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BroadcastResponse> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(rvFooter, t.getMessage());
                }
            });
        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
       /* } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    private void getShiftsAndEarnings(final String todate, String displayName) {
//        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {
            final HomeRequest homeRequest = new HomeRequest();
            homeRequest.setTodayDate(todate);
            homeRequest.setTimezone(displayName);
            Call<HomeResponse> call = api.dashboardApi("Bearer " + session.getUserToken(),
                    homeRequest);
            call.enqueue(new Callback<HomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<HomeResponse> call,
                                       @NonNull Response<HomeResponse> response) {
//                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {

                            HomeResponse homeResponse = response.body();

                            if (homeResponse != null && homeResponse.getResponse().getCode() == 200) {


                                shift_id = homeResponse.getResponse().getShiftId();
                                session.setNextShift(shift_id);






                            } else {
                                //CommonUtils.showSnackbar(tvViewCalendar, homeResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                       // CommonUtils.showSnackbar(tvViewCalendar, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
                   // CommonUtils.showSnakBar(tvViewCalendar, t.getMessage());
                }
            });
        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onHomeCallback(String type) {

        Log.d("Home","callback");

        switch (type){

            case "Pay":
                itemClicked(getCurrentFocus(),2);
/*                fragment = PaymentFragment.newInstance("", "");
                addFragment(fragment, "+");*/

                break;
            case "Cal":
                itemClicked(getCurrentFocus(),1);
                /*fragment = CalenderFragment.newInstance("", "");
                addFragment(fragment, "+");*/
                break;
        }
    }
}

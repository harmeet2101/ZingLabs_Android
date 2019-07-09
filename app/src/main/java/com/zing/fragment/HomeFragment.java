package com.zing.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.activity.PreferenceCalenderActivity;
import com.zing.activity.ShiftActivity;
import com.zing.adapter.UpcomingShiftAdapter;
import com.zing.model.CalendarDataModel;
import com.zing.model.request.HomeRequest;
import com.zing.model.request.UpcomingShiftRequest;
import com.zing.model.response.CalendarScheduledShiftResponse.CalendarScheduledShiftResponse;
import com.zing.model.response.HomeResponse.HomeResponse;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvNextShift)
    TextView tvNextShift;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvCashierName)
    TextView tvCashierName;
    @BindView(R.id.tvShiftAmount)
    TextView tvShiftAmount;
    @BindView(R.id.tvUpcomingShift)
    TextView tvUpcomingShift;
    @BindView(R.id.rvUpcomingShift)
    RecyclerView rvUpcomingShift;
    @BindView(R.id.tvViewCalendar)
    TextView tvViewCalendar;
    @BindView(R.id.tvMyPay)
    TextView tvMyPay;
    @BindView(R.id.tvDollar)
    TextView tvDollar;
    @BindView(R.id.tvPayoutAmount)
    TextView tvPayoutAmount;

    @BindView(R.id.tvPayoutAmount1)
    TextView tvPayoutAmount1;
    @BindView(R.id.tvViewEarning)
    TextView tvViewEarning;
    @BindView(R.id.tvPreferenceHeading)
    TextView tvPreferenceHeading;
    @BindView(R.id.tvPreferenceDescription)
    TextView tvPreferenceDescription;
    @BindView(R.id.tvSetPreference)
    TextView tvSetPreference;
    @BindView(R.id.tvPayCardHeading)
    TextView tvPayCardHeading;
    @BindView(R.id.tvPayCardDescription)
    TextView tvPayCardDescription;
    @BindView(R.id.tvInstantPay)
    TextView tvInstantPay;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.llShift)
    LinearLayout llShift;
    Unbinder unbinder;
    @BindView(R.id.cvUpcomingShiftList)
    CardView cvUpcomingShiftList;
    @BindView(R.id.cvUpcomingShift)
    CardView cvUpcomingShift;
    @BindView(R.id.cvMyPayData)
    CardView cvMyPayData;
    @BindView(R.id.cvMyPay)
    CardView cvMyPay;
    @BindView(R.id.cvNextShift)
    CardView cvNextShift;
    /*   @BindView(R.id.ivImage)
       ImageView ivImage;*/
    @BindView(R.id.llLayout)
    LinearLayout llLayout;
    @BindView(R.id.tvNxtShift)
    TextView tvNxtShift;
    @BindView(R.id.tvStartsIn)
    TextView tvStartsIn;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvToday)
    TextView tvToday;
    @BindView(R.id.tvShiftTime)
    TextView tvShiftTime;
    @BindView(R.id.tvShiftCashierName)
    TextView tvShiftCashierName;
    @BindView(R.id.tvShiftEarningAmount)
    TextView tvShiftEarningAmount;
    @BindView(R.id.tvViewShift)
    TextView tvViewShift;
    @BindView(R.id.cvShift)
    CardView cvShift;
    @BindView(R.id.llCurrentShift)
    LinearLayout llCurrentShift;
    private ProgressDialog progressDialog;
    SessionManagement session;

    private String mParam1;
    private String mParam2;
    private ArrayList<UpcomingShift> upcomingShiftsList;
    private UpcomingShiftAdapter shiftAdapter;
    private String shift_id = "", date = "", date1 = "", day, location, role, amount,
            starts_at = "", diff = "", check_in_status = "",checkInTime,checkOutTime,shiftStatus;
    private int breakStatus;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        upcomingShiftsList = new ArrayList<>();
        upcomingShiftsList.clear();
        session = new SessionManagement(getActivity());

        rvUpcomingShift.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        shiftAdapter = new UpcomingShiftAdapter(getActivity(), fragmentInterface, upcomingShiftsList);
        rvUpcomingShift.setAdapter(shiftAdapter);

        tvUserName.setTypeface(AppTypeface.avenieNext_demibold);
        tvNxtShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvStartTime.setTypeface(AppTypeface.avenieNext_demibold);
        tvNextShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvUpcomingShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvMyPay.setTypeface(AppTypeface.avenieNext_demibold);
        tvPayoutAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvPayoutAmount1.setTypeface(AppTypeface.avenieNext_regular);
        tvViewCalendar.setTypeface(AppTypeface.avenieNext_demibold);
        tvViewEarning.setTypeface(AppTypeface.avenieNext_demibold);
        tvSetPreference.setTypeface(AppTypeface.avenieNext_demibold);
        tvInstantPay.setTypeface(AppTypeface.avenieNext_demibold);
        tvViewShift.setTypeface(AppTypeface.avenieNext_demibold);

        tvPreferenceHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvPayCardHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvDay.setTypeface(AppTypeface.avenieNext_medium);

        tvCashierName.setTypeface(AppTypeface.avenieNext_regular);
        tvStartsIn.setTypeface(AppTypeface.avenieNext_regular);
        tvToday.setTypeface(AppTypeface.avenieNext_regular);
        tvShiftTime.setTypeface(AppTypeface.avenieNext_regular);
        tvShiftCashierName.setTypeface(AppTypeface.avenieNext_regular);
        tvShiftEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvShiftAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvWelcome.setTypeface(AppTypeface.avenieNext_regular);
        tvTime.setTypeface(AppTypeface.avenieNext_regular);
        tvPreferenceDescription.setTypeface(AppTypeface.avenieNext_regular);
        tvPayCardDescription.setTypeface(AppTypeface.avenieNext_regular);

        tvUserName.setText(session.getUserFirstName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);
        // to get the time zone
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.getID());
        // to fetch the current date and time
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (NetworkUtils.isNetworkConnected(getActivity())){
            getShiftsAndEarnings(date, tz.getID());

            getEarningWeek();
        }
    }


    private void getEarningWeek(){
        Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH)+1;
        startDate = year1+"-"+month1+"-"+day1;
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);
        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH) + 1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH)+1;
        endDate = year7+"-"+month7+"-"+day7;

        fetchCalenderDetails(startDate,endDate);
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
                            upcomingShiftsList.clear();
                            if (homeResponse != null && homeResponse.getResponse().getCode() == 200) {
                                upcomingShiftsList.addAll(homeResponse.getResponse().getUpcomingShifts());

                                shift_id = homeResponse.getResponse().getShiftId();
                                session.setNextShift(shift_id);
                                tvShiftAmount.setText(homeResponse.getResponse().getExpectedEarning());
                                tvShiftEarningAmount.setText("$" + homeResponse.getResponse().getExpectedEarning());

                                session.setManagerNumber(homeResponse.getResponse().getManager_number());
                                session.setRegistrationDate(homeResponse.getResponse().getRegistration_date());

                                tvTime.setText(homeResponse.getResponse().getNextShift());
                                tvShiftTime.setText(homeResponse.getResponse().getNextShift());

                                tvCashierName.setText(homeResponse.getResponse().getRole());
                                tvShiftCashierName.setText(homeResponse.getResponse().getRole() + ", " + homeResponse.getResponse().getLocation());

                                starts_at = homeResponse.getResponse().getStarts_at();
                                location = homeResponse.getResponse().getStoreName();
                                /*StringTokenizer tokens = new StringTokenizer(homeResponse.getResponse().getTotalEarnings(), ".");
                                String first = tokens.nextToken();
                                String second = tokens.nextToken();

                                tvPayoutAmount.setText(first);*/

                                checkInTime = homeResponse.getResponse().getCheckInTime();
                                checkOutTime = homeResponse.getResponse().getCheckOutTime();
                                shiftStatus = homeResponse.getResponse().getShiftStatus();

                                breakStatus = homeResponse.getResponse().getIsOnBreak();
                                amount = homeResponse.getResponse().getTotalEarnings();
                                date1 = homeResponse.getResponse().getDate();
                                role = homeResponse.getResponse().getRole();
                                if (date.equalsIgnoreCase(date1)) {
                                    tvDay.setText("Today");
                                    tvToday.setText("Today");

                                } else {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        Date date01 = format.parse(date1);
                                        String dayOfTheWeek = (String) DateFormat.format("EEEE", date01); // Thursday
                                        day = (String) DateFormat.format("EEE", date01); // Thursday

                                        tvDay.setText(dayOfTheWeek);
                                        tvToday.setText(dayOfTheWeek);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                    shiftAdapter.updateDataSource(upcomingShiftsList);

                                if (upcomingShiftsList.size() == 0) {
                                    cvUpcomingShift.setVisibility(View.VISIBLE);
                                    cvUpcomingShiftList.setVisibility(View.GONE);
                                } else {
                                    cvUpcomingShift.setVisibility(View.GONE);
                                    cvUpcomingShiftList.setVisibility(View.VISIBLE);
                                }

                                if (homeResponse.getResponse().getTime_preferences().equalsIgnoreCase("1")) {
                                    tvSetPreference.setText("Modify time preferences");
                                } else {
                                    tvSetPreference.setText("Set up your preferences");
                                }

                                if (homeResponse.getResponse().getTotalEarnings().equalsIgnoreCase("0.0")) {
                                    cvMyPay.setVisibility(View.VISIBLE);
                                    cvMyPayData.setVisibility(View.GONE);
                                } else {
                                    cvMyPay.setVisibility(View.GONE);
                                    cvMyPayData.setVisibility(View.VISIBLE);
                                }

                                if (homeResponse.getResponse().getNextShift().equalsIgnoreCase("")) {
                                    cvNextShift.setVisibility(View.GONE);
//                                    ivImage.setVisibility(View.GONE);
                                } else {
                                    cvNextShift.setVisibility(View.VISIBLE);
//                                    ivImage.setVisibility(View.VISIBLE);
                                }

                                llLayout.setVisibility(View.VISIBLE);

                                check_in_status = homeResponse.getResponse().getCheck_in_status();
                                if (check_in_status.equalsIgnoreCase("1")) {
                                    tvNextShift.setText("Current Shift");
                                } else {
                                    tvNextShift.setText("Next Shift");
                                }
//                                updateTime();

                                if (check_in_status.equalsIgnoreCase("0")) {
                                    llCurrentShift.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
                                } else {
                                    llCurrentShift.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_green));
                                    tvStartsIn.setText("Ends in: ");
                                    tvNxtShift.setText("Ongoing Shift");
                                }

                            } else {
                                CommonUtils.showSnackbar(tvViewCalendar, homeResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvViewCalendar, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvViewCalendar, t.getMessage());
                }
            });
        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private Fragment fragment;

    @OnClick({R.id.llShift, R.id.tvViewEarning, R.id.tvSetPreference, R.id.tvInstantPay,
            R.id.tvViewShift, R.id.tvViewCalendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvViewCalendar:
               /* fragment = CalenderFragment.newInstance("", "");
                fragmentInterface.fragmentResult(fragment, "+");*/
                iHomFragListner.onHomeCallback("Cal");
                break;
            case R.id.llShift:
                if (check_in_status.equalsIgnoreCase("0")) {
                    fragment = ShiftDialogFragment.newInstance("home", shift_id,
                            date1, day, tvShiftAmount.getText().toString(), tvTime.getText().toString(),
                            location, role, "0",tvNextShift.getText().toString(), new ArrayList< CalendarDataModel>());
                    fragmentInterface.fragmentResult(fragment, "+");
                } else {
                    fragment = RateShiftFragment.newInstance(shift_id, location,checkInTime,
                            checkOutTime,shiftStatus,String.valueOf(breakStatus),"home");
                    fragmentInterface.fragmentResult(fragment, "+");
                }
                break;
            case R.id.tvViewEarning:
                if (amount.equalsIgnoreCase("0.0")) {
                    CommonUtils.showSnackbar(tvViewEarning, "No Earnings Yet.");
                } else {
                   /* Fragment fragment1 = PaymentFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment1, "+");*/
                    iHomFragListner.onHomeCallback("Pay");
                }

                break;
            case R.id.tvSetPreference:
                Intent intent = new Intent(getActivity(), PreferenceCalenderActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
                break;
            case R.id.tvInstantPay:
                Fragment fragment1 = SetPaymentDetailsFragment.newInstance("", "", "", "", "home");
                fragmentInterface.fragmentResult(fragment1, "+");
                break;
            case R.id.tvViewShift:
                if (check_in_status.equalsIgnoreCase("0")) {
                    Fragment fragment2 = NextShiftDetailFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment2, "+");
                } else {
                    Fragment fragment2 = ShiftDetailFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment2, "+");
                }
                break;
        }
    }


    public interface IHomFragListner{
        void  onHomeCallback(String type);
    }


    private double expAmount=0.0,prjAmount=0.0,upComingAmount=0.0;
    private String startDate="",endDate="";
    private void fetchCalenderDetails(String start_date, String end_date) {

        prjAmount = 0.00;
        expAmount = 0.00;
        upComingAmount = 0.0;

        //progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {
            UpcomingShiftRequest upcomingShiftRequest = new UpcomingShiftRequest();
            upcomingShiftRequest.setStartDate(start_date);
            upcomingShiftRequest.setEndDate(end_date);

            Call<CalendarScheduledShiftResponse> call = api.calendarDetailApi("Bearer " +
                    session.getUserToken(), upcomingShiftRequest);
            call.enqueue(new Callback<CalendarScheduledShiftResponse>() {
                @Override
                public void onResponse(@NonNull Call<CalendarScheduledShiftResponse> call,
                                       @NonNull Response<CalendarScheduledShiftResponse> response) {
                   // progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            CalendarScheduledShiftResponse calendarSheduledShiftResponse = response.body();
                            if (calendarSheduledShiftResponse != null && calendarSheduledShiftResponse.
                                    getResponse().getCode() == 200) {
                                DecimalFormat df2 = new DecimalFormat("#0.00");
                                df2.setRoundingMode(RoundingMode.DOWN);


                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getScheduledShifts().size(); i++) {

                                    if(calendarSheduledShiftResponse.getResponse().getScheduledShifts().get(i).getShiftStatus().equals("UPCOMING")||

                                    calendarSheduledShiftResponse.getResponse().getScheduledShifts().get(i).getShiftStatus().equals("ONGOING")
                                    ) {
                                        StringTokenizer tokenizer = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                                getScheduledShifts().get(i).getExpectedEarning(), "$");
                                        upComingAmount = upComingAmount + Double.parseDouble(tokenizer.nextToken());

                                    }

                                }

                                //llLayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getCompletedShiftList().size(); i++) {

                                    StringTokenizer tokenizer = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i).getExpectedEarning(),"$");
                                    StringTokenizer tokenizer_1 = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i).getEarningAmount(),"$");
                                    prjAmount = prjAmount + Double.parseDouble(tokenizer.nextToken());

                                    expAmount = expAmount + Double.parseDouble(tokenizer_1.nextToken());
                                }

                                Log.d("EA",expAmount+"");
                                Log.d("PA",prjAmount+"");

                                tvPayoutAmount.setText("$"+df2.format(expAmount));
                                tvPayoutAmount1.setText("$"+df2.format(prjAmount + upComingAmount));


                            }
                            else {
                                Toast.makeText(getContext(),calendarSheduledShiftResponse.getResponse().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            //progressDialog.dismiss();
                            expAmount = 0.0;
                            prjAmount = 0.0;
                            upComingAmount = 0.0;
                        }
                    } else {
                        Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CalendarScheduledShiftResponse> call, @NonNull Throwable t) {
                    //progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }



    private String getMonth(int month1) {
        String startMonth = "";
        switch (month1) {
            case 1:
                startMonth = "Jan";
                break;
            case 2:
                startMonth = "Feb";
                break;
            case 3:
                startMonth = "Mar";
                break;
            case 4:
                startMonth = "Apr";
                break;
            case 5:
                startMonth = "May";
                break;
            case 6:
                startMonth = "Jun";
                break;
            case 7:
                startMonth = "Jul";
                break;
            case 8:
                startMonth = "Aug";
                break;
            case 9:
                startMonth = "Sep";
                break;
            case 10:
                startMonth = "Oct";
                break;
            case 11:
                startMonth = "Nov";
                break;
            case 12:
                startMonth = "Dec";
                break;
        }
        return startMonth;
    }
}

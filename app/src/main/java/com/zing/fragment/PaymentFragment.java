package com.zing.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.adapter.PayCompletedShiftAdapter;
import com.zing.model.request.UpcomingShiftRequest;
import com.zing.model.response.CalendarScheduledShiftResponse.CalendarScheduledShiftResponse;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.SortByTimeComparator;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.tlGraph)
    TabLayout tlGraph;

    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String startWeek = "", endWeek = "";
    private String startDate="",endDate="";

    @BindView(R.id.tvCompletedShift)
    TextView tvCompletedShift;
    @BindView(R.id.rvCompletedShift)
    RecyclerView rvCompletedShift;
    @BindView(R.id.cvCompletedShiftList)
    CardView cvCompletedShiftList;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.tvProjected)
    TextView tvProjected;

    @BindView(R.id.earningAmount)
    TextView earningAmount;
    @BindView(R.id.projectedAmount)
    TextView projectedAmount;


    private PayCompletedShiftAdapter completedAdapter;
    private ProgressDialog progressDialog;
    SessionManagement session;
    ArrayList<RecommendedShift> completedShiftList = new ArrayList<>();

    private double expAmount=0.0,prjAmount=0.0,upComingAmount=0.0;
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
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
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);

        Calendar c1 = Calendar.getInstance();

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH)+1;
        startWeek = getMonth(month1);
        startDate = year1+"-"+month1+"-"+day1;
        //Log.d("start",year1+"-"+month1+"-"+day1);
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH) + 1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH)+1;
        endWeek = getMonth(month7);
        endDate = year7+"-"+month7+"-"+day7;

        if (NetworkUtils.isNetworkConnected(getActivity())){
            fetchCalenderDetails(startDate,endDate);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        session = new SessionManagement(getActivity());
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create tabs title
        tlGraph.addTab(tlGraph.newTab().setText("Week"));
        tlGraph.addTab(tlGraph.newTab().setText("Month"));

        //addFragment(new WeekFragment(), "Week");

        //handling tab click event
        tlGraph.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        tvEarning.setText("Earning this week");
                        tvProjected.setText("Projected this week");
                        fetchCalenderDetails(startDate,endDate);
                        break;
                    case 1:
                        tvEarning.setText("Earning this month");
                        tvProjected.setText("Projected this month");
                        Calendar c = Calendar.getInstance();
                        String start = c.get(Calendar.YEAR)+"-" +
                                (c.get(Calendar.MONTH)+1)+
                                "-" +
                                c.getActualMinimum(Calendar.DAY_OF_MONTH);

                        String end = c.get(Calendar.YEAR)+"-" +
                                (c.get(Calendar.MONTH)+1)+
                                "-" +
                                c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        fetchCalenderDetails(start,end);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        rvCompletedShift.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        completedAdapter = new PayCompletedShiftAdapter(getActivity(), fragmentInterface, completedShiftList);
        rvCompletedShift.setAdapter(completedAdapter);

    }



    private void fetchCalenderDetails(String start_date, String end_date) {

        prjAmount = 0.00;
        expAmount = 0.00;
        upComingAmount = 0.0;
        progressDialog = CommonUtils.getProgressBar(getActivity());
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
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            CalendarScheduledShiftResponse calendarSheduledShiftResponse = response.body();
                            if (calendarSheduledShiftResponse != null && calendarSheduledShiftResponse.
                                    getResponse().getCode() == 200) {
                                DecimalFormat df2 = new DecimalFormat("#0.00");
                                df2.setRoundingMode(RoundingMode.DOWN);
                                completedShiftList.clear();
                                //llLayout.setVisibility(View.VISIBLE);


                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getScheduledShifts().size(); i++) {

                                    if(calendarSheduledShiftResponse.getResponse().getScheduledShifts().get(i).getShiftStatus().equals("NOSHOW")) {
                                        completedShiftList.add(

                                                new RecommendedShift(
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getShiftId(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getDay(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getDate(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getTimeSlot(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getExpectedEarning(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getRole(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getLocation(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getRelease(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getStoreName(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getEarningAmount(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getTotalBreakTime(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getLastBreakInDate(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getLastBreakInTime(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getCheckInTime(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getCheckOutTime(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).isOnBreak(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getShiftStatus(),
                                                        calendarSheduledShiftResponse.getResponse().
                                                                getScheduledShifts().get(i).getShift_start_timestamp()

                                                )

                                        );
                                    }
                                    if(calendarSheduledShiftResponse.getResponse().getScheduledShifts().get(i).getShiftStatus().equals("UPCOMING")||
                                            calendarSheduledShiftResponse.getResponse().getScheduledShifts().get(i).getShiftStatus().equals("ONGOING")
                                    ) {
                                        StringTokenizer tokenizer = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                                getScheduledShifts().get(i).getExpectedEarning(), "$");
                                        upComingAmount = upComingAmount + Double.parseDouble(tokenizer.nextToken());
                                    }

                                }

                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getCompletedShiftList().size(); i++) {
                                    completedShiftList.add(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i));


                                    StringTokenizer tokenizer = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i).getExpectedEarning(),"$");
                                    StringTokenizer tokenizer_1 = new StringTokenizer(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i).getEarningAmount(),"$");
                                    prjAmount = prjAmount + Double.parseDouble(tokenizer.nextToken());

                                    expAmount = expAmount + Double.parseDouble(tokenizer_1.nextToken());
                                }

                                Log.d("EA",expAmount+"");
                                Log.d("PA",prjAmount+"");

                                earningAmount.setText("$ "+df2.format(expAmount));
                                projectedAmount.setText("$ "+df2.format(prjAmount + upComingAmount));
                                if (completedShiftList.size() != 0) {
                                    tvCompletedShift.setText(getActivity().getResources().getText(R.string.completed_shift));
                                    cvCompletedShiftList.setVisibility(View.VISIBLE);
                                } else {
                                    cvCompletedShiftList.setVisibility(View.VISIBLE);
                                    tvCompletedShift.setText("No Completed Shifts Available");
                                }


                            }
                            else {
                                Toast.makeText(getContext(),calendarSheduledShiftResponse.getResponse().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {

                            Collections.sort(completedShiftList,new SortByTimeComparator());
                            completedAdapter.notifyDataSetChanged();
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
                    progressDialog.dismiss();
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

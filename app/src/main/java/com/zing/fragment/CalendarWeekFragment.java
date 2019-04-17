package com.zing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.activity.PreferenceCalenderActivity;
import com.zing.adapter.CompletedShiftAdapter;
import com.zing.adapter.RecomendedShiftAdapter;
import com.zing.adapter.ShiftAdapter;
import com.zing.adapter.WeekCalendarAdapter;
import com.zing.model.request.CalendarRequest;
import com.zing.model.request.UpcomingShiftRequest;
import com.zing.model.response.CalendarScheduledShiftResponse.CalendarScheduledShiftResponse;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.model.response.CalendarSlotResponse.Slot;
import com.zing.model.response.HomeResponse.UpcomingShift;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CalendarWeekFragment extends BaseFragment implements WeekCalendarAdapter.ClickListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.cvShiftList)
    CardView cvShiftList;
    Unbinder unbinder;
    @BindView(R.id.tvScheduleShift)
    TextView tvScheduleShift;
    @BindView(R.id.rvScheduleShift)
    RecyclerView rvScheduleShift;
    @BindView(R.id.cvRecommendedShiftList)
    CardView cvRecommendedShiftList;
    @BindView(R.id.tvCompletedShift)
    TextView tvCompletedShift;
    @BindView(R.id.rvCompletedShift)
    RecyclerView rvCompletedShift;
    @BindView(R.id.cvCompletedShiftList)
    CardView cvCompletedShiftList;

    private String mParam1;
    private String mParam2;

    @BindView(R.id.rvCalendar)
    RecyclerView rvCalendar;
    @BindView(R.id.tvScheduledShift)
    TextView tvScheduledShift;
    @BindView(R.id.tvScheduledShiftHeading)
    TextView tvScheduledShiftHeading;
    @BindView(R.id.tvScheduledShiftDetail)
    TextView tvScheduledShiftDetail;
    @BindView(R.id.btnSetPreference)
    Button btnSetPreference;
    @BindView(R.id.rvScheduledShift)
    RecyclerView rvScheduledShift;
    @BindView(R.id.tvRecommendedShift)
    TextView tvRecommendedShift;
    @BindView(R.id.rvRecommendedShift)
    RecyclerView rvRecommendedShift;
    @BindView(R.id.llLayout)
    LinearLayout llLayout;

    @BindView(R.id.cvShift)
    CardView cvShift;
    ArrayList<UpcomingShift> scheduledShiftList;

    private ProgressDialog progressDialog;
    private WeekCalendarAdapter myAdapter;
    SessionManagement session;
    ArrayList<Slot> slotList;
    ArrayList<RecommendedShift> recommendedShiftList, completedShiftList;
    private ShiftAdapter adapter;
    private RecomendedShiftAdapter recomendedAdapter;
    private CompletedShiftAdapter completedAdapter;
    int pos = 0;

    public static CalendarWeekFragment newInstance(String param1, String param2) {
        CalendarWeekFragment fragment = new CalendarWeekFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());
        scheduledShiftList = new ArrayList<>();

        slotList = new ArrayList<>();
        recommendedShiftList = new ArrayList<>();
        completedShiftList = new ArrayList<>();
        slotList.clear();
        recommendedShiftList.clear();
        completedShiftList.clear();

        tvScheduledShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvRecommendedShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvCompletedShift.setTypeface(AppTypeface.avenieNext_demibold);
        tvScheduleShift.setTypeface(AppTypeface.avenieNext_demibold);
        btnSetPreference.setTypeface(AppTypeface.avenieNext_demibold);
        tvScheduledShiftHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvScheduledShiftDetail.setTypeface(AppTypeface.avenieNext_regular);

        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        rvCalendar.setLayoutManager(lManager);
        myAdapter = new WeekCalendarAdapter(getActivity(), slotList);
        rvCalendar.setAdapter(myAdapter);
        myAdapter.setClickListner(this);

        rvScheduleShift.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        adapter = new ShiftAdapter(getActivity(), fragmentInterface, scheduledShiftList);
        rvScheduleShift.setAdapter(adapter);

        rvRecommendedShift.setLayoutManager(new LinearLayoutManager(getActivity()));
        recomendedAdapter = new RecomendedShiftAdapter(getActivity(), fragmentInterface, recommendedShiftList);
        rvRecommendedShift.setAdapter(recomendedAdapter);

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
        completedAdapter = new CompletedShiftAdapter(getActivity(), fragmentInterface, completedShiftList);
        rvCompletedShift.setAdapter(completedAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnSetPreference})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSetPreference:
                Intent intent = new Intent(getActivity(), PreferenceCalenderActivity.class);
                intent.putExtra("from", "calendar");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);
        if (NetworkUtils.isNetworkConnected(getActivity()))
            calendarSlotDetails();
    }

    private void calendarSlotDetails() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setSlot("weekly");

            Call<JsonElement> call = api.calendarSlotDetailsApi("Bearer " +
                    session.getUserToken(), calendarRequest);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject responseOb = new JSONObject(response.body().toString());
                            JSONObject responseObj = responseOb.optJSONObject("response");
                            String code = responseObj.optString("code");
                            String message = responseObj.optString("message");
                            if (code.equalsIgnoreCase("200")) {
                                JSONArray slotArr = responseObj.optJSONArray("slot");
                                slotList.clear();
                                for (int i = 0; i < slotArr.length(); i++) {
                                    JSONObject slotObj = slotArr.optJSONObject(i);
                                    String start_date = slotObj.optString("start_date");
                                    String end_date = slotObj.optString("end_date");

                                    Slot slot = new Slot();
                                    slot.setStartDate(start_date);
                                    slot.setEndDate(end_date);

                                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date newDate = spf.parse(start_date);
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                                    start_date = sdf.format(newDate);

                                    Date newEndDate = spf.parse(end_date);
                                    SimpleDateFormat df = new SimpleDateFormat("dd");
                                    end_date = df.format(newEndDate);

                                    slot.setSlot(start_date + "-" + end_date);



                                    if(mParam1!=null && !mParam1.isEmpty()){

                                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date testDate = sd.parse(mParam1);
                                            if(CommonUtils.isWithinDateRange(testDate,newDate,newEndDate)) {
                                               // Log.d("check date:", "true");
                                                slot.setSelected(true);
                                                pos = i;
                                            }else
                                                slot.setSelected(false);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }else if (i == pos){
                                        slot.setSelected(true);

                                    }
                                    else
                                        slot.setSelected(false);

                                    slotList.add(slot);
                                }
                                myAdapter.notifyDataSetChanged();

                                fetchCalenderDetails(slotList.get(pos).getStartDate(),
                                        slotList.get(pos).getEndDate());


                            } else {
                                CommonUtils.showSnackbar(rvCalendar, message);
                            }
                        } catch (Exception e) {
                            CommonUtils.showSnackbar(rvCalendar, e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(rvCalendar, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(rvCalendar, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void fetchCalenderDetails(String start_date, String end_date) {

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

                                scheduledShiftList.clear();
                                recommendedShiftList.clear();
                                completedShiftList.clear();
                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getScheduledShifts().size(); i++) {
                                    scheduledShiftList.add(calendarSheduledShiftResponse.getResponse().
                                            getScheduledShifts().get(i));
                                }

                                llLayout.setVisibility(View.VISIBLE);

                                if (scheduledShiftList.size() == 0) {
                                    cvShiftList.setVisibility(View.GONE);
                                    cvShift.setVisibility(View.VISIBLE);
                                } else {
                                    cvShiftList.setVisibility(View.VISIBLE);
                                    cvShift.setVisibility(View.GONE);

                                }

                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getRecommendedShiftList().size(); i++) {
                                    recommendedShiftList.add(calendarSheduledShiftResponse.getResponse().
                                            getRecommendedShiftList().get(i));
                                }

                                if (recommendedShiftList.size() != 0) {
                                    cvRecommendedShiftList.setVisibility(View.VISIBLE);
                                } else {
                                    cvRecommendedShiftList.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getCompletedShiftList().size(); i++) {
                                    completedShiftList.add(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i));
                                }

                                if (completedShiftList.size() != 0) {
                                    cvCompletedShiftList.setVisibility(View.VISIBLE);
                                } else {
                                    cvCompletedShiftList.setVisibility(View.GONE);
                                }


                            }
                            else {
                                CommonUtils.showSnackbar(rvCalendar, calendarSheduledShiftResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            adapter.notifyDataSetChanged();
                            recomendedAdapter.notifyDataSetChanged();
                            completedAdapter.notifyDataSetChanged();
                        }
                    } else {
                        CommonUtils.showSnackbar(rvCalendar, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CalendarScheduledShiftResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(rvCalendar, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View view, int position) {

        if (position != pos) {
            slotList.get(position).setSelected(true);
            slotList.get(pos).setSelected(false);

            myAdapter.notifyDataSetChanged();
            pos = position;
        } else {
            slotList.get(position).setSelected(true);
            myAdapter.notifyDataSetChanged();
        }

        fetchCalenderDetails(slotList.get(position).getStartDate(),
                slotList.get(position).getEndDate());
    }
}

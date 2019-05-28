package com.zing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zing.R;
import com.zing.adapter.TimeAdapter;
import com.zing.adapter.WeekDaysAdapter;
import com.zing.base.BaseActivity;
import com.zing.dragSelection.DragSelectTouchListener;
import com.zing.dragSelection.DragSelectionProcessor;
import com.zing.model.TimeBean;
import com.zing.model.request.TimePreferenceRequest.Available;
import com.zing.model.request.TimePreferenceRequest.Preffered;
import com.zing.model.response.GetBusinessHourResponse.GetBusinessHours;
import com.zing.model.response.GetTimePreferenceResponse.GetTimePreferenceResponse;
import com.zing.model.response.WeekPreference.Datum;
import com.zing.model.response.WeekPreference.WeekPreference;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PreferenceCalenderActivity extends BaseActivity implements WeekDaysAdapter.ClickListner {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvPreferences)
    TextView tvPreferences;
    @BindView(R.id.rvWeekDays)
    RecyclerView rvWeekDays;
    @BindView(R.id.rvTime)
    RecyclerView rvTime;

    //  DaysBean daysBean;
    ArrayList<TimeBean> daysList;
    String days[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    TimeBean timeBean;
    ArrayList<TimeBean> timeList;
    String time[] = {"12 am", "01 am","02 am","03 am", "04 am","05 am","06 am","07 am", "08 am","09 am","10 am",
            "11 am","12 pm", "01 pm","02 pm","03 pm", "04 pm","05 pm", "06 pm","07 pm", "08 pm","09 pm", "10 pm",
            "11 pm","12 am"};

    @BindView(R.id.tvSkipStep)
    TextView tvSkipStep;

    @BindView(R.id.btnDone)
    Button btnDone;
    @BindView(R.id.tvAvailable)
    TextView tvAvailable;
    @BindView(R.id.tvPreffered)
    TextView tvPreffered;

    private ProgressDialog progressDialog;
    SessionManagement session;
    private WeekDaysAdapter weekDaysAdapter;
    private TimeAdapter adapter;
    private String from;
    public static ArrayList<Preffered> prefferedList;
    public static ArrayList<Available> availableList;

    private DragSelectionProcessor.Mode mMode = DragSelectionProcessor.Mode.Simple;
    private DragSelectTouchListener mDragSelectTouchListener;
    private DragSelectionProcessor mDragSelectionProcessor;
    private ArrayList<Datum> dayList;
    private LinkedHashMap<String, ArrayList<TimeBean>> dayBusinessHourMatch;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_calender);
        ButterKnife.bind(this);
        prefferedList = new ArrayList<>();
        availableList = new ArrayList<>();
        dayBusinessHourMatch =new LinkedHashMap<>();
        dayList = new ArrayList<>();

        AppTypeface.getTypeFace(this);
        tvTime.setTypeface(AppTypeface.avenieNext_demibold);
        tvPreferences.setTypeface(AppTypeface.avenieNext_demibold);
        tvAvailable.setTypeface(AppTypeface.avenieNext_demibold);
        tvPreffered.setTypeface(AppTypeface.avenieNext_demibold);
        btnDone.setTypeface(AppTypeface.avenieNext_demibold);
        tvSkipStep.setTypeface(AppTypeface.avenieNext_demibold);

        session = new SessionManagement(this);

        from = getIntent().getStringExtra("from");
        daysList = new ArrayList<>();
        daysList.clear();

        for (int i = 0; i < days.length; i++) {
            if (i == 0) {
                timeBean = new TimeBean(days[i], true, false);
            } else {
                timeBean = new TimeBean(days[i], false, false);
            }

            daysList.add(timeBean);
        }

        timeList = new ArrayList<>();
        timeList.clear();

        for (int i = 0; i < time.length; i++) {
            timeBean = new TimeBean(time[i].toUpperCase(), false, false);
            timeList.add(timeBean);
        }

        rvWeekDays.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.single_line_grey));

        DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        dividerItemDecorationHorizontal.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.single_line_grey));

        rvWeekDays.addItemDecoration(dividerItemDecoration);
        rvWeekDays.addItemDecoration(dividerItemDecorationHorizontal);

        weekDaysAdapter = new WeekDaysAdapter(this, daysList);
        rvWeekDays.setAdapter(weekDaysAdapter);
        weekDaysAdapter.setClickListner(this);

        rvTime.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        if (NetworkUtils.isNetworkConnected(this)) {
           getBusinessHours();
        }
    }


    private void updateSelectionListener() {
        mDragSelectionProcessor.withMode(mMode);
    }

    @OnClick({R.id.tvSkipStep})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSkipStep:
                Intent intent = new Intent(PreferenceCalenderActivity.this, DashboardActivity.class);
                intent.putExtra("from", from);
                if (from.equalsIgnoreCase("notification")){
                    intent.putExtra("deeplink", "account");
                }
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(this))
            getPreference();
    }

    private ArrayList<String> getTimeRange(double startTime, double endTime)
    {
        Calendar startCalendarInstance= Calendar.getInstance();
        startCalendarInstance.set(Calendar.HOUR_OF_DAY, (int) startTime);
        startCalendarInstance.set(Calendar.MINUTE, 0);
        startCalendarInstance.set(Calendar.SECOND, 0);

        Calendar endCalendarInstance= Calendar.getInstance();
        endCalendarInstance.set(Calendar.HOUR_OF_DAY, (int) endTime);
        endCalendarInstance.set(Calendar.MINUTE, 0);
        endCalendarInstance.set(Calendar.SECOND, 0);

        ArrayList<String> timeList=new ArrayList<>();
        while(startCalendarInstance.getTimeInMillis() < (endCalendarInstance.getTimeInMillis() + 10))
        {
            int presentTime = startCalendarInstance.getTime().getHours();
            timeList.add(""+getTime(startCalendarInstance));
            startCalendarInstance.set(Calendar.HOUR_OF_DAY, presentTime + 1);
        }
        return timeList;
    }

    private String getTime(Calendar calendar)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hha");
        return sdf.format(calendar.getTime()).toLowerCase();
    }


    private void getBusinessHours() {
//        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        Call<GetBusinessHours> call = api.getBusinessHour("Bearer " + session.getUserToken());
        call.enqueue(new Callback<GetBusinessHours>() {
            @Override
            public void onResponse(@NonNull Call<GetBusinessHours> call,
                                   @NonNull Response<GetBusinessHours> response) {
                if (response.code() == 200) {
                    try {
                        GetBusinessHours weekPreference = response.body();
                        if (weekPreference != null && weekPreference.getCode() == 200) {
                            for (int i = 0; i < weekPreference.getData().size(); i++) {
                                ArrayList<String> dataList= getTimeRange(Math.floor(weekPreference.getData().get(i).getRange().get(0)),
                                        Math.ceil(weekPreference.getData().get(i).getRange().get(1)));
                                ArrayList<TimeBean> timeBeanArrayList = new ArrayList<>();
                                for (int j = 0; j < dataList.size(); j++) {

                                    TimeBean tm = new TimeBean(dataList.get(j),false,false);
                                    timeBeanArrayList.add(tm);
                                    Log.e("********",""+dataList.get(j));
                                    //Toast.makeText(getApplicationContext(),dataList.get(j),Toast.LENGTH_SHORT).show();
                                }

                                dayBusinessHourMatch.put(weekPreference.getData().get(i).getDay(), timeBeanArrayList);
                            }


                        }
                        else {
                            CommonUtils.showSnackbar(btnDone, weekPreference.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBusinessHours> call, Throwable t) {
                CommonUtils.showSnakBar(btnDone, t.getMessage());
            }
        });
    }

    private void getPreference() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<WeekPreference> call = api.getWeekPreferenceApi("Bearer " +
                    session.getUserToken());
            call.enqueue(new Callback<WeekPreference>() {
                @Override
                public void onResponse(@NonNull Call<WeekPreference> call,
                                       @NonNull Response<WeekPreference> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            WeekPreference weekPreference = response.body();
                            if (weekPreference != null && weekPreference.getResponse().getCode() == 200) {
                                dayList.clear();
                                dayList = weekPreference.getResponse().getData();

                                daysList.clear();
                                for (int i = 0; i < dayList.size(); i++) {
                                    if (i == 0) {
                                        if (dayList.get(i).getPreference().equalsIgnoreCase("1")) {
                                            timeBean = new TimeBean(days[i], true, true);
                                        } else
                                            timeBean = new TimeBean(days[i], true, false);
                                    } else {
                                        if (dayList.get(i).getPreference().equalsIgnoreCase("1")) {
                                            timeBean = new TimeBean(days[i], false, true);
                                        } else
                                            timeBean = new TimeBean(days[i], false, false);
                                    }
                                    daysList.add(timeBean);
                                }
                                weekDaysAdapter.notifyDataSetChanged();

                                getTimePreference();
                            } else {
                                CommonUtils.showSnackbar(btnDone, weekPreference.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        CommonUtils.showSnackbar(btnDone, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<WeekPreference> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(btnDone, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void getTimePreference() {
        progressDialog = CommonUtils.getProgressBar(this);
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<GetTimePreferenceResponse> call = api.getPreferenceApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<GetTimePreferenceResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetTimePreferenceResponse> call,
                                       @NonNull Response<GetTimePreferenceResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            GetTimePreferenceResponse getTimePreference = response.body();
                            if (getTimePreference != null && getTimePreference.getResponse().getCode() == 200) {
                                prefferedList = (ArrayList<Preffered>) getTimePreference.getResponse().getData().getPreferred();
                                availableList = (ArrayList<Available>) getTimePreference.getResponse().getData().getAvailable();
                            } else {
                                CommonUtils.showSnackbar(btnDone, getTimePreference.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        adapter = new TimeAdapter(PreferenceCalenderActivity.this, timeList/*dayBusinessHourMatch.get(day[0])*/,
                                0, btnDone, from);
                        rvTime.setAdapter(adapter);
                        adapter.setClickListener(new TimeAdapter.ItemClickListener() {
                            @Override
                            public void onSlotItemClick(View view, int position, String timeValue) {
                                adapter.toggleSelection(position, timeValue);
                            }

                            @Override
                            public boolean onSlotItemLongClick(View view, int position) {
                                mDragSelectTouchListener.startDragSelection(position);
                                return true;
                            }
                        });

                        mDragSelectionProcessor = new DragSelectionProcessor(new DragSelectionProcessor.ISelectionHandler() {
                            @Override
                            public ArrayList<String> getSelection() {
                                return adapter.getSelection();
                            }

                            @Override
                            public boolean isSelected(int index) {
                                return adapter.getSelection().contains(index);
                            }

                            @Override
                            public void updateSelection(int start, int end, boolean isSelected,
                                                        boolean calledFromOnStart) {
                                adapter.selectRange(start, end, isSelected);
                            }
                        }).withMode(mMode);

                        mDragSelectTouchListener = new DragSelectTouchListener()
                                .withSelectListener(mDragSelectionProcessor);
                        updateSelectionListener();
                        rvTime.addOnItemTouchListener(mDragSelectTouchListener);

                    } else {
                        CommonUtils.showSnackbar(btnDone, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetTimePreferenceResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(btnDone, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private int clickedPos;

    @Override
    public void itemClicked(View view, int position) {
        clickedPos = position;

        TimeAdapter.setData();
        adapter = new TimeAdapter(this, timeList/*dayBusinessHourMatch.get(day[clickedPos])*/, position, btnDone, from);
        rvTime.setAdapter(adapter);
        adapter.setClickListener(new TimeAdapter.ItemClickListener() {
            @Override
            public void onSlotItemClick(View view, int position, String timeValue) {
                adapter.toggleSelection(position, timeValue);
            }

            @Override
            public boolean onSlotItemLongClick(View view, int position) {
                // if one item is long pressed, we start the drag selection like following:
                // we just call this function and pass in the position of the first selected item
                // the selection processor does take care to update the positions selection mode correctly
                // and will correctly transform the touch events so that they can be directly applied to your adapter!!!
                mDragSelectTouchListener.startDragSelection(position);
                return true;
            }
        });

        mDragSelectionProcessor = new DragSelectionProcessor(new DragSelectionProcessor.ISelectionHandler() {
            @Override
            public ArrayList<String> getSelection() {
                return adapter.getSelection();
            }

            @Override
            public boolean isSelected(int index) {
                return adapter.getSelection().contains(index);
            }

            @Override
            public void updateSelection(int start, int end, boolean isSelected,
                                        boolean calledFromOnStart) {
                adapter.selectRange(start, end, isSelected);
            }
        }).withMode(mMode);

        mDragSelectTouchListener = new DragSelectTouchListener().withSelectListener(mDragSelectionProcessor);
        updateSelectionListener();
        rvTime.addOnItemTouchListener(mDragSelectTouchListener);

        for (int i = 0; i < daysList.size(); i++) {
            if (clickedPos == i) {
//              llDay.setBackgroundColor(Color.BLUE);
                daysList.get(i).setSelected(true);
            } else {
//              llDay.setBackgroundColor(Color.WHITE);
                daysList.get(i).setSelected(false);
            }
            weekDaysAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
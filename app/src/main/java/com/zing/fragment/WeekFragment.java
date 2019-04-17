package com.zing.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.zing.R;
import com.zing.model.request.EarningSlotRequest;
import com.zing.model.response.Week;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.weekGraph)
    GraphView weekGraph;
    Unbinder unbinder;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.line)
    View line;
    float width_x;
    int size;
    private String mParam1;
    private String mParam2;
    LineGraphSeries<DataPoint> series, dashSeries;
    SessionManagement session;
    ArrayList<Week> weekList = new ArrayList<>();
    ArrayList<Week> dashWeekList = new ArrayList<>();
    private String startWeek = "", endWeek = "";

    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                width_x = weekGraph.getWidth();
            }
        }, 100);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar c1 = Calendar.getInstance();

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        startWeek = getMonth(month1);
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH) + 1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);
        endWeek = getMonth(month7);

        tvMonth.setText(startWeek + " " + day1 + " - " + endWeek + " " + day7);
        if (NetworkUtils.isNetworkConnected(getActivity()))
            getWeekData();
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

    private ProgressDialog progressDialog;

    private void getWeekData() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            EarningSlotRequest earningSlotRequest = new EarningSlotRequest();
            earningSlotRequest.setSlot("week");

            Call<JsonElement> call = api.weekGraphApi("Bearer " + session.getUserToken(),
                    earningSlotRequest);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject responseObj = jsonObject.optJSONObject("response");
                            String code = responseObj.optString("code");
                            String message = responseObj.optString("message");

                            JSONArray jsonArray = responseObj.optJSONArray("week");

                            weekList.clear();
                            dashWeekList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject weekObj = jsonArray.optJSONObject(i);
                                Week week = new Week();
                                week.setDayXAxis(weekObj.optString("day(x-axis)"));
                                week.setShift(weekObj.optString("shift"));
                                week.setEarningsYAxis(weekObj.optString("earnings(y-axis)"));

                                switch (weekObj.optString("day(x-axis)")) {
                                    case "Sunday":
                                        week.setDay(0);
                                        break;
                                    case "Monday":
                                        week.setDay(1);
                                        break;
                                    case "Tuesday":
                                        week.setDay(2);
                                        break;
                                    case "Wednesday":
                                        week.setDay(3);
                                        break;
                                    case "Thursday":
                                        week.setDay(4);
                                        break;
                                    case "Friday":
                                        week.setDay(5);
                                        break;
                                    case "Saturday":
                                        week.setDay(6);
                                        break;
                                }
                                weekList.add(week);
                                if (weekObj.optString("shift").equalsIgnoreCase("completed")) {
                                    dashWeekList.add(week);
                                }
                            }

//                          series = new LineGraphSeries<>(getDataPoint(weekList));
                            Paint paint = new Paint();
                            Paint paint2 = new Paint();

                            series = new LineGraphSeries<>(getDataPoint(weekList));
                            if (dashWeekList.size() > 1)
                                dashSeries = new LineGraphSeries<>(getDataPoint(dashWeekList));

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setColor(Color.WHITE);
                            paint.setStrokeWidth(6);
                            paint.setPathEffect(new DashPathEffect(new float[]{24, 12}, 0));
                            series.setCustomPaint(paint);
                            series.setDrawAsPath(true);
                            weekGraph.addSeries(series);

                            if (dashWeekList.size() > 1) {
                                paint2.setStyle(Paint.Style.STROKE);
                                paint2.setColor(Color.WHITE);
                                paint2.setStrokeWidth(6);
                                paint2.setPathEffect(new DashPathEffect(new float[]{0, 0}, 0));
                                dashSeries.setCustomPaint(paint2);
                                dashSeries.setDrawAsPath(true);
                                weekGraph.addSeries(dashSeries);
                            }
                            size = weekList.size();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvEarning, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvEarning, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvMonth.setTypeface(AppTypeface.avenieNext_medium);
        tvEarning.setTypeface(AppTypeface.avenieNext_demibold);

        session = new SessionManagement(getActivity());

        weekGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                line.setVisibility(View.VISIBLE);
                setData(event, v);
                return true;
            }
        });

        weekGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        weekGraph.getViewport().setXAxisBoundsManual(true);
        weekGraph.getViewport().setMinX(0);
        weekGraph.getViewport().setMaxX(6);
        weekGraph.setTitleColor(getResources().getColor(R.color.graph_text));

        weekGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.graph_text));
        weekGraph.getGridLabelRenderer().setHighlightZeroLines(true);
        weekGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.graph_text));
        weekGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.graph_text));

        weekGraph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        weekGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        weekGraph.getGridLabelRenderer().reloadStyles();

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(weekGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"S", "M", "T", "W", "T", "F", "S"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", ""});
        weekGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

//      weekGraph.getViewport().setScrollable(true); // enables horizontal scrolling
//      weekGraph.getViewport().setScrollableY(true); // enables vertical scrolling
        weekGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        weekGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

    }

    @SuppressLint("SetTextI18n")
    public void setData(MotionEvent event, View v) {
        line.animate();
        int size = weekList.size();
        float screenX = event.getX();
        float screenY = event.getY();
        float width_x = v.getWidth();
        float viewX = screenX - v.getLeft();
        float viewY = screenY - v.getTop();
        float percent_x = (viewX / width_x);
        int pos = (int) (size * percent_x);

        System.out.println("X: " + viewX + " Y: " + viewY + " Percent = " + percent_x);
        System.out.println("YVal = " + weekList.get(pos).getEarningsYAxis());

        tvEarning.setText("$" + weekList.get(pos).getEarningsYAxis());

//        if(event.getX()<v.getX())
        line.setTranslationX(event.getX());
        line.invalidate();

    }

    private DataPoint[] getDataPoint(ArrayList<Week> weekList) {
        DataPoint[] dp = new DataPoint[weekList.size()];
        int i = 0;
        for (Week week : weekList) {
            dp[i] = new DataPoint(week.getDay(), Float.valueOf(week.getEarningsYAxis()));
            i++;
        }

        return dp;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
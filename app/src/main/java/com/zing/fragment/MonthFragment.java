package com.zing.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MonthFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.monthGraph)
    GraphView monthGraph;
    @BindView(R.id.line)
    View line;
    float width_x;
    int size;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    SessionManagement session;
    LineGraphSeries<DataPoint> series, dashSeries;
    String[] strings = {"1", "5", "10", "15", "20", "25", "30", "35"};
    ArrayList<Week> monthList = new ArrayList<>();
    ArrayList<Week> dashWeekList = new ArrayList<>();

    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
            getMonthData();
    }

    private void getMonthData() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            EarningSlotRequest earningSlotRequest = new EarningSlotRequest();
            earningSlotRequest.setSlot("month");

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

                            JSONArray jsonArray = responseObj.optJSONArray("month");

                            monthList.clear();
                          /*  Week week1 = new Week();
                            week1.setDayXAxis("0");
                            week1.setShift("completed");
                            week1.setEarningsYAxis("0.0");
                            week1.setDay(0);
                            monthList.add(week1);*/

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject weekObj = jsonArray.optJSONObject(i);
                                Week week = new Week();
                                week.setDayXAxis(weekObj.optString("day(x-axis)"));
                                week.setShift(weekObj.optString("shift"));
                                week.setEarningsYAxis(weekObj.optString("earnings(y-axis)"));

                                String date = weekObj.optString("day(x-axis)");
                                Date date1 = null;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    date1 = format.parse(date);
                                    String day = (String) DateFormat.format("dd", date1); // 20
                                    week.setDay(Float.parseFloat(day));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                monthList.add(week);
                                if (weekObj.optString("shift").equalsIgnoreCase("completed")) {
                                    dashWeekList.add(week);
                                }
                            }

                            Paint paint = new Paint();
                            Paint paint2 = new Paint();

                            series = new LineGraphSeries<>(getDataPoint(monthList));
                            dashSeries = new LineGraphSeries<>(getDataPoint(dashWeekList));

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setColor(Color.WHITE);
                            paint.setStrokeWidth(6);
                            paint.setPathEffect(new DashPathEffect(new float[]{24, 12}, 0));
                            series.setCustomPaint(paint);
                            series.setDrawAsPath(true);
                            monthGraph.addSeries(series);

                            paint2.setStyle(Paint.Style.STROKE);
                            paint2.setColor(Color.WHITE);
                            paint2.setStrokeWidth(6);
                            paint2.setPathEffect(new DashPathEffect(new float[]{0, 0}, 0));
                            dashSeries.setCustomPaint(paint2);
                            dashSeries.setDrawAsPath(true);
                            monthGraph.addSeries(dashSeries);

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

    private DataPoint[] getDataPoint(ArrayList<Week> monthList) {
        DataPoint[] dp = new DataPoint[monthList.size()];
        int i = 0;
        for (Week week : monthList) {
            dp[i] = new DataPoint(week.getDay(), Float.valueOf(week.getEarningsYAxis()));
            i++;
        }

        return dp;
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
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvMonth.setTypeface(AppTypeface.avenieNext_medium);
        tvEarning.setTypeface(AppTypeface.avenieNext_demibold);

        session = new SessionManagement(getActivity());

        Date c = Calendar.getInstance().getTime();
        String monthString = (String) DateFormat.format("MMM", c); // Jun
        switch (monthString) {
            case "Jan":
                tvMonth.setText("Jan 1 to Jan 31");
                break;
            case "Feb":
                tvMonth.setText("Feb 1 to Feb 28");
                break;
            case "Mar":
                tvMonth.setText("Mar 1 to Mar 31");
                break;
            case "Apr":
                tvMonth.setText("Apr 1 to Apr 30");
                break;
            case "May":
                tvMonth.setText("May 1 to May 31");
                break;
            case "Jun":
                tvMonth.setText("Jun 1 to Jun 30");
                break;
            case "Jul":
                tvMonth.setText("Jul 1 to Jul 31");
                break;
            case "Aug":
                tvMonth.setText("Aug 1 to Aug 31");
                break;
            case "Sep":
                tvMonth.setText("Sep 1 to Sep 30");
                break;
            case "Oct":
                tvMonth.setText("Oct 1 to Oct 31");
                break;
            case "Nov":
                tvMonth.setText("Nov 1 to Nov 30");
                break;
            case "Dec":
                tvMonth.setText("Dec 1 to Dec 31");
                break;
        }


        monthGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        monthGraph.getViewport().setXAxisBoundsManual(true);
        monthGraph.getViewport().setMinX(0);
        monthGraph.getViewport().setMaxX(35);
        monthGraph.setTitleColor(getResources().getColor(R.color.graph_text));

        monthGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.graph_text));
        monthGraph.getGridLabelRenderer().setHighlightZeroLines(true);
        monthGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.graph_text));
        monthGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.graph_text));

        monthGraph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        monthGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        monthGraph.getGridLabelRenderer().reloadStyles();

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(monthGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"1", "5", "10", "15", "20", "25", "30", "35"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", ""});
        monthGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

//      monthGraph.getViewport().setScrollable(true); // enables horizontal scrolling
//      monthGraph.getViewport().setScrollableY(true); // enables vertical scrolling
        monthGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        monthGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        monthGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setData(event, v);
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setData(MotionEvent event, View v) {
        line.animate();
        int size = monthList.size();
        float screenX = event.getX();
        float screenY = event.getY();
        float width_x = v.getWidth();
        float viewX = screenX - v.getLeft();
        float viewY = screenY - v.getTop();
        float percent_x = (viewX / width_x);
        int pos = (int) (size * percent_x);

        System.out.println("X: " + viewX + " Y: " + viewY + " Percent = " + percent_x);
        System.out.println("YVal = " + monthList.get(pos).getEarningsYAxis());

        tvEarning.setText("$" + monthList.get(pos).getEarningsYAxis());

//        if(event.getX()<v.getX())
        line.setTranslationX(event.getX());

        line.invalidate();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

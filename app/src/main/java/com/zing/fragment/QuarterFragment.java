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

public class QuarterFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.quarterGraph)
    GraphView quarterGraph;
    Unbinder unbinder;
    LineGraphSeries<DataPoint> series, dashSeries;
    @BindView(R.id.line)
    View line;
    float width_x;
    int size;

    private String mParam1;
    private String mParam2;
    SessionManagement session;
    private ProgressDialog progressDialog;
    ArrayList<Week> quarterList;
    ArrayList<Week> dashWeekList;

    public static QuarterFragment newInstance(String param1, String param2) {
        QuarterFragment fragment = new QuarterFragment();
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
        View view = inflater.inflate(R.layout.fragment_quarter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
            getQuarterData();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvMonth.setTypeface(AppTypeface.avenieNext_medium);
        tvEarning.setTypeface(AppTypeface.avenieNext_demibold);
        quarterList = new ArrayList<>();
        dashWeekList = new ArrayList<>();
        session = new SessionManagement(getActivity());

        quarterGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        quarterGraph.getViewport().setXAxisBoundsManual(true);
        quarterGraph.getViewport().setMinX(0);
        quarterGraph.getViewport().setMaxX(60);
        quarterGraph.setTitleColor(Color.WHITE);

        quarterGraph.getGridLabelRenderer().setGridColor(Color.WHITE);
        quarterGraph.getGridLabelRenderer().setHighlightZeroLines(true);
        quarterGraph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        quarterGraph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);

        quarterGraph.getViewport().setScrollable(false); // enables horizontal scrolling
        quarterGraph.getViewport().setScrollableY(false); // enables vertical scrolling
        quarterGraph.getViewport().setScalable(false); // enables horizontal zooming and scrolling
        quarterGraph.getViewport().setScalableY(false); // enables vertical zooming and scrolling

        quarterGraph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        quarterGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        quarterGraph.getGridLabelRenderer().reloadStyles();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String monthName = "";
//        int value = month ;
        switch (month) {
            case 0:
                monthName = "Jan1";
                tvMonth.setText("Nov 1 to Jan 1");
                break;
            case 1:
                monthName = "Feb1";
                tvMonth.setText("Dec 1 to Feb 1");
                break;
            case 2:
                monthName = "Mar1";
                tvMonth.setText("Jan 1 to Mar 1");
                break;
            case 3:
                monthName = "Apr1";
                tvMonth.setText("Feb 1 to Apr 1");
                break;
            case 4:
                monthName = "May1";
                tvMonth.setText("Mar 1 to May 1");
                break;
            case 5:
                monthName = "Jun1";
                tvMonth.setText("Apr 1 to Jun 1");
                break;
            case 6:
                monthName = "Jul1";
                tvMonth.setText("May 1 to Jul 1");
                break;
            case 7:
                monthName = "Aug1";
                tvMonth.setText("Jun 1 to Aug 1");
                break;
            case 8:
                monthName = "Sep1";
                tvMonth.setText("Jul 1 to Sep 1");
                break;
            case 9:
                monthName = "Oct1";
                tvMonth.setText("Aug 1 to Oct 1");
                break;
            case 10:
                monthName = "Nov1";
                tvMonth.setText("Sep 1 to Nov 1");
                break;
            case 11:
                monthName = "Dec1";
                tvMonth.setText("Oct 1 to Dec 1");
                break;
        }

        ArrayList<String> stringArrayList = new ArrayList<String>();

        for (int i = 0; i < 4; i++) {
            int value = month + i;
            switch (value) {
                case 0:
                    monthName = "Nov1";
                    break;
                case 1:
                    monthName = "Dec1";
                    break;
                case 2:
                    monthName = "Jan1";
                    break;
                case 3:
                    monthName = "Feb1";
                    break;
                case 4:
                    monthName = "Mar1";
                    break;
                case 5:
                    monthName = "Apr1";
                    break;
                case 6:
                    monthName = "May1";
                    break;
                case 7:
                    monthName = "Jun1";
                    break;
                case 8:
                    monthName = "Jul1";
                    break;
                case 9:
                    monthName = "Aug1";
                    break;
                case 10:
                    monthName = "Sep1";
                    break;
                case 11:
                    monthName = "Oct1";
                    break;
                case 12:
                    monthName = "Nov1";
                    break;
                case 13:
                    monthName = "Dec1";
                    break;
                case 14:
                    monthName = "Jan1";
                    break;
                case 15:
                    monthName = "Feb1";
                    break;
            }
            stringArrayList.add(monthName); //add to arraylist
            monthName = "";
        }

//      if you want your array
        String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(quarterGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{stringArray[0], stringArray[1], stringArray[2], stringArray[3]});
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", ""});
        quarterGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        quarterGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        quarterGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        quarterGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if (quarterList.size() > 0)
                setData(event, v);
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setData(MotionEvent event, View v) {
        line.animate();
        int size = quarterList.size();
        float screenX = event.getX();
        float screenY = event.getY();
        float width_x = v.getWidth();
        float viewX = screenX - v.getLeft();
        float viewY = screenY - v.getTop();
        float percent_x = (viewX / width_x);
        int pos = (int) (size * percent_x);

        System.out.println("X: " + viewX + " Y: " + viewY + " Percent = " + percent_x);
        System.out.println("YVal = " + quarterList.get(pos).getEarningsYAxis());

        tvEarning.setText("$" + quarterList.get(pos).getEarningsYAxis());

//        if(event.getX()<v.getX())
        line.setTranslationX(event.getX());

        line.invalidate();
    }

    private void getQuarterData() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            EarningSlotRequest earningSlotRequest = new EarningSlotRequest();
            earningSlotRequest.setSlot("quarter");

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

                            JSONArray jsonArray = responseObj.optJSONArray("quarter");

                            quarterList.clear();
                          /*  Week week1 = new Week();
                            week1.setDayXAxis("0");
                            week1.setShift("completed");
                            week1.setEarningsYAxis("0.0");
                            week1.setDay(0);
                            quarterList.add(week1);*/

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject weekObj = jsonArray.optJSONObject(i);
                                Week week = new Week();
                                week.setDayXAxis(weekObj.optString("date"));
                                week.setShift(weekObj.optString("shift"));
                                week.setEarningsYAxis(weekObj.optString("earnings(y-axis)"));
                                week.setDay(Float.parseFloat(weekObj.optString("day(x-axis)")));

                              /*  String date = weekObj.optString("date");
                                Date date1 = null;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    date1 = format.parse(date);
                                    String day = (String) DateFormat.format("dd", date1); // 20
                                    week.setDay(Float.parseFloat(day));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*/

                                quarterList.add(week);

                                if (weekObj.optString("shift").equalsIgnoreCase("completed")) {
                                    dashWeekList.add(week);
                                }
                            }

                            Paint paint = new Paint();
                            Paint paint2 = new Paint();

                            series = new LineGraphSeries<>(getDataPoint(quarterList));
                            dashSeries = new LineGraphSeries<>(getDataPoint(dashWeekList));

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setColor(Color.WHITE);
                            paint.setStrokeWidth(6);
                            paint.setPathEffect(new DashPathEffect(new float[]{24, 12}, 0));
                            series.setCustomPaint(paint);
                            series.setDrawAsPath(true);
                            quarterGraph.addSeries(series);

                            paint2.setStyle(Paint.Style.STROKE);
                            paint2.setColor(Color.WHITE);
                            paint2.setStrokeWidth(6);
                            paint2.setPathEffect(new DashPathEffect(new float[]{0, 0}, 0));
                            dashSeries.setCustomPaint(paint2);
                            dashSeries.setDrawAsPath(true);
                            quarterGraph.addSeries(dashSeries);

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

    private DataPoint[] getDataPoint(ArrayList<Week> quarterList) {
        DataPoint[] dp = new DataPoint[quarterList.size()];
        int i = 0;
        for (Week week : quarterList) {
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

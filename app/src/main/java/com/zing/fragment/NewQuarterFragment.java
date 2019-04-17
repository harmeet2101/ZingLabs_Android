package com.zing.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorLong;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.JsonElement;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.zing.R;
import com.zing.model.request.EarningSlotRequest;
import com.zing.model.response.Week;
import com.zing.util.CommonUtils;
import com.zing.util.MyMarkerView;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewQuarterFragment extends BaseFragment implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.chart)
    LineChart mChart;
    @BindView(R.id.line)
    View line;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    SessionManagement session;
    private ProgressDialog progressDialog;
    private ArrayList<Week> quarterList;
    private ArrayList<Week> dashWeekList;

    public static NewQuarterFragment newInstance(String param1, String param2) {
        NewQuarterFragment fragment = new NewQuarterFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_quarter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManagement(getActivity());

        quarterList = new ArrayList<>();
        dashWeekList = new ArrayList<>();
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view,tvEarning);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(0f, "");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(8f, 0f, 0f);

        llXAxis.setTextSize(10f);
        llXAxis.setTextColor(Color.WHITE);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 0f, 0f);
        xAxis.setGridColor(Color.TRANSPARENT);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setZeroLineColor(Color.WHITE);
        leftAxis.setAxisMaximum(300f);
        leftAxis.setAxisMinimum(0f);
//        leftAxis.setEnabled(false);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

//        mChart.getAxisLeft().setDrawGridLines(false);
//        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
//        mChart.getXAxis().setDrawLabels(false);

        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        getQuarterData();

        //  dont forget to refresh the drawing
         mChart.invalidate();
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

                          /*
                            Week week1 = new Week();
                            week1.setDayXAxis("0");
                            week1.setShift("completed");
                            week1.setEarningsYAxis("0.0");
                            week1.setDay(0);
                            quarterList.add(week1); */

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
    /*
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
                            quarterGraph.addSeries(dashSeries);*/
                            setData(quarterList);
                            mChart.animateX(2500);
                            //mChart.invalidate();

                            // get the legend (only possible after setting data)
                            Legend l = mChart.getLegend();

                            // modify the legend ...
                            l.setForm(Legend.LegendForm.LINE);

//                            mChart.invalidate();
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

    private void setData(ArrayList<Week> quarterList) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < quarterList.size(); i++) {
            float val = Float.valueOf(quarterList.get(i).getEarningsYAxis());
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.bell)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setColor(Color.WHITE);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");
            set1.setDrawCircles(false);
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setHighlightEnabled(true);  // value selection color
            set1.setHighLightColor(Color.WHITE);
            set1.setHighlightLineWidth(2f);

            set1.setColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f); // text size of values on chart
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

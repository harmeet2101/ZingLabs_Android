package com.zing.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;
import com.zing.R;
import com.zing.calendar.CalendarCustomView;
import com.zing.model.CalendarDataModel;
import com.zing.model.request.CalendarRequest;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.DividerDecoration;
import com.zing.util.NetworkUtils;
import com.zing.util.RecyclerItemClickListener;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarMonthFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.rvCalendar)
    RecyclerView rvCalendar;
    LinearLayoutManager layoutManager;
    @BindView(R.id.sun)
    TextView sun;
    @BindView(R.id.mon)
    TextView mon;
    @BindView(R.id.tue)
    TextView tue;
    @BindView(R.id.wed)
    TextView wed;
    @BindView(R.id.thu)
    TextView thu;
    @BindView(R.id.fri)
    TextView fri;
    @BindView(R.id.sat)
    TextView sat;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    private int mParam1;
    private String mParam2;
    private boolean bottomScroll = true;
    private ProgressDialog progressDialog;
    SessionManagement session;
    private HashMap<String, ArrayList<CalendarDataModel>> calendarData;
    private HashMap<String, String> shiftData;
    public Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    public Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
    int count = 0;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private CalendarAdapter adapter;

    private int currentMonth;
    private int currentYear;
    private String monthString;
    private ArrayList<String> monthList = new ArrayList<>();

    public static CalendarMonthFragment newInstance(int param1, String param2) {
        CalendarMonthFragment fragment = new CalendarMonthFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_month, container, false);
        unbinder = ButterKnife.bind(this, view);
        session = new SessionManagement(getActivity());
        calendarData = new HashMap<>();
        shiftData = new HashMap<>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
            calendarMonthDetails();
    }

    private void calendarMonthDetails() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setSlot("monthly");

            Call<JsonElement> call = api.calendarSlotDetailsApi("Bearer " + session.getUserToken(),
                    calendarRequest);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject responseObj = new JSONObject(response.body().toString());
                            JSONObject jsonObject = responseObj.optJSONObject("response");
                            String code = jsonObject.optString("code");
                            String message = jsonObject.optString("message");

                            calendarData.clear();
                            shiftData.clear();

                            JSONArray calendarArr = jsonObject.optJSONArray("calendar");
                            for (int i = 0; i < calendarArr.length(); i++) {
                                JSONObject calendarObj = calendarArr.optJSONObject(i);

                                String date = calendarObj.optString("date");
                                String event = calendarObj.optString("event");
                                String event_color = calendarObj.optString("event_color");
                                String shift_id = calendarObj.optString("shift_id");
                                String shift_status = calendarObj.optString("shift_status");
                                CalendarDataModel tempModel = new CalendarDataModel();
                                tempModel.setDate(date);
                                tempModel.setEvent(event);
                                tempModel.setEventColor(event_color);
                                tempModel.setShiftId(shift_id);
                                tempModel.setShiftStatus(shift_status);
                                ArrayList<CalendarDataModel> calendarModelList;
                                if (calendarData.get(date) != null) {
                                    calendarModelList = calendarData.get(date);
                                    calendarModelList.add(tempModel);
                                    calendarData.put(date, calendarModelList);
                                } else {
                                    calendarModelList = new ArrayList<>();
                                    calendarModelList.add(tempModel);
                                    calendarData.put(date, calendarModelList);
                                }

                                shiftData.put(date, shift_id);
                            }

                            adapter.notifyDataSetChanged();

                           /* rvCalendar.setLayoutManager(layoutManager);
                            adapter = new CalendarAdapter();
                            rvCalendar.setAdapter(adapter);*/

                        } catch (Exception e) {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        sun.setTypeface(AppTypeface.avenieNext_regular);
        mon.setTypeface(AppTypeface.avenieNext_regular);
        tue.setTypeface(AppTypeface.avenieNext_regular);
        wed.setTypeface(AppTypeface.avenieNext_regular);
        thu.setTypeface(AppTypeface.avenieNext_regular);
        fri.setTypeface(AppTypeface.avenieNext_regular);
        sat.setTypeface(AppTypeface.avenieNext_regular);
        tvMonth.setTypeface(AppTypeface.avenieNext_regular);

        layoutManager = new LinearLayoutManager(getActivity());

        String date = session.getRegistrationDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = format.format(cal.getTime());
        try {
            Date startDate = format.parse(date);
            Date endDate = format.parse(date2);

            count = monthsBetweenDates(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String regDate = session.getRegistrationDate();
        String[] separated = regDate.split("-");

        int year = Integer.parseInt(separated[0]);
        int month = Integer.parseInt(separated[1]);

        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH);

        year = year - current_year;
        month = month - current_month;

        cal.add(Calendar.MONTH, month - 1);
        cal.add(Calendar.YEAR, year);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);

        monthList.clear();
        for (int i = 0; i < count + 7; i++) {

            if (i == 0) {
                cal.add(Calendar.MONTH, 0);
            } else {
                cal.add(Calendar.MONTH, 1);
            }

            String monthName = formatter.format(cal.getTime());
            monthList.add(monthName);
        }

        rvCalendar.setLayoutManager(layoutManager);
        adapter = new CalendarAdapter();
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rvCalendar.addItemDecoration(headersDecor);
        rvCalendar.scrollToPosition(count);

        // Add decoration for dividers between list items
        rvCalendar.addItemDecoration(new DividerDecoration(getActivity()));
        rvCalendar.setAdapter(adapter);
        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(rvCalendar, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(getActivity(), "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        rvCalendar.addOnItemTouchListener(touchListener);
        rvCalendar.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                                    adapter.remove(adapter.getItem(position));
                    }
                }));

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }

    public int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.calendar_row, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            CalendarCustomView custom_calendar = (CalendarCustomView) holder.itemView;
            custom_calendar.setNextButtonClickEvent(position, calendarData, shiftData, fragmentInterface);
        }

        @Override
        public int getItemCount() {
            return count + 7;
        }

        @Override
        public long getHeaderId(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_header, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            TextView textView = (TextView) holder.itemView;
            textView.setTypeface(AppTypeface.avenieNext_regular);
            textView.setText(monthList.get(position));
        }
    }
}

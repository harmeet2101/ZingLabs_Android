package com.zing.calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.fragment.CalendarMonthFragment;
import com.zing.fragment.ShiftByDateFragment;
import com.zing.fragment.ShiftDialogFragment;
import com.zing.interfaces.FragmentInterface;
import com.zing.model.CalendarDataModel;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.request.ShiftDetailByDateRequest;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.model.response.shiftbydate.ShiftByDateBaseModel;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private HashMap<String, ArrayList<CalendarDataModel>> calendarData;
    private HashMap<String, String> shiftData;
    private String shiftId = "";
    private ProgressDialog progressDialog;
    private LinearLayout llDate;
    SessionManagement session;
    private FragmentInterface fragmentInterface;

    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate,
                       HashMap<String, ArrayList<CalendarDataModel>> calendarData, HashMap<String, String> shiftData,
                       FragmentInterface fragmentInterface) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.calendarData = calendarData;
        this.shiftData = shiftData;
        this.fragmentInterface = fragmentInterface;
        session = new SessionManagement(context);
        mInflater = LayoutInflater.from(context);
        AppTypeface.getTypeFace(context);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        //Add day to calendar
        TextView cellNumber = view.findViewById(R.id.calendar_date_id);
        cellNumber.setTypeface(AppTypeface.avenieNext_regular);
        cellNumber.setText(String.valueOf(dayValue));

        //Add events to the calendar
        ImageView eventIndicator = view.findViewById(R.id.event_id);
        ImageView eventTwo = view.findViewById(R.id.event_two);
        ImageView eventThree = view.findViewById(R.id.event_three);

        llDate = view.findViewById(R.id.llDate);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            cellNumber.setTextColor(Color.GRAY);
            eventIndicator.setVisibility(View.VISIBLE);
        } else {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            cellNumber.setTextColor(Color.WHITE);
            eventIndicator.setVisibility(View.INVISIBLE);
        }

        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = targetFormat.format(mDate);

        eventIndicator.setVisibility(View.GONE);
        eventTwo.setVisibility(View.GONE);
        eventThree.setVisibility(View.GONE);

        if (calendarData.get(formattedDate) != null) {
            ArrayList<CalendarDataModel> dataModel = calendarData.get(formattedDate);
            switch (dataModel.size()) {
                case 1:
                    setColorDot(dataModel.get(0).getEventColor(), eventIndicator);
                    break;
                case 2:
                    setColorDot(dataModel.get(0).getEventColor(), eventIndicator);
                    setColorDot(dataModel.get(1).getEventColor(), eventTwo);
                    break;
                case 3:
                    setColorDot(dataModel.get(0).getEventColor(), eventIndicator);
                    setColorDot(dataModel.get(1).getEventColor(), eventTwo);
                    setColorDot(dataModel.get(2).getEventColor(), eventThree);
                    break;
            }

            if (dataModel.size() > 3) {
                setColorDot(dataModel.get(0).getEventColor(), eventIndicator);
                setColorDot(dataModel.get(1).getEventColor(), eventTwo);
                setColorDot(dataModel.get(2).getEventColor(), eventThree);
            }
        }

        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftId = shiftData.get(formattedDate);
                if (shiftId != null) {
//                    getShiftDetails(formattedDate, calendarData.get(formattedDate));
                    getShiftDetailByDate(formattedDate);
                }
            }
        });

        return view;
    }


    private void setColorDot(String color, ImageView eventIndicator) {
        eventIndicator.setVisibility(View.VISIBLE);
        switch (color) {
            case "Blue":
                eventIndicator.setImageResource(R.drawable.blue_circle);
                break;
            case "orange":
                eventIndicator.setImageResource(R.drawable.orange_circle);
                break;
            case "green":
                eventIndicator.setImageResource(R.drawable.green_circle);
                break;
            case "red":
                eventIndicator.setImageResource(R.drawable.red_circle);
                break;
            default:
                eventIndicator.setImageResource(R.drawable.white_circle);
                break;
        }
    }

//    Url: http://zira.n1.iworklab.com/public/api/shift_by_date
//    request body : {"date":"2019-03-29"}
//    End point is: shift_by_date
//    method post
    private void getShiftDetailByDate(final String formattedDate) {
        progressDialog = CommonUtils.getProgressBar(getContext());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            ShiftDetailByDateRequest dateBean = new ShiftDetailByDateRequest();
            dateBean.setDate(formattedDate);

            Call<ShiftByDateBaseModel> call = api.shiftByDate("Bearer " + session.getUserToken(), dateBean);
            call.enqueue(new Callback<ShiftByDateBaseModel>() {
                @Override
                public void onResponse(@NonNull Call<ShiftByDateBaseModel> call,
                                       @NonNull Response<ShiftByDateBaseModel> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            ShiftByDateBaseModel shiftDetailResponse = response.body();
                            if (shiftDetailResponse != null && shiftDetailResponse.getResponse().getCode() == 200) {
                                Fragment fragment = new ShiftByDateFragment();
                                Bundle bundle =new Bundle();
                                bundle.putString("date", formattedDate);
                                fragment.setArguments(bundle);
                                fragmentInterface.fragmentResult(fragment, "+");
                            } else {
                                CommonUtils.showSnackbar(llDate, shiftDetailResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(llDate, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShiftByDateBaseModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(llDate, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void getShiftDetails(final String formattedDate, final ArrayList<CalendarDataModel> calendarDataModels) {
        progressDialog = CommonUtils.getProgressBar(getContext());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            ShiftCheckInRequest shiftCheckInRequest = new ShiftCheckInRequest();
            shiftCheckInRequest.setShiftId(shiftId);

            Call<ShiftDetailResponse> call = api.shiftDetailApi("Bearer " +
                    session.getUserToken(), shiftCheckInRequest);
            call.enqueue(new Callback<ShiftDetailResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShiftDetailResponse> call,
                                       @NonNull Response<ShiftDetailResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            ShiftDetailResponse shiftDetailResponse = response.body();
                            if (shiftDetailResponse != null && shiftDetailResponse.getResponse().getCode() == 200) {
                                String day_no = "", monthString = "", day = "";
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date1 = format.parse(shiftDetailResponse.getResponse().getData().getDate());
                                    day_no = (String) android.text.format.DateFormat.format("dd", date1); // 20
                                    monthString = (String) android.text.format.DateFormat.format("MMM", date1); // Jun
                                    day = (String) android.text.format.DateFormat.format("EEE", date1); // Thursday

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Fragment fragment = ShiftDialogFragment.newInstance("calendar", shiftDetailResponse.getResponse().getData().getShiftId(),
                                        formattedDate,
                                        monthString + " " + day_no + ", " + day,
                                        shiftDetailResponse.getResponse().getData().getExpectedEarning(),
                                        shiftDetailResponse.getResponse().getData().getTimeSlot(),
                                        shiftDetailResponse.getResponse().getData().getLocation(),
                                        shiftDetailResponse.getResponse().getData().getRole(), "", "", calendarDataModels);
                                fragmentInterface.fragmentResult(fragment, "+");

                            } else {
                                CommonUtils.showSnackbar(llDate, shiftDetailResponse.getResponse().getMessage());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(llDate, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShiftDetailResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(llDate, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

}
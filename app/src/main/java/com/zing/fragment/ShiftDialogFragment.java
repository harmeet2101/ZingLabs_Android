package com.zing.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.adapter.RecomendedShiftAdapter;
import com.zing.adapter.ShiftCalendarAdapter;
import com.zing.model.CalendarDataModel;
import com.zing.model.request.ReleaseShiftRequest;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.request.VerifyCheckInRequest;
import com.zing.model.response.ShiftDetailResponse.Data;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.GeocodingLocation;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftDialogFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";
    private static final String ARG_PARAM10 = "param10";
    private static final String ARG_PARAM11 = "param11";

    Unbinder unbinder;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.textviewshiftType)
    TextView textviewshiftType;
    @BindView(R.id.tvEarningAmount)
    TextView tvEarningAmount;
    @BindView(R.id.tvLocationDetail)
    TextView tvLocationDetail;
    @BindView(R.id.ivRectangle)
    ImageView ivRectangle;
    @BindView(R.id.ivDiamond)
    ImageView ivDiamond;
    @BindView(R.id.rlDialog)
    LinearLayout rlDialog;
    @BindView(R.id.btnReleaseShift)
    Button btnReleaseShift;
    @BindView(R.id.btnCallManager)
    Button btnCallManager;
    @BindView(R.id.lay01)
    ViewGroup lay01;
    @BindView(R.id.lay02)
    ViewGroup lay02;
    @BindView(R.id.lay03)
    ViewGroup lay03;
    @BindView(R.id.lay04)
    ViewGroup lay04;
    @BindView(R.id.tvRoleDetail)
    TextView tvRoleDetail;
    @BindView(R.id.llcheckin)
    ViewGroup llCheckin;
    @BindView(R.id.llcheckout)
    ViewGroup llCheckout;

    private ProgressDialog progressDialog;
    SessionManagement session;

    private String shift_id, date, day, expectedEarning, timeSlot, location, role, release = "", nextShiftId, shiftType;
    private String from;
    private int PERMISSION = 0;
    private boolean isWithin24hrs ,autoCheckin;
//    private ArrayList<CalendarDataModel> calendarDataModel;

    public static ShiftDialogFragment newInstance(String param1, String param2,
                                                  String param3, String param4,
                                                  String param5, String param6,
                                                  String param7, String param8,
                                                  String param9, String param10, ArrayList<CalendarDataModel> calendarDataModels) {
        ShiftDialogFragment fragment = new ShiftDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        args.putString(ARG_PARAM10, param10);
        args.putSerializable(ARG_PARAM11, calendarDataModels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManagement(getActivity());
        if (getArguments() != null) {
            from = getArguments().getString(ARG_PARAM1);
            shift_id = getArguments().getString(ARG_PARAM2);
            date = getArguments().getString(ARG_PARAM3);
            day = getArguments().getString(ARG_PARAM4);
            expectedEarning = getArguments().getString(ARG_PARAM5);
            timeSlot = getArguments().getString(ARG_PARAM6);
            location = getArguments().getString(ARG_PARAM7);
            role = getArguments().getString(ARG_PARAM8);
            release = getArguments().getString(ARG_PARAM9);
            shiftType = getArguments().getString(ARG_PARAM10);
//            calendarDataModel = (ArrayList<CalendarDataModel>) getArguments().getSerializable(ARG_PARAM11);
        }
        getShiftDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shift_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());
        tvClose.setTypeface(AppTypeface.avenieNext_regular);
        tvEarningAmount.setTypeface(AppTypeface.avenieNext_regular);
        tvLocationDetail.setTypeface(AppTypeface.avenieNext_regular);

        btnReleaseShift.setTypeface(AppTypeface.avenieNext_demibold);
        btnCallManager.setTypeface(AppTypeface.avenieNext_demibold);

        textviewshiftType.setTypeface(AppTypeface.avenieNext_regular);
        tvDay.setTypeface(AppTypeface.avenieNext_medium);
        tvStartTime.setTypeface(AppTypeface.avenieNext_regular);
        tvEndTime.setTypeface(AppTypeface.avenieNext_regular);
        nextShiftId = session.getNextShift();



        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        tvEarningAmount.setText(expectedEarning);
        tvLocationDetail.setText(location);
        tvRoleDetail.setText(role);
        try {
            Date date1 = format.parse(date);
            String day_no = (String) DateFormat.format("dd", date1); // 20
            String monthString = (String) DateFormat.format("MMM", date1); // Jun
            day = (String) DateFormat.format("EEE", date1); // Thursday

            tvDay.setText(monthString + " " + day_no + ", " + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {

            Date currentDate = new Date();
            String pattern = "yyyy-MM-dd hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            StringTokenizer tokenizer = new StringTokenizer(timeSlot, "-");
            String startTime = tokenizer.nextToken();
            String endTime = tokenizer.nextToken();

            tvStartTime.setText(startTime);
            tvEndTime.setText(endTime);

            String mDateString = date + " " + startTime.toUpperCase();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 1);
            Date nxtDate = cal.getTime();
            String nextDateString = simpleDateFormat.format(nxtDate);
            Date nextDate = simpleDateFormat.parse(nextDateString);
            Date nextShiftDate = simpleDateFormat.parse(mDateString);

            if (nextShiftDate.compareTo(nextDate) < 0) {
                isWithin24hrs = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        session.setDialogData(from, shift_id, date, day, expectedEarning, timeSlot, location, role);

        if (isWithin24hrs) {
            btnReleaseShift.setVisibility(View.GONE);
        } else {
            btnReleaseShift.setVisibility(View.VISIBLE);
        }
        if (from.equalsIgnoreCase("home")) {

            btnReleaseShift.setVisibility(View.VISIBLE);
            btnReleaseShift.setText(getResources().getString(R.string.check_in));
            btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
        } else if (from.equalsIgnoreCase("calendar")) {
            btnReleaseShift.setVisibility(View.GONE);
            btnCallManager.setVisibility(View.GONE);
        } else {
            if (release.equalsIgnoreCase("0")) {

                btnReleaseShift.setText("RELEASE SHIFT");
            } else if (release.equalsIgnoreCase("1")) {

                btnReleaseShift.setText("UNDO RELEASE");
                btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {

                btnReleaseShift.setText(getResources().getString(R.string.check_in));
                btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
        if (shift_id.equalsIgnoreCase(nextShiftId)) {

            btnReleaseShift.setVisibility(View.VISIBLE);
            textviewshiftType.setText("Next Shift");
            btnReleaseShift.setText(getResources().getString(R.string.check_in));
            btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
        }*/


        switch (shiftType) {

            case "NOSHOW":
                textviewshiftType.setText("No Show");
                btnReleaseShift.setVisibility(View.GONE);
                rlDialog.setBackgroundColor(getResources().getColor(R.color.now_show_bg));
                lay01.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay02.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay03.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay04.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                llCheckin.setVisibility(View.VISIBLE);
                llCheckout.setVisibility(View.VISIBLE);
                break;

            case "COMPLETED":
                textviewshiftType.setText("Completed Shift");
                break;
            case "UPCOMING":
                textviewshiftType.setText("Upcoming Shift");
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Fragment fragment;

    @Nullable
    @OnClick({R.id.tvClose, R.id.ivRectangle, R.id.ivDiamond, R.id.btnReleaseShift, R.id.btnCallManager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvClose:
                /*if (from.equalsIgnoreCase("home")||from.equalsIgnoreCase("upcoming")) {
                    Fragment fragment = HomeFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment, "");
                } else if (from.equalsIgnoreCase("calendar") ||
                        (from.equalsIgnoreCase("calendarWeek"))) {
                    Fragment fragment = CalenderFragment.newInstance("", "");
                    fragmentInterface.fragmentResult(fragment, "");
                }*/
                getActivity().onBackPressed();
                break;
            case R.id.ivRectangle:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.WRITE_CALENDAR)
                                    != PackageManager.PERMISSION_GRANTED) {

                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_CALENDAR,
                                        Manifest.permission.WRITE_CALENDAR},
                                PERMISSION);
                    } else {
                        syncCalendar();
                    }
                } else {
                    syncCalendar();
                }

                break;
            case R.id.ivDiamond:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (location.equalsIgnoreCase("")) {
                            //CommonUtils.showSnackbar(ivDiamond, "No Location Found");
                        } else {
                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(location,
                                    getActivity(), new GeocoderHandler());
                        }
                    }
                }, 1000);

                break;
            case R.id.btnReleaseShift:
                if (btnReleaseShift.getText().toString().equalsIgnoreCase("RELEASE SHIFT")) {
                    release = "1";
                    openDialog("Release Confirmation", "Once released the shift will be open for the rest of the team to claim.");
                } else if (btnReleaseShift.getText().toString().equalsIgnoreCase("UNDO RELEASE")) {
                    release = "0";
                    if (NetworkUtils.isNetworkConnected(getActivity()))
                        releaseShift();
                }
                else {
                    Fragment fragment = VerifyOtpFragment.newInstance(shift_id, from);
                    fragmentInterface.fragmentResult(fragment, "");
                }
                break;
            case R.id.btnCallManager:
                String phone = session.getManagerNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        phone, null));
                startActivity(intent);
                break;
        }
    }

    private void syncCalendar() {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(date);
            long startDate = date1.getTime();
            addToCalendar(getActivity(), "shift Event", startDate, startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        StringTokenizer tokenizer = new StringTokenizer(timeSlot,"-");
        String startTime = tokenizer.nextToken().toUpperCase();
        String endTme = tokenizer.nextToken().toUpperCase();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(date);
            long startDate = date1.getTime();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date stestDate = sdf1.parse(date+" "+startTime);
            Date eTestDate = sdf1.parse(date+" "+endTme);
            Log.d("testDate: ",stestDate.toString()+" : "+eTestDate.toString());

            Calendar beginTime = Calendar.getInstance();
            beginTime.setTime(stestDate);
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(eTestDate);
            //addToCalendar(getActivity(), "shift Event", startDate, startDate);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, role+" shift - "+location)
                    .putExtra(CalendarContract.Events.ALLOWED_REMINDERS,CalendarContract.Reminders.METHOD_ALERT)
                    .putExtra(CalendarContract.Events.HAS_ALARM,1)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_TENTATIVE);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertCalendarEntry(long startDate,long endDate) {

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, "Zira shift");
        values.put(CalendarContract.Events.DTSTART, startDate);
        values.put(CalendarContract.Events.DTEND, endDate);
        /*values.put(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTimeInMillis());
        values.put(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTimeInMillis());*/
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        // default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.HAS_ALARM, 1);

        // insert event to calendar
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        ContentValues reminderContentvalues = new ContentValues();
        reminderContentvalues.put(CalendarContract.Reminders.MINUTES, 30 * 24 * 60);
        reminderContentvalues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderContentvalues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderContentvalues);

        Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
        startActivity(LaunchIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

        }
    }

    private void addToCalendar(Context ctx, final String title, final long dtstart, final long dtend) {
        final ContentResolver cr = ctx.getContentResolver();
        Cursor cursor;
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
            cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), new String[]{"_id", "name"}, null, null, null);
        else
            cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{"_id", "name"}, null, null, null);

        if (cursor.moveToFirst()) {
            final String[] calNames = new String[cursor.getCount()];
            final int[] calIds = new int[cursor.getCount()];
            for (int i = 0; i < calNames.length; i++) {
                calIds[i] = cursor.getInt(0);
                calNames[i] = cursor.getString(1);
                cursor.moveToNext();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setSingleChoiceItems(calNames, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues cv = new ContentValues();
                    cv.put("calendar_id", calIds[which]);
                    cv.put("title", title);
                    cv.put("dtstart", dtstart);
                    cv.put("hasAlarm", 1);
                    cv.put("dtend", dtend);
                    cv.put("eventTimezone", TimeZone.getDefault().getID());

                    Uri newEvent;
                    if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                        newEvent = cr.insert(Uri.parse("content://com.android.calendar/events"), cv);
                    else
                        newEvent = cr.insert(Uri.parse("content://calendar/events"), cv);

                    if (newEvent != null) {
                        long id = Long.parseLong(newEvent.getLastPathSegment());
                        ContentValues values = new ContentValues();
                        values.put("event_id", id);
                        values.put("method", 1);
                        values.put("minutes", 15); // 15 minutes
                        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                            cr.insert(Uri.parse("content://com.android.calendar/reminders"), values);
                        else
                            cr.insert(Uri.parse("content://calendar/reminders"), values);
                    }
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
        cursor.close();

        Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
        startActivity(LaunchIntent);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            String[] separated = locationAddress.split("\n");
            String lat = separated[0];
            String lng = separated[1];

            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private Dialog dialog;

    private void openDialog(String heading, String Description) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        TextView tvGoBack = dialog.findViewById(R.id.tvGoBack);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        tvHeading.setText(heading);
        tvDescription.setText(Description);
        tvGoBack.setText(getResources().getString(R.string.cancel));
        btnYes.setText(getResources().getString(R.string.release));

        tvHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvDescription.setTypeface(AppTypeface.avenieNext_medium);
        tvGoBack.setTypeface(AppTypeface.avenieNext_demibold);
        btnYes.setTypeface(AppTypeface.avenieNext_demibold);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (NetworkUtils.isNetworkConnected(getActivity()))
                    releaseShift();
            }
        });

        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.GONE);
    }

    private void releaseShift() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {
            ReleaseShiftRequest shiftCheckInRequest = new ReleaseShiftRequest();
            shiftCheckInRequest.setShiftId(shift_id);
            shiftCheckInRequest.setRelease(release);

            Call<JsonElement> call = api.releaseShiftApi("Bearer " + session.getUserToken(),
                    shiftCheckInRequest);
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

                            CommonUtils.showSnackbar(tvClose, message);

                            if (code.equalsIgnoreCase("200")) {

                                if (release.equalsIgnoreCase("1")) {
                                    btnReleaseShift.setText(getContext().getString(R.string.undo_release));
                                    btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
                                } else {
                                    btnReleaseShift.setText(getContext().getString(R.string.release_shift_caps));
                                    btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.black_btn));
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvClose, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvClose, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }



    private void getShiftDetail() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            ShiftCheckInRequest shiftCheckInRequest = new ShiftCheckInRequest();
            shiftCheckInRequest.setShiftId(shift_id);

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
                               // release = String.valueOf(shiftDetailResponse.getResponse().getData().getRelease());


                                 updateView(shiftDetailResponse.getResponse().getData());



                            } else {
                                CommonUtils.showSnackbar(tvClose, shiftDetailResponse.getResponse().getMessage());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvClose, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShiftDetailResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvClose, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }


    private  void updateView(Data response){



        if(response.getShift_status().equalsIgnoreCase("NOSHOW")){
            tvEarningAmount.setText("$0.00");
        }else
        tvEarningAmount.setText(/*"$" +*/ response.getExpectedEarning());
        tvLocationDetail.setText(response.getStore_name());
        tvRoleDetail.setText(response.getRole());
        nextShiftId = session.getNextShift();
        autoCheckin = response.getAuto_checkin();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(response.getDate());
            String day_no = (String) DateFormat.format("dd", date1); // 20
            String monthString = (String) DateFormat.format("MMM", date1); // Jun
            day = (String) DateFormat.format("EEE", date1); // Thursday

            tvDay.setText(monthString + " " + day_no + ", " + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {

            Date currentDate = new Date();
            String pattern = "yyyy-MM-dd hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            StringTokenizer tokenizer = new StringTokenizer(response.getTimeSlot(), "-");
            String startTime = tokenizer.nextToken();
            String endTime = tokenizer.nextToken();

            tvStartTime.setText(startTime);
            tvEndTime.setText(endTime);

            String mDateString = response.getDate() + " " + startTime.toUpperCase();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 1);
            Date nxtDate = cal.getTime();
            String nextDateString = simpleDateFormat.format(nxtDate);
            Date nextDate = simpleDateFormat.parse(nextDateString);
            Date nextShiftDate = simpleDateFormat.parse(mDateString);

            if (nextShiftDate.compareTo(nextDate) < 0) {
                isWithin24hrs = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        session.setDialogData(from, response.getShiftId(), response.getDate(), response.getDay(),
                response.getExpectedEarning(), response.getTimeSlot(), response.getStore_name(), response.getRole());

        if (isWithin24hrs) {
            btnReleaseShift.setVisibility(View.GONE);
        } else {
            btnReleaseShift.setText("RELEASE SHIFT");
            btnReleaseShift.setVisibility(View.VISIBLE);
        }


        if ((from.equalsIgnoreCase("home")
             || from.equalsIgnoreCase("upcoming")||from.equalsIgnoreCase("calendarWeek")
        ) && isWithin24hrs && response.getShiftId().equalsIgnoreCase(nextShiftId)) {

            btnReleaseShift.setVisibility(View.VISIBLE);
            btnReleaseShift.setText(getResources().getString(R.string.check_in));
            btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
            textviewshiftType.setText("Next Shift");

        } else if (from.equalsIgnoreCase("calendar")) {
            btnReleaseShift.setVisibility(View.GONE);
            btnCallManager.setVisibility(View.GONE);
        } else {

            if(response.getRelease()==0 && isWithin24hrs){
                btnReleaseShift.setVisibility(View.GONE);
            }
            else if (response.getRelease()==0) {

                btnReleaseShift.setText("RELEASE SHIFT");
                btnReleaseShift.setVisibility(View.VISIBLE);
            } else if (response.getRelease()==1) {

                btnReleaseShift.setText("UNDO RELEASE");
                btnReleaseShift.setVisibility(View.VISIBLE);
                btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
            }else{
                btnReleaseShift.setText(getResources().getString(R.string.check_in));
                btnReleaseShift.setVisibility(View.VISIBLE);
                btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
        if(btnReleaseShift.getText().toString().equalsIgnoreCase(getResources().getString(R.string.check_in))
                && autoCheckin){
            btnReleaseShift.setVisibility(View.GONE);
        }
        /*if (response.getShiftId().equalsIgnoreCase(nextShiftId)) {

            btnReleaseShift.setVisibility(View.VISIBLE);
            textviewshiftType.setText("Next Shift");
            btnReleaseShift.setText(getResources().getString(R.string.check_in));
            btnReleaseShift.setBackgroundColor(getResources().getColor(R.color.blue));
        }*/
        switch (response.getShift_status()) {

            case "NOSHOW":
                textviewshiftType.setText("No Show");
                btnReleaseShift.setVisibility(View.GONE);
                rlDialog.setBackgroundColor(getResources().getColor(R.color.now_show_bg));
                lay01.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay02.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay03.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                lay04.setBackgroundColor(getResources().getColor(R.color.now_show_bg_dark));
                break;

            case "COMPLETED":
                textviewshiftType.setText("Completed Shift");
                break;
            case "UPCOMING":
                if(from.equalsIgnoreCase("home")){
                    textviewshiftType.setText("Next Shift");
                }else
                textviewshiftType.setText("Upcoming Shift");
                break;
        }

    }


    /*private void completeCheckIn(String pinValue, final String shift_id) {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {
            VerifyCheckInRequest verifyCheckInRequest = new VerifyCheckInRequest();
            verifyCheckInRequest.setPin(pinValue);
            verifyCheckInRequest.setShiftId(shift_id);

            Call<JsonElement> call = api.checkInShiftApi("Bearer " + session.getUserToken(),
                    verifyCheckInRequest);
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

                            CommonUtils.showSnackbar(tvClose, message);

                            if (code.equalsIgnoreCase("200")) {
                                Fragment fragment = HomeFragment.newInstance("", "");
                                fragmentInterface.fragmentResult(fragment,"");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvClose, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvClose, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }*/

}

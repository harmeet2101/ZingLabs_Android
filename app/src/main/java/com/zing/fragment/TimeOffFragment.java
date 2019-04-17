package com.zing.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.model.request.LeaveRequest;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeOffFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.tvTimeOffPeriod)
    TextView tvTimeOffPeriod;
    @BindView(R.id.llHeading)
    LinearLayout llHeading;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.etStartDate)
    EditText etStartDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.etEndDate)
    EditText etEndDate;
    @BindView(R.id.btnRequest)
    Button btnRequest;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    Unbinder unbinder;
    private Calendar calendar;

    private ProgressDialog progressDialog;
    SessionManagement session;
    private String mParam1, startDate = "", endDate = "";
    private String mParam2;

    public static TimeOffFragment newInstance(String param1, String param2) {
        TimeOffFragment fragment = new TimeOffFragment();
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
        View view = inflater.inflate(R.layout.fragment_time_off, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((BottomNavigationView) getActivity().findViewById(R.id.btmNavigation)).setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());
        tvSelect.setTypeface(AppTypeface.avenieNext_demibold);
        tvTimeOffPeriod.setTypeface(AppTypeface.avenieNext_demibold);
        tvStartDate.setTypeface(AppTypeface.avenieNext_demibold);
        tvEndDate.setTypeface(AppTypeface.avenieNext_demibold);

        etStartDate.setTypeface(AppTypeface.avenieNext_regular);
        etEndDate.setTypeface(AppTypeface.avenieNext_regular);
        btnRequest.setTypeface(AppTypeface.avenieNext_demibold);
        tvCancel.setTypeface(AppTypeface.avenieNext_medium);

        calendar = Calendar.getInstance();
        etStartDate.setCursorVisible(false);
        etStartDate.setFocusableInTouchMode(false);

        etEndDate.setCursorVisible(false);
        etEndDate.setFocusableInTouchMode(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                String myFormat = "yyyy-MM-dd";
                String secondFormat = "dd-MMM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                SimpleDateFormat sdfFormat = new SimpleDateFormat(secondFormat, Locale.US);
                etStartDate.setText(sdfFormat.format(calendar.getTime()));
                startDate = sdf.format(calendar.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener Enddate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                String myFormat = "yyyy-MM-dd";
                String secondFormat = "dd-MMM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                SimpleDateFormat sdfFormat = new SimpleDateFormat(secondFormat, Locale.US);
                etEndDate.setText(sdfFormat.format(calendar.getTime()));
                endDate = sdf.format(calendar.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Fragment fragment;

    @OnClick({R.id.ivBack, R.id.etStartDate, R.id.etEndDate, R.id.btnRequest, R.id.tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
//                getActivity().onBackPressed();
                fragment = AccountFragment.newInstance("", "");
                fragmentInterface.fragmentResult(fragment, "");
                break;
            case R.id.etStartDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                long now = System.currentTimeMillis() - 1000;
                datePickerDialog.getDatePicker().setMinDate(now);
                datePickerDialog.show();
                break;
            case R.id.etEndDate:
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(),
                        Enddate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
//                long now1 = System.currentTimeMillis() - 1000;

                Date d = null;
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                try {
                    d = sdf.parse(startDate);
                    long milliseconds = d.getTime();
                    datePickerDialog1.getDatePicker().setMinDate(milliseconds);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                datePickerDialog1.show();
                break;
            case R.id.btnRequest:
                if (startDate.length() < 1) {
                    CommonUtils.showSnackbar(btnRequest, getContext().getString(R.string.validate_emptyField));
                } else if (endDate.length() < 1) {
                    CommonUtils.showSnackbar(btnRequest, getContext().getString(R.string.validate_emptyField));
                } else {
                    if (NetworkUtils.isNetworkConnected(getActivity()))
                        sendLeaveRequest();
                }
                break;
            case R.id.tvCancel:
//                getActivity().onBackPressed();
                fragment = AccountFragment.newInstance("", "");
                fragmentInterface.fragmentResult(fragment, "");
                break;
        }
    }

    private void sendLeaveRequest() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setStartDate(startDate);
            leaveRequest.setEndDate(endDate);

            Call<JsonElement> call = api.sendLeaveRequestApi("Bearer " + session.getUserToken(),
                    leaveRequest);
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

                            if (code.equalsIgnoreCase("200")) {
                                CommonUtils.showSnackbar(tvTimeOffPeriod, message);
                                etStartDate.setText("");
                                etEndDate.setText("");
                                Fragment fragment = AccountFragment.newInstance("", "");
                                fragmentInterface.fragmentResult(fragment, "");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvTimeOffPeriod, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvTimeOffPeriod, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}

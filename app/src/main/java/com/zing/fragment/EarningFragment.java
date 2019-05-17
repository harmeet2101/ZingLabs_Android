package com.zing.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.adapter.CompletedShiftAdapter;
import com.zing.adapter.PaystubAdapter;
import com.zing.model.request.BankTransferRequest;
import com.zing.model.request.UpcomingShiftRequest;
import com.zing.model.response.CalendarScheduledShiftResponse.CalendarScheduledShiftResponse;
import com.zing.model.response.CalendarSlotResponse.RecommendedShift;
import com.zing.model.response.PreviousPaystubResponse.PreviousPaystub;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.vpGraph)
    FrameLayout vpGraph;
    @BindView(R.id.tlGraph)
    TabLayout tlGraph;
    @BindView(R.id.tvCurrentWallet)
    TextView tvCurrentWallet;
    @BindView(R.id.tvEarningDescription)
    TextView tvEarningDescription;
    @BindView(R.id.tvCurrentEarning)
    TextView tvCurrentEarning;
    @BindView(R.id.btnBankTransfer)
    Button btnBankTransfer;
    @BindView(R.id.tvNote)
    TextView tvNote;
    @BindView(R.id.tvSetUpPay)
    TextView tvSetUpPay;
    @BindView(R.id.cvCurrentWallet)
    CardView cvCurrentWallet;
    @BindView(R.id.tvPreviousPaystubs)
    TextView tvPreviousPaystubs;
    @BindView(R.id.rvPreviousPaystubs)
    RecyclerView rvPreviousPaystubs;
    Unbinder unbinder;
    @BindView(R.id.tvSetPayHeading)
    TextView tvSetPayHeading;
    @BindView(R.id.tvSetPayDescription)
    TextView tvSetPayDescription;
    @BindView(R.id.cvSetUpPay)
    CardView cvSetUpPay;
    @BindView(R.id.cvPreviousPaystub)
    CardView cvPreviousPaystub;
    @BindView(R.id.tvInstantPay)
    TextView tvInstantPay;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvEarning)
    TextView tvEarning;



    @BindView(R.id.tvCompletedShift)
    TextView tvCompletedShift;
    @BindView(R.id.rvCompletedShift)
    RecyclerView rvCompletedShift;
    @BindView(R.id.cvCompletedShiftList)
    CardView cvCompletedShiftList;
    private CompletedShiftAdapter completedAdapter;
    private ProgressDialog progressDialog;
    SessionManagement session;
    ArrayList<PreviousPaystub> paystubList;
    private PaystubAdapter paystubAdapter;

    private String mParam1;
    private String mParam2;

    private String startWeek = "", endWeek = "",typeSelection;
    private String startDate="",endDate="";
    public static EarningFragment newInstance(String param1, String param2) {
        EarningFragment fragment = new EarningFragment();
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
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);

        Calendar c1 = Calendar.getInstance();

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        startWeek = getMonth(month1);
        startDate = year1+"-"+month1+"-"+day1;
        //Log.d("start",year1+"-"+month1+"-"+day1);
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH) + 1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);
        endWeek = getMonth(month7);
        endDate = year7+"-"+month7+"-"+day7;
        //Log.d("end",year7+"-"+month7+"-"+day7);
        tvMonth.setText(startWeek + " " + day1 + " - " + endWeek + " " + day7);
        if (NetworkUtils.isNetworkConnected(getActivity())){
            getPreviousPaystub();
            fetchCalenderDetails(startDate,endDate);
        }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_earning, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        session = new SessionManagement(getActivity());
        paystubList = new ArrayList<>();

        tvCurrentWallet.setTypeface(AppTypeface.avenieNext_demibold);
        tvCurrentEarning.setTypeface(AppTypeface.avenieNext_regular);
        tvSetUpPay.setTypeface(AppTypeface.avenieNext_demibold);
        tvPreviousPaystubs.setTypeface(AppTypeface.avenieNext_demibold);
        tvInstantPay.setTypeface(AppTypeface.avenieNext_demibold);
        tvMonth.setTypeface(AppTypeface.avenieNext_medium);
        tvEarning.setTypeface(AppTypeface.avenieNext_demibold);
//      toolbar.setVisibility(View.INVISIBLE);

        tvEarningDescription.setTypeface(AppTypeface.avenieNext_regular);
        btnBankTransfer.setTypeface(AppTypeface.avenieNext_medium);
        tvNote.setTypeface(AppTypeface.avenieNext_regular);
        tvSetPayHeading.setTypeface(AppTypeface.avenieNext_medium);
        tvSetPayDescription.setTypeface(AppTypeface.avenieNext_regular);


        tvCompletedShift.setTypeface(AppTypeface.avenieNext_demibold);

      /*  AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
//                    showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
//                    hideOption(R.id.action_info);
                }
            }
        });*/

        //create tabs title
        tlGraph.addTab(tlGraph.newTab().setText("Week"));
        tlGraph.addTab(tlGraph.newTab().setText("Month"));
        tlGraph.addTab(tlGraph.newTab().setText("Quarter"));

        //tlGraph.setupWithViewPager(vpGraph);
       /* GraphAdapter adapter = new GraphAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new WeekFragment(), "Week");
        adapter.addFragment(new MonthFragment(), "Month");
        adapter.addFragment(new QuarterFragment(), "Quarter");*/


        addFragment(new WeekFragment(), "Week");

        //handling tab click event
        tlGraph.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        addFragment(new WeekFragment(), "Week");
                        fetchCalenderDetails(startDate,endDate);
                        break;
                    case 1:
                        addFragment(new MonthFragment(), "Month");
                        Calendar c = Calendar.getInstance();
                        String start = c.get(Calendar.YEAR)+"-" +
                                (c.get(Calendar.MONTH)+1)+
                                "-" +
                                c.getActualMinimum(Calendar.DAY_OF_MONTH);

                        String end = c.get(Calendar.YEAR)+"-" +
                                (c.get(Calendar.MONTH)+1)+
                                "-" +
                                c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        fetchCalenderDetails(start,end);
                        break;
                    case 2:
                        addFragment(new NewQuarterFragment(), "Quarter");
                        Calendar c1 = Calendar.getInstance();
                        String[] qrtr = CommonUtils.getQuarter();

                        int sMonth = CommonUtils.getMonthInInt(qrtr[0]);
                        int eMonth = CommonUtils.getMonthInInt(qrtr[1]);
                        String start1 = c1.get(Calendar.YEAR)+"-" +
                                (sMonth+1)+
                                "-" +
                                c1.getActualMinimum(Calendar.DAY_OF_MONTH);

                        String end1= c1.get(Calendar.YEAR)+"-" +
                                (eMonth+1)+
                                "-" +
                                c1.getActualMaximum(Calendar.DAY_OF_MONTH);
                        fetchCalenderDetails(start1,end1);
                        break;
                }
               /* if (tab.getPosition() == 0) {
                    addFragment( new WeekFragment(), "Week" );

                } else if (tab.getPosition() == 1) {
                    addFragment( new MonthFragment(), "Month" );

                } else {
                    addFragment( new QuarterFragment(), "Quarter");

                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //   vpGraph.setAdapter(adapter);


        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvPreviousPaystubs.setLayoutManager(lManager);
        paystubAdapter = new PaystubAdapter(getActivity(), fragmentInterface, paystubList);
        rvPreviousPaystubs.setAdapter(paystubAdapter);
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

    private void addFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.vpGraph, fragment, title);
        fragmentTransaction.commit();
    }

    private void getPreviousPaystub() {

//        progressDialog = CommonUtils.getProgressBar( getActivity() );
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<JsonElement> call = api.previousPaystubApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
//                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject responseObj = jsonObject.optJSONObject("response");
                            String code = responseObj.optString("code");
                            String message = responseObj.optString("message");
                            String upcoming_payment = responseObj.optString("upcoming_payment");
                            String bank_detail = responseObj.optString("bank_detail");

                          /*  if (bank_detail.equalsIgnoreCase("0")) {
                                cvSetUpPay.setVisibility(View.VISIBLE);
                            } else {
                                cvSetUpPay.setVisibility(View.GONE);
                            }*/

                            paystubList.clear();
                            JSONArray previous_paystubsArr = responseObj.optJSONArray("previous_paystubs");
                            for (int i = 0; i < previous_paystubsArr.length(); i++) {
                                JSONObject previous_paystubsObj = previous_paystubsArr.optJSONObject(i);

                                String earnings = previous_paystubsObj.optString("earnings");
                                String start_date = previous_paystubsObj.optString("start_date");
                                String end_date = previous_paystubsObj.optString("end_date");
                                String paystub_payment_id = previous_paystubsObj.optString("payment_id");
                                String status = previous_paystubsObj.optString("status");

                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                Date newDate = spf.parse(start_date);
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                                start_date = sdf.format(newDate);

                                Date newEndDate = spf.parse(end_date);
                                SimpleDateFormat df = new SimpleDateFormat("MMM dd");
                                end_date = df.format(newEndDate);

                                PreviousPaystub previousPaystub = new PreviousPaystub();
                                previousPaystub.setEarnings("$" + earnings);
                                previousPaystub.setStartDate(start_date);
                                previousPaystub.setEndDate(end_date);
                                previousPaystub.setPaymentId(paystub_payment_id);
                                previousPaystub.setStatus(status);
                                previousPaystub.setSlot(start_date + " to " + end_date);

                                paystubList.add(previousPaystub);
                            }

                            if (paystubList.size() > 0) {
                                cvPreviousPaystub.setVisibility(View.VISIBLE);
                            } else {
                                cvPreviousPaystub.setVisibility(View.GONE);
                            }

                            tvCurrentEarning.setText("$" + upcoming_payment);
                            paystubAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvCurrentEarning, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvCurrentEarning, t.getMessage());
                }
            });
        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bankTransfer() {
        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);

        try {

            BankTransferRequest bankTransferRequest = new BankTransferRequest();
            bankTransferRequest.setAmount(tvCurrentEarning.getText().toString().
                    replace("$", "").replace(",", ""));

            Call<JsonElement> call = api.bankTransferApi("Bearer " + session.getUserToken(),
                    bankTransferRequest);
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

                            CommonUtils.showSnackbar(btnBankTransfer, message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(btnBankTransfer, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(btnBankTransfer, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnBankTransfer, R.id.tvInstantPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBankTransfer:
                if (NetworkUtils.isNetworkConnected(getActivity()))
                bankTransfer();
                break;
            case R.id.tvInstantPay:
                Fragment fragment = SetPaymentDetailsFragment.newInstance("", "",
                        "", "", "earning");
                fragmentInterface.fragmentResult(fragment, "+");
                break;
        }
    }


    ArrayList<RecommendedShift> completedShiftList = new ArrayList<>();


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


                                completedShiftList.clear();
                                //llLayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < calendarSheduledShiftResponse.getResponse().
                                        getCompletedShiftList().size(); i++) {
                                    completedShiftList.add(calendarSheduledShiftResponse.getResponse().
                                            getCompletedShiftList().get(i));
                                }

                                if (completedShiftList.size() != 0) {
                                    tvCompletedShift.setText("Completed Shift");
                                    cvCompletedShiftList.setVisibility(View.VISIBLE);
                                } else {
                                    cvCompletedShiftList.setVisibility(View.VISIBLE);
                                    tvCompletedShift.setText("No Completed Shifts Available");
                                }


                            }
                            else {
                                Toast.makeText(getContext(),calendarSheduledShiftResponse.getResponse().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {

                            completedAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CalendarScheduledShiftResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}

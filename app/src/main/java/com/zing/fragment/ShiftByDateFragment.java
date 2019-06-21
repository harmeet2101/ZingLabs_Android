package com.zing.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.adapter.ShiftCalendarAdapter;
import com.zing.model.request.ShiftDetailByDateRequest;
import com.zing.model.response.shiftbydate.Shift;
import com.zing.model.response.shiftbydate.ShiftByDateBaseModel;
import com.zing.util.CommonUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShiftByDateFragment extends BaseFragment {

    @BindView(R.id.rv_shift_view)
    RecyclerView rvShiftView;
    Unbinder unbinder;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    private ProgressDialog progressDialog;
    private SessionManagement session;
    private Activity mActivity;
    private List<Shift> shiftsList;
    private String date;
    ShiftCalendarAdapter recomendedAdapter;
    public ShiftByDateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift_by_date, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        getBundleData();

        return view;
    }

    /*
      Getting bundle data
     */
    private void getBundleData() {
         date = getArguments().getString("date");
        getShiftDetailByDate(date);
    }

    private void initViews() {
        mActivity = getActivity();
        session = new SessionManagement(mActivity);
    }

    private void settingUpAdapter() {
        rvShiftView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recomendedAdapter = new ShiftCalendarAdapter(mActivity, shiftsList,fragmentInterface,ShiftByDateFragment.this);
        rvShiftView.setAdapter(recomendedAdapter);
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
                                shiftsList= shiftDetailResponse.getResponse().getShifts();
                                settingUpAdapter();
                            } else {
                                CommonUtils.showSnackbar(rlMain, shiftDetailResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(rlMain, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShiftByDateBaseModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(rlMain, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

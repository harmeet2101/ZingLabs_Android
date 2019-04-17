package com.zing.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zing.R;
import com.zing.adapter.NotificationAdapter;
import com.zing.model.response.BroadcastResponse.BroadcastList;
import com.zing.model.response.BroadcastResponse.BroadcastResponse;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BroadcastFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvBroadcast)
    TextView tvBroadcast;
    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ArrayList<BroadcastList> broadcastLists;
    private ProgressDialog progressDialog;
    SessionManagement session;
    private NotificationAdapter myAdapter;

    public static BroadcastFragment newInstance(String param1, String param2) {
        BroadcastFragment fragment = new BroadcastFragment();
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
        View view = inflater.inflate(R.layout.fragment_broadcast, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvBroadcast.setTypeface(AppTypeface.avenieNext_demibold);
        broadcastLists = new ArrayList<>();
        broadcastLists.clear();
        session = new SessionManagement(getActivity());

        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        rvNotification.setLayoutManager(lManager);
        myAdapter = new NotificationAdapter(getActivity(), fragmentInterface, broadcastLists);
        rvNotification.setAdapter(myAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(getActivity()))
        getNotificationList();
    }
    private void getNotificationList() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<BroadcastResponse> call = api.broadcastApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<BroadcastResponse>() {
                @Override
                public void onResponse(@NonNull Call<BroadcastResponse> call,
                                       @NonNull Response<BroadcastResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {

                            BroadcastResponse broadcastResponse = response.body();
                            if (broadcastResponse != null && broadcastResponse.getResponse().getCode().equalsIgnoreCase("200")) {
                                broadcastLists.clear();
                                for (int i = 0; i < broadcastResponse.getResponse().getBroadcastList().size(); i++) {
                                    BroadcastList broadcastList = new BroadcastList();
                                    broadcastList.setDescription(broadcastResponse.getResponse().getBroadcastList().get(i).getDescription());
                                    broadcastList.setImageUrl(broadcastResponse.getResponse().getBroadcastList().get(i).getImageUrl());
                                    broadcastList.setHeading(broadcastResponse.getResponse().getBroadcastList().get(i).getHeading());
                                    broadcastList.setTime(broadcastResponse.getResponse().getBroadcastList().get(i).getTime());
                                    broadcastList.setOtherLink(broadcastResponse.getResponse().getBroadcastList().get(i).getOtherLink());
                                    broadcastList.setType(broadcastResponse.getResponse().getBroadcastList().get(i).getType());
                                    broadcastList.setIsMsgPersonal(broadcastResponse.getResponse().getBroadcastList().get(i).getIsMsgPersonal());
                                    broadcastLists.add(broadcastList);
                                }

                                myAdapter.notifyDataSetChanged();
                            } else {
                                CommonUtils.showSnackbar(tvBroadcast, broadcastResponse.getResponse().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvBroadcast, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BroadcastResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvBroadcast, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
       /* } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}

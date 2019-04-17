package com.zing.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.adapter.BadgesAdapter;
import com.zing.adapter.GridBadgesAdapter;
import com.zing.model.response.StatsResponse.Badge;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.NetworkUtils;
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

public class BadgesFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvBadges)
    TextView tvBadges;
    @BindView(R.id.rvBadges)
    RecyclerView rvBadges;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    SessionManagement session;
    ArrayList<Badge> badgesList;
    private GridBadgesAdapter myAdapter;

    public static BadgesFragment newInstance(String param1, String param2) {
        BadgesFragment fragment = new BadgesFragment();
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
        View view = inflater.inflate(R.layout.fragment_badges, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvBadges.setTypeface(AppTypeface.avenieNext_demibold);
        session = new SessionManagement(getActivity());
        badgesList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvBadges.setHasFixedSize(true);
        rvBadges.setLayoutManager(gridLayoutManager);
         myAdapter = new GridBadgesAdapter(getActivity(), badgesList);
        rvBadges.setAdapter(myAdapter);

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
        statsDetails();
    }

    private void statsDetails() {

        progressDialog = CommonUtils.getProgressBar(getActivity());
        ZinglabsApi api = ApiClient.getClient().create(ZinglabsApi.class);
        try {

            Call<JsonElement> call = api.statsDetailApi("Bearer " + session.getUserToken());
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call,
                                       @NonNull Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject responseObject = new JSONObject(response.body().toString());
                            JSONObject jsonObject = responseObject.optJSONObject("response");
                            String code = jsonObject.optString("code");
                            String message = jsonObject.optString("message");
                            String rate = jsonObject.optString("rate");

                            CommonUtils.showSnackbar(tvBadges, message);
                            badgesList.clear();
                            JSONArray badgeArr = jsonObject.optJSONArray("badges");
                            for (int i = 0; i < badgeArr.length(); i++) {
                                JSONObject badgesObj = badgeArr.optJSONObject(i);
                                Badge badge = new Badge();
                                badge.setBadgeId(badgesObj.optString("badge_id"));
                                badge.setBadgeImgUrl(badgesObj.optString("badge_img_url"));
                                badge.setBadgeStatus(badgesObj.optString("badge_status"));
                                badge.setDescription(badgesObj.optString("description"));
                                badge.setName(badgesObj.optString("name"));

                                badgesList.add(badge);
                            }

                            myAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvBadges, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvBadges, t.getMessage());
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }
}

package com.zing.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.zing.R;
import com.zing.adapter.OtpAdapter;
import com.zing.model.request.VerifyCheckInRequest;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvEnterPin)
    TextView tvEnterPin;
    @BindView(R.id.tvPin1)
    TextView tvPin1;
    @BindView(R.id.tvPin2)
    TextView tvPin2;
    @BindView(R.id.tvPin3)
    TextView tvPin3;
    @BindView(R.id.tvPin4)
    TextView tvPin4;
    @BindView(R.id.tvPin5)
    TextView tvPin5;
    @BindView(R.id.gvKeyPad)
    GridView gvKeyPad;
    Unbinder unbinder;

    int[] KeyId = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 11};
    ArrayList<Integer> pin = new ArrayList<>();
    String pinValue = "";
    int position = 0;
    @BindView(R.id.tvCheckinClose)
    TextView tvCheckinClose;

    private String shift_id;
    private String mParam2;
    boolean minZero, maxSize;
    private ProgressDialog progressDialog;
    SessionManagement session;

    public static VerifyOtpFragment newInstance(String param1, String param2) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
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
            shift_id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
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
        tvEnterPin.setTypeface(AppTypeface.avenieNext_regular);
        tvCheckinClose.setTypeface(AppTypeface.avenieNext_demibold);

        gvKeyPad.setVerticalScrollBarEnabled(false);
        gvKeyPad.setAdapter(new OtpAdapter(getActivity(), KeyId, VerifyOtpFragment.this));
    }

    public void setValue(int position) {
        this.position = position;

        if (position != 11) {
            if (position == 9) {
                if (pin.size() > 0) {
                    int pos = pin.size();
                    pin.remove((pin.size() - 1));
                    setSelection(pos, false);
                }
            } else {
                if (pin.size() < 5) {
                    pin.add(KeyId[position]);
                    //Log.d("pin",""+(KeyId[position]));
                    int pos = pin.size();
                    setSelection(pos, true);
                }
            }
        } else {
            getPin();
        }
    }

    private void setSelection(int pos, boolean isAdd) {
        if (pin.size() > 0) {
            minZero = true;
            maxSize = pin.size() == 5;
        } else {
            minZero = false;
        }

        switch (pos) {
            case 1:
                setText(tvPin1, isAdd);
                break;
            case 2:
                setText(tvPin2, isAdd);
                break;
            case 3:
                setText(tvPin3, isAdd);
                break;
            case 4:
                setText(tvPin4, isAdd);
                break;
            case 5:
                setText(tvPin5, isAdd);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setText(TextView textView, boolean isAdd) {
        if (isAdd) {
           // textView.setText("" + KeyId[this.position]);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.blue_circle_otp);
        } else {
            //textView.setText("");
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    private void getPin() {
        pinValue = "";
        if (pin != null) {
            if (pin.size() == 5 && pin.size() > 0) {
                for (Integer integer : pin) {
                    pinValue = pinValue + integer;
                }
                completeCheckIn(pinValue, shift_id);
                Toast.makeText(getActivity(), pinValue, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please enter Valid pin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void completeCheckIn(String pinValue, final String shift_id) {

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

                            CommonUtils.showSnackbar(tvCheckinClose, message);

                            if (code.equalsIgnoreCase("200")) {
                                Fragment fragment = HomeFragment.newInstance("", "");
                               fragmentInterface.fragmentResult(fragment,"");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar(tvCheckinClose, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar(tvCheckinClose, t.getMessage());
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

    public boolean getMin() {
        return minZero;
    }

    public boolean getMax() {
        return maxSize;
    }

    @OnClick(R.id.tvCheckinClose)
    public void onViewClicked() {
        if (mParam2.equalsIgnoreCase("home")) {
            Fragment fragment = HomeFragment.newInstance("", "");
            fragmentInterface.fragmentResult(fragment, "");
        } else {
            Fragment fragment = CalenderFragment.newInstance("", "");
            fragmentInterface.fragmentResult(fragment, "");
        }
    }
}

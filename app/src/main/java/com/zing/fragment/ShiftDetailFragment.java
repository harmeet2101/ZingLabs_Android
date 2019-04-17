package com.zing.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.util.AppTypeface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShiftDetailFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tvShiftDetail)
    TextView tvShiftDetail;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvTxtStartTime)
    TextView tvTxtStartTime;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvTxtCheckIn)
    TextView tvTxtCheckIn;
    @BindView(R.id.tvCheckIn)
    TextView tvCheckIn;
    @BindView(R.id.tvClockedIn)
    TextView tvClockedIn;
    @BindView(R.id.tvClockedInTime)
    TextView tvClockedInTime;
    @BindView(R.id.tvBreak)
    TextView tvBreak;
    @BindView(R.id.tvBreakTime)
    TextView tvBreakTime;
    @BindView(R.id.tvTxtEndTime)
    TextView tvTxtEndTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvTxtCheckOut)
    TextView tvTxtCheckOut;
    @BindView(R.id.tvCheckOut)
    TextView tvCheckOut;
    Unbinder unbinder;
    @BindView(R.id.customSeekBar)
    CustomSeekBar customSeekBar;
    @BindView(R.id.tvRole)
    TextView tvRole;
    @BindView(R.id.tvEarning)
    TextView tvEarning;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.rlDialog)
    RelativeLayout rlDialog;
    @BindView(R.id.btnTakeBreak)
    Button btnTakeBreak;
    @BindView(R.id.btnCheckout)
    Button btnCheckout;
    @BindView(R.id.tvClose)
    TextView tvClose;

    private String mParam1;
    private String mParam2;
    int maxValue = 100;
    int curValue = 0;
    Handler handler;


    public static ShiftDetailFragment newInstance(String param1, String param2) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_shift_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        customSeekBar.setProgress(50);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());
        tvShiftDetail.setTypeface(AppTypeface.avenieNext_medium);
        tvTxtStartTime.setTypeface(AppTypeface.avenieNext_medium);
        tvTxtEndTime.setTypeface(AppTypeface.avenieNext_medium);
        tvTxtCheckOut.setTypeface(AppTypeface.avenieNext_medium);
        tvBreak.setTypeface(AppTypeface.avenieNext_medium);
        tvBreakTime.setTypeface(AppTypeface.avenieNext_medium);

        tvShiftDetail.setTypeface(AppTypeface.avenieNext_demibold);
        tvStartTime.setTypeface(AppTypeface.avenieNext_demibold);
        tvEndTime.setTypeface(AppTypeface.avenieNext_demibold);
        tvCheckOut.setTypeface(AppTypeface.avenieNext_demibold);
        tvDay.setTypeface(AppTypeface.avenieNext_demibold);
        tvCheckIn.setTypeface(AppTypeface.avenieNext_demibold);

        tvRole.setTypeface(AppTypeface.avenieNext_regular);
        tvEarning.setTypeface(AppTypeface.avenieNext_regular);
        tvLocation.setTypeface(AppTypeface.avenieNext_regular);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnTakeBreak, R.id.btnCheckout, R.id.tvClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnTakeBreak:
                break;
            case R.id.btnCheckout:
                break;
            case R.id.tvClose:
                getActivity().onBackPressed();
                break;
        }
    }
}

package com.zing.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zing.R;
import com.zing.adapter.GraphAdapter;
import com.zing.util.AppTypeface;
import com.zing.util.NonSwipeableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CalenderFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tvCalendar)
    TextView tvCalendar;
    @BindView(R.id.tvCalendarSelection)
    TextView tvCalendarSelection;

    @BindView(R.id.vpCalendar)
    NonSwipeableViewPager vpCalendar;
    Unbinder unbinder;
    private String mParam1;
    private String mParam2;
    private GraphAdapter graphAdapter;

    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
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
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppTypeface.getTypeFace(getActivity());

        graphAdapter = new GraphAdapter(getActivity().getSupportFragmentManager());
        graphAdapter.addFragment(CalendarWeekFragment.newInstance(mParam1,mParam2), "Week");
        graphAdapter.addFragment(new CalendarMonthFragment(), "Month");
        vpCalendar.setAdapter(graphAdapter);

        tvCalendar.setTypeface(AppTypeface.avenieNext_demibold);
        tvCalendarSelection.setTypeface(AppTypeface.avenieNext_medium);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvCalendarSelection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCalendarSelection:

                if (tvCalendarSelection.getText().toString().equalsIgnoreCase("Week")) {
                    vpCalendar.setCurrentItem(1, true);
                    tvCalendarSelection.setText("Month");
                } else {
                    vpCalendar.setCurrentItem(0, true);
                    tvCalendarSelection.setText("Week");
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById(R.id.rvFooter)).setVisibility(View.VISIBLE);
    }

}

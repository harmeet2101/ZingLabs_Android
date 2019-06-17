package com.zing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zing.interfaces.FragmentInterface;

/**
 * Created by savita on 6/4/18.
 */

public class BaseFragment extends Fragment {
    public FragmentInterface fragmentInterface;
    HomeFragment.IHomFragListner iHomFragListner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentInterface = (FragmentInterface) getActivity();
        iHomFragListner = (HomeFragment.IHomFragListner)getActivity();
    }

}

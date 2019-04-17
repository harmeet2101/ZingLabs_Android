package com.zing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zing.R;
import com.zing.adapter.ViewPagerAdapter;
import com.zing.util.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;

    CircleImageView ivProfileImg;
    @BindView(R.id.tlProfile)
    TabLayout tlProfile;
    //    @BindView(R.id.toolbar)
//    Toolbar toolbarId;
    @BindView(R.id.vpProfile)
    ViewPager vpProfile;

    SessionManagement session;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    private String mParam1;
    private String mParam2;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        ivProfileImg = view.findViewById(R.id.ivProfileImg);
//        ivProfileImg = toolbarId.findViewById(R.id.ivProfileImg);
        initCollapsingToolbar();
        return view;
    }

    private void initCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appbar.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    ivProfileImg.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
                    ivProfileImg.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManagement(getActivity());

        if (!session.getProfilePic().equalsIgnoreCase("")) {
            Log.e("session", session.getProfilePic());
            Glide.with(getActivity()).load(session.getProfilePic())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .dontTransform().placeholder(R.drawable.ic_user)).into(ivProfileImg);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new AccountFragment(), "Account");
        adapter.addFragment(new MyStatsFragment(), "My Stats ");
        vpProfile.setAdapter(adapter);
        tlProfile.setupWithViewPager(vpProfile);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.example.irene.khramovahomework6;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerFragment extends Fragment {
    @BindView(R.id.viewPager) WrapContentViewPager mViewPager;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;
    private static final int PAGE_COUNT = 3;

    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        PagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager, true);

        return rootView;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String text = getString(R.string.text_img, String.valueOf(position+1));
            switch (position) {
                case 0:
                    return PageFragment.newInstance(text, R.drawable.img_1);
                case 1:
                    return PageFragment.newInstance(text, R.drawable.img_2);
                case 2:
                    return PageFragment.newInstance(text, R.drawable.img_3);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}

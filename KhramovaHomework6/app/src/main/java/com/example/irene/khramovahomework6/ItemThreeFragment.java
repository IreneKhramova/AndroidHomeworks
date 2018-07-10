package com.example.irene.khramovahomework6;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemThreeFragment extends Fragment {
    @BindView(R.id.buttonShowBanner) Button mButtonShowBanner;
    private OnViewPagerFragmentListener mOnViewPagerFragmentListener;
    private boolean isBannerShown;

    public static ItemThreeFragment newInstance() {
        return new ItemThreeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewPagerFragmentListener) {
            mOnViewPagerFragmentListener = (OnViewPagerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewPagerFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_three, container, false);
        ButterKnife.bind(this, rootView);
        isBannerShown = false;

        mButtonShowBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBannerShown) {
                    isBannerShown = false;
                    mButtonShowBanner.setText(R.string.show_banner);
                    mOnViewPagerFragmentListener.hidePagerFragment();
                } else {
                    isBannerShown = true;
                    mButtonShowBanner.setText(R.string.hide_banner);
                    mOnViewPagerFragmentListener.showPagerFragment();
                }
            }
        });

        return rootView;
    }

    public interface OnViewPagerFragmentListener {
        void showPagerFragment();
        void hidePagerFragment();
    }
}

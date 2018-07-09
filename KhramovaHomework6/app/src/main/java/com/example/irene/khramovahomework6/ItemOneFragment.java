package com.example.irene.khramovahomework6;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemOneFragment extends Fragment {
    @BindView(R.id.textView) TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items_one_two, container, false);
        ButterKnife.bind(this, rootView);

        mTextView.setText(R.string.text_fragment_one);

        return rootView;
    }
}

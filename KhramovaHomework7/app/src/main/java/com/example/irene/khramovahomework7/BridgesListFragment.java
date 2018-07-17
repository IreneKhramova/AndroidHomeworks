package com.example.irene.khramovahomework7;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgesListFragment extends Fragment {
    public static final String TAG_LIST = "Bridges list";
    public static final String ARG_ON_CLICK = "onItemClick";
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private BridgeAdapter mBridgeAdapter;
    private BridgeAdapter.OnItemClick mOnItemClick;

    public static BridgesListFragment newInstance(BridgeAdapter.OnItemClick onItemClick) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ON_CLICK, onItemClick);
        BridgesListFragment fragment = new BridgesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bridges_list, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mOnItemClick = getArguments().getParcelable(ARG_ON_CLICK);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBridgeAdapter = new BridgeAdapter(mOnItemClick);
        mRecyclerView.setAdapter(mBridgeAdapter);

        return rootView;
    }

    public void setBridges(List<Bridge> bridges) {
        mBridgeAdapter.setBridges(bridges);
    }
}

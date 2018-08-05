package com.example.irene.khramovahomework7;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgeAdapter extends RecyclerView.Adapter<BridgeAdapter.ViewHolder> {
    private List<Bridge> mBridges = new ArrayList<>();
    private OnItemClick mOnItemClick;

    BridgeAdapter(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setBridges(List<Bridge> bridges) {
        mBridges.clear();
        mBridges.addAll(bridges);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_bridge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bridge item = mBridges.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(view -> mOnItemClick.onClick(item));
    }

    @Override
    public int getItemCount() {
        return mBridges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewBridge) ImageView mImageViewBridge;
        @BindView(R.id.textViewBridgeName) TextView mTextViewBridgeName;
        @BindView(R.id.textViewDivorceTime) TextView mTextViewDivorceTime;
        @BindView(R.id.imageViewBell) ImageView mImageViewBell;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Bridge bridge) {
            mTextViewBridgeName.setText(bridge.getName());
            mTextViewDivorceTime.setText(DivorceUtil.getDivorceTime(bridge));
            mImageViewBridge.setImageResource(DivorceUtil.getDivorceImgResId(bridge));
            //TODO:
            mImageViewBell.setImageResource(R.drawable.ic_kolocol_off);
        }
    }

    public interface OnItemClick {
        void onClick(Bridge bridge);
    }
}

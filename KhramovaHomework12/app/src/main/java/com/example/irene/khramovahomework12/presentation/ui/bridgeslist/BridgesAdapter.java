package com.example.irene.khramovahomework12.presentation.ui.bridgeslist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irene.khramovahomework12.R;
import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.util.DivorceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgesAdapter extends RecyclerView.Adapter<BridgesAdapter.BridgeViewHolder> {

    private List<Bridge> bridges = new ArrayList<>();
    private OnItemClick onItemClick;

    public BridgesAdapter(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public BridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BridgeViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_bridge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BridgeViewHolder holder, int position) {
        Bridge item = bridges.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(view -> onItemClick.onClick(item));
    }

    @Override
    public int getItemCount() {
        return bridges.size();
    }

    public void setBridges(List<Bridge> bridges) {
        this.bridges = bridges;
        notifyDataSetChanged();
    }

    static class BridgeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTitle) TextView textViewTitle;
        @BindView(R.id.textViewDivorceTime) TextView textViewDivorceTime;
        @BindView(R.id.imageViewBridge) ImageView imageViewBridge;
        @BindView(R.id.imageViewBell) ImageView imageViewBell;

        BridgeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Bridge bridge) {
            textViewTitle.setText(bridge.getName());
            textViewDivorceTime.setText(DivorceUtil.getDivorceTime(bridge));
            imageViewBridge.setImageResource(DivorceUtil.getDivorceImgResId(bridge));
            imageViewBell.setImageResource(R.drawable.ic_kolocol_off);
        }
    }

    public interface OnItemClick {
        void onClick(Bridge bridge);
    }
}

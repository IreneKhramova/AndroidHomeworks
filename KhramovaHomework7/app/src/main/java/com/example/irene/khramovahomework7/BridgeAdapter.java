package com.example.irene.khramovahomework7;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgeAdapter extends RecyclerView.Adapter<BridgeAdapter.ViewHolder> {
    static final int MINUTES_IN_HOUR = 60;
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
        holder.itemView.setOnClickListener(view -> mOnItemClick.onClick(item, holder));
    }

    @Override
    public int getItemCount() {
        return mBridges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static final String DIVORCE_SOON = "Осталось меньше часа до развода моста";
        static final String DIVORCE_LATE = "Мост разведен";
        static final String DIVORCE_NORMAL = "Мост сведен";
        @BindView(R.id.imageViewBridge) ImageView mImageViewBridge;
        @BindView(R.id.textViewBridgeName) TextView mTextViewBridgeName;
        @BindView(R.id.textViewDivorceTime) TextView mTextViewDivorceTime;
        @BindView(R.id.imageViewBell) ImageView mImageViewBell;
        private StringBuffer mDivorceTime;
        private String mDivorceMark;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Bridge bridge) {
            mTextViewBridgeName.setText(bridge.getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());

            mDivorceTime = new StringBuffer();
            for (Divorce divorce : bridge.getDivorces()) {
                String start = simpleDateFormat.format(divorce.getStart());
                String end = simpleDateFormat.format(divorce.getEnd());
                mDivorceTime
                        .append(start)
                        .append(" - ")
                        .append(end)
                        .append("     ");
            }
            mTextViewDivorceTime.setText(mDivorceTime);

            String currTime = simpleDateFormat.format(System.currentTimeMillis());
            String[] partsCurr = currTime.split(":");
            int currMin = MINUTES_IN_HOUR * Integer.parseInt(partsCurr[0]) + Integer.parseInt(partsCurr[1]);
            mDivorceMark = DIVORCE_NORMAL;

            for (int i = 0; i < bridge.getDivorces().size(); i++) {
                Divorce divorce = bridge.getDivorces().get(i);
                String start = simpleDateFormat.format(divorce.getStart());
                String end = simpleDateFormat.format(divorce.getEnd());

                String[] partsStart = start.split(":");
                String[] partsEnd = end.split(":");
                int startMin = MINUTES_IN_HOUR * Integer.parseInt(partsStart[0]) + Integer.parseInt(partsStart[1]);
                int endMin = MINUTES_IN_HOUR * Integer.parseInt(partsEnd[0]) + Integer.parseInt(partsEnd[1]);

                if (startMin - currMin <= MINUTES_IN_HOUR && startMin > currMin) {
                    mDivorceMark = DIVORCE_SOON;
                } else if (startMin > endMin) {
                    //В промежуток попадает полночь
                    if (currMin >= startMin || currMin < endMin) {
                        mDivorceMark = DIVORCE_LATE;
                        break;
                    }
                } else {
                    if (currMin >= startMin && currMin < endMin) {
                        mDivorceMark = DIVORCE_LATE;
                        break;
                    }
                }
            }

            switch (mDivorceMark) {
                case DIVORCE_LATE:
                    mImageViewBridge.setImageResource(R.drawable.ic_bridge_late);
                    break;
                case DIVORCE_SOON:
                    mImageViewBridge.setImageResource(R.drawable.ic_bridge_soon);
                    break;
                case DIVORCE_NORMAL:
                    mImageViewBridge.setImageResource(R.drawable.ic_bridge_normal);
                    break;
            }

            //
            mImageViewBell.setImageResource(R.drawable.ic_kolocol_off);
        }

        public Integer getBridgeImage() {
            switch (mDivorceMark) {
                case DIVORCE_LATE:
                    return R.drawable.ic_bridge_late;
                case DIVORCE_SOON:
                    return R.drawable.ic_bridge_soon;
                case DIVORCE_NORMAL:
                    return R.drawable.ic_bridge_normal;
            }
            return null;
        }

        public boolean isDivorced() {
            if (mDivorceMark.equals(DIVORCE_NORMAL)) {
                return false;
            } else {
                return true;
            }
        }

        public String getDivorceTime() {
            return mDivorceTime.toString();
        }
    }

    public interface OnItemClick extends Parcelable {
        void onClick(Bridge bridge, ViewHolder holder);
    }
}

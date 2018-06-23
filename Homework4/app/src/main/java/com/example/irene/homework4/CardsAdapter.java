package com.example.irene.homework4;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_CARD_HALF = 1;
    public static final int TYPE_CARD_FULL = 2;
    private List<BaseInfoItem> mCards;

    public CardsAdapter(List<BaseInfoItem> cards) {
        mCards = new ArrayList<>();
        mCards.addAll(cards);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;

        if (viewType == TYPE_CARD_HALF) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_half_screen, parent, false);

            holder = new CardHalfViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_full_screen, parent, false);

            holder = new CardFullViewHolder(view);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, mCards.get(holder.getAdapterPosition()).getTitle(), Snackbar.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CARD_HALF) {

            ((CardHalfViewHolder) holder).mTextViewTitle
                    .setText(mCards.get(position).getTitle());

            ((CardHalfViewHolder) holder).mImageView
                    .setImageResource(mCards.get(position).getIcon());

            ((CardHalfViewHolder) holder).mTextViewInfo
                    .setText(((DetailInfoItem) mCards.get(position)).getTextInfo());

            if (((DetailInfoItem) mCards.get(position)).isNeedAttention()) {
                ((CardHalfViewHolder) holder).mTextViewInfo
                        .setTextColor(ContextCompat.getColor(((CardHalfViewHolder) holder).mTextViewInfo.getContext(), R.color.coral));
            }

        } else {
            ((CardFullViewHolder) holder).mTextViewTitle.setText(mCards.get(position).getTitle());
            ((CardFullViewHolder) holder).mImageView.setImageResource(mCards.get(position).getIcon());
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseInfoItem item = mCards.get(position);
        if (item instanceof DetailInfoItem) {
            return TYPE_CARD_HALF;
        } else {
            return TYPE_CARD_FULL;
        }
    }

    public class CardHalfViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewInfo;

        public CardHalfViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewCardHalf);
            mTextViewTitle = itemView.findViewById(R.id.textViewCardHalf);
            mTextViewInfo = itemView.findViewById(R.id.textViewInfo);
        }
    }

    public class CardFullViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;

        public CardFullViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewCardFull);
            mTextViewTitle = itemView.findViewById(R.id.textViewCardFull);
        }
    }
}

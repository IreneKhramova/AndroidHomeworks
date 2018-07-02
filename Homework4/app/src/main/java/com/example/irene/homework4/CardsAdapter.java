package com.example.irene.homework4;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    private OnCardClickListener mOnCardClickListener;

    CardsAdapter(List<BaseInfoItem> cards, OnCardClickListener onCardClickListener) {
        mCards = new ArrayList<>();
        mCards.addAll(cards);
        mOnCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;

        if (viewType == TYPE_CARD_HALF) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_card_half_screen, parent, false);

            holder = new CardHalfViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_card_full_screen, parent, false);

            holder = new CardFullViewHolder(view);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCardClickListener.onCardClick(view, mCards.get(holder.getAdapterPosition()));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseInfoItem item = mCards.get(position);

        if (getItemViewType(position) == TYPE_CARD_HALF) {
            ((CardHalfViewHolder) holder).bind(item);
        } else {
            ((CardFullViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isItemSingleInLine(position)) {
            return TYPE_CARD_FULL;
        } else {
            return TYPE_CARD_HALF;
        }
    }

    private boolean isItemSingleInLine(int position) {
        if (mCards.get(position) instanceof DetailInfoItem) {
            if (position % 2 == 0) {
                if (mCards.size() == (position + 1)) {
                    //Карточка последняя в списке
                    return true;
                } else {
                    //Следующая карточка не DetailInfoItem
                    return !(mCards.get(position + 1) instanceof DetailInfoItem);
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public interface OnCardClickListener {
        void onCardClick(View view, BaseInfoItem item);
    }

    public class CardHalfViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewInfo;

        CardHalfViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewCardHalf);
            mTextViewTitle = itemView.findViewById(R.id.textViewTitleCardHalf);
            mTextViewInfo = itemView.findViewById(R.id.textViewInfoCardHalf);
        }

        void bind(BaseInfoItem item) {
            this.mTextViewTitle.setText(item.getTitle());
            this.mImageView.setImageResource(item.getIcon());
            this.mTextViewInfo.setText(((DetailInfoItem) item).getTextInfo());

            if (((DetailInfoItem) item).isNeedAttention()) {
                this.mTextViewInfo.setTextColor(ContextCompat.getColor(this.mTextViewInfo.getContext(),
                        R.color.coral));
            } else {
                this.mTextViewInfo.setTextColor(ContextCompat.getColor(this.mTextViewInfo.getContext(),
                        R.color.warm_grey_two));
            }
        }
    }

    public class CardFullViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewInfo;

        CardFullViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewCardFull);
            mTextViewTitle = itemView.findViewById(R.id.textViewTitleCardFull);
            mTextViewInfo = itemView.findViewById(R.id.textViewInfoCardFull);
        }

        void bind(BaseInfoItem item) {
            this.mTextViewTitle.setText(item.getTitle());
            this.mImageView.setImageResource(item.getIcon());

            if (item instanceof DetailInfoItem) {
                this.mTextViewInfo.setText(((DetailInfoItem) item).getTextInfo());
            } else {
                this.mTextViewInfo.setVisibility(View.GONE);
            }
        }
    }
}

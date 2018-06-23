package com.example.irene.homework4;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
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
    private static final int TITLE_MARGIN_BOTTOM = 2;
    private List<BaseInfoItem> mCards;

    CardsAdapter(List<BaseInfoItem> cards) {
        mCards = new ArrayList<>();
        mCards.addAll(cards);
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
                Snackbar.make(view,
                        mCards.get(holder.getAdapterPosition()).getTitle(),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseInfoItem item = mCards.get(position);

        if (getItemViewType(position) == TYPE_CARD_HALF) {

            ((CardHalfViewHolder) holder).mTextViewTitle.setText(item.getTitle());
            ((CardHalfViewHolder) holder).mImageView.setImageResource(item.getIcon());
            ((CardHalfViewHolder) holder).mTextViewInfo.setText(((DetailInfoItem) item).getTextInfo());

            if (((DetailInfoItem) item).isNeedAttention()) {
                ((CardHalfViewHolder) holder).mTextViewInfo
                        .setTextColor(ContextCompat.getColor(((CardHalfViewHolder) holder)
                                .mTextViewInfo.getContext(), R.color.coral));
            }
        } else {
            ((CardFullViewHolder) holder).mTextViewTitle.setText(item.getTitle());
            ((CardFullViewHolder) holder).mImageView.setImageResource(item.getIcon());

            if (item instanceof DetailInfoItem) {
                ((CardFullViewHolder) holder).mTextViewInfo.setText(((DetailInfoItem) item).getTextInfo());
            } else {
                /* Не отображаем textInfo, выравниваем title по центру по вертикали */

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(((CardFullViewHolder) holder).mConstraintLayout);
                constraintSet.connect(((CardFullViewHolder) holder).mTextViewTitle.getId(),
                        ConstraintSet.BOTTOM,
                        ((CardFullViewHolder) holder).mConstraintLayout.getId(),
                        ConstraintSet.BOTTOM);

                constraintSet.applyTo(((CardFullViewHolder) holder).mConstraintLayout);

                ConstraintLayout.LayoutParams params =
                        (ConstraintLayout.LayoutParams) ((CardFullViewHolder) holder).mTextViewTitle.getLayoutParams();

                params.setMargins(params.leftMargin, 0, params.rightMargin,
                        Converter.dpToPx(((CardFullViewHolder) holder).itemView.getContext(), TITLE_MARGIN_BOTTOM));

                ((CardFullViewHolder) holder).mTextViewTitle.setLayoutParams(params);
            }
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
    }

    public class CardFullViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewInfo;
        private ConstraintLayout mConstraintLayout;

        CardFullViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewCardFull);
            mTextViewTitle = itemView.findViewById(R.id.textViewTitleCardFull);
            mTextViewInfo = itemView.findViewById(R.id.textViewInfoCardFull);
            mConstraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}

package com.example.irene.khramovahomework6;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment {
    public static final String ARGUMENT_TEXT = "Текст";
    public static final String ARGUMENT_IMAGE = "Картинка";

    @BindView(R.id.imageView) ImageView mImageView;
    @BindView(R.id.textViewImage) TextView mTextView;

    private String mText;
    private int mImageResId;
    private OnImageClick mOnImageClick;

    public static PageFragment newInstance(String text, int imageResId) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TEXT, text);
        args.putInt(ARGUMENT_IMAGE, imageResId);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageClick) {
            mOnImageClick = (OnImageClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageClick");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mText = args.getString(ARGUMENT_TEXT);
            mImageResId = args.getInt(ARGUMENT_IMAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager_content, container, false);
        ButterKnife.bind(this, rootView);

        mTextView.setText(mText);
        mImageView.setImageResource(mImageResId);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnImageClick.onClick(mText);
            }
        });

        return rootView;
    }

    public interface OnImageClick {
        void onClick(String title);
    }
}

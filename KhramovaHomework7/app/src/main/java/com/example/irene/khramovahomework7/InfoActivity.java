package com.example.irene.khramovahomework7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    public static final String EXTRA_BRIDGE = "Bridge";
    @BindView(R.id.toolbarImage) Toolbar mToolbar;
    @BindView(R.id.imageViewPhoto) ImageView mImageViewPhoto;
    @BindView(R.id.linearLayoutButton) LinearLayout mLinearLayoutButton;
    @BindView(R.id.progressBarPhoto) ProgressBar mProgressBarPhoto;
    @BindView(R.id.textViewDescription) TextView mTextViewDescription;
    private Bridge mBridge;

    public static Intent createStartIntent(Context context, Bridge bridge) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(EXTRA_BRIDGE, bridge);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBridge = getIntent().getParcelableExtra(EXTRA_BRIDGE);

        ImageView imageViewBridge = findViewById(R.id.fragment).findViewById(R.id.imageViewBridge);
        TextView textViewBridgeName = findViewById(R.id.fragment).findViewById(R.id.textViewBridgeName);
        TextView textViewDivorceTime = findViewById(R.id.fragment).findViewById(R.id.textViewDivorceTime);

        mProgressBarPhoto.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        mProgressBarPhoto.setVisibility(View.VISIBLE);
        StringBuffer photo = new StringBuffer(MainActivity.BASE_URL);
        if (DivorceUtil.isDivorced(mBridge)) {
            photo.append(mBridge.getPhotoClose());
        } else {
            photo.append(mBridge.getPhotoOpen());
        }
        Glide.with(this)
                .load(photo)
                .listener(new RequestListener<Drawable>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                  Log.d("Load photo", e.getMessage());
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                  mProgressBarPhoto.setVisibility(View.GONE);
                                  mImageViewPhoto.setImageDrawable(resource);
                                  return false;
                              }
                          })
                .into(mImageViewPhoto);

        imageViewBridge.setImageResource(DivorceUtil.getDivorceImgResId(mBridge));
        textViewBridgeName.setText(mBridge.getName());
        textViewDivorceTime.setText(DivorceUtil.getDivorceTime(mBridge));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextViewDescription.setText(Html.fromHtml(mBridge.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mTextViewDescription.setText(Html.fromHtml(mBridge.getDescription()));
        }

        mLinearLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    //Не работает пока =(
    public void showDialog()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);
        ((TextView) view.findViewById(R.id.textViewTitle)).setText(mBridge.getName());
        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(75);
        numberPicker.setMinValue(15);
        //numberPicker.setValue(30);

        //numberPicker.setEnabled(true);
        //numberPicker.setDisplayedValues();
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setView(R.layout.dialog)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ...
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

}

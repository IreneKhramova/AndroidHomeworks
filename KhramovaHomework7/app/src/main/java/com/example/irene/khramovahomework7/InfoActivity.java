package com.example.irene.khramovahomework7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.example.irene.khramovahomework7.data.Bridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    public static final String EXTRA_BRIDGE = "Bridge";
    public static final String EXTRA_BRIDGE_NAME = "Bridge name";
    public static final String EXTRA_TIME_BEFORE_DIVORCE = "Time";
    public static final String EXTRA_ID = "Bridge id";
    public static final long MILLS_IN_MINUTE = 60000;
    @BindView(R.id.toolbarImage) Toolbar mToolbar;
    @BindView(R.id.imageViewPhoto) ImageView mImageViewPhoto;
    @BindView(R.id.linearLayoutButton) LinearLayout mLinearLayoutButton;
    @BindView(R.id.textViewRemind) TextView textViewRemind;
    @BindView(R.id.progressBarPhoto) ProgressBar mProgressBarPhoto;
    @BindView(R.id.textViewDescription) TextView mTextViewDescription;
    @BindView(R.id.fragment) View viewFragment;
    private Bridge mBridge;
    private AlarmManager mAlarmManager;

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

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mBridge = getIntent().getParcelableExtra(EXTRA_BRIDGE);

        ImageView imageViewBridge = viewFragment.findViewById(R.id.imageViewBridge);
        TextView textViewBridgeName = viewFragment.findViewById(R.id.textViewBridgeName);
        TextView textViewDivorceTime = viewFragment.findViewById(R.id.textViewDivorceTime);

        mProgressBarPhoto.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white),
                PorterDuff.Mode.MULTIPLY);
        mProgressBarPhoto.setVisibility(View.VISIBLE);
        StringBuilder photo = new StringBuilder(MainActivity.BASE_URL);
        if (DivorceUtil.isDivorced(mBridge)) {
            photo.append(mBridge.getPhotoClose());
        } else {
            photo.append(mBridge.getPhotoOpen());
        }
        Glide.with(this)
                .load(photo.toString())
                .listener(new RequestListener<Drawable>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                          Target<Drawable> target, boolean isFirstResource) {
                                  Log.d("Load photo", e.getMessage());
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                             DataSource dataSource, boolean isFirstResource) {
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

        mLinearLayoutButton.setOnClickListener(view -> showDialog());
    }

    public void showDialog()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //TODO: размеры окна, font у выбранного значения, margins, цвет разделителя
        View view = inflater.inflate(R.layout.dialog, null);
        ((TextView) view.findViewById(R.id.textViewTitle)).setText(mBridge.getName());
        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        int maxValue = 60;
        int minValue = 15;
        int step = 15;

        Map<String, Long> map = new HashMap<>();

        for (long i = minValue; i <= maxValue; i += step) {
            map.put(String.valueOf(i) + getString(R.string.minutes), i);
        }
        Set<String> keys = map.keySet();
        String[] valueSet = keys.toArray(new String[keys.size()]);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(valueSet.length - 1);
        numberPicker.setValue(1);
        numberPicker.setDisplayedValues(valueSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setView(view)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    String notifyTime = valueSet[numberPicker.getValue()];

                    Intent intent = createIntent(notifyTime);
                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(this, mBridge.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Long divorceStart = DivorceUtil.getNearestDivorceStart(mBridge);
                    Log.d("Time info", String.valueOf(divorceStart - map.get(notifyTime) * MILLS_IN_MINUTE));
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, divorceStart - map.get(notifyTime) * MILLS_IN_MINUTE, pendingIntent);

                    //TODO: куда сохранить время напоминания, чтобы выводить на кнопке?
                    textViewRemind.setText(getString(R.string.remind_time, map.get(notifyTime).toString()));
                });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });

        builder.create().show();
    }

    Intent createIntent(String time) {
        Intent intent = new Intent(this, Receiver.class);
        intent.setAction(DivorceUtil.ACTION_DIVORCE);
        intent.putExtra(EXTRA_BRIDGE_NAME, mBridge.getName());
        intent.putExtra(EXTRA_TIME_BEFORE_DIVORCE, time);
        intent.putExtra(EXTRA_ID, mBridge.getId());
        return intent;
    }
}

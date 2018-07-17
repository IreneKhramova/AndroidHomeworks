package com.example.irene.khramovahomework7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    public static final String EXTRA_BRIDGE = "Мост";
    public static final String EXTRA_BRIDGE_IMAGE = "Картинка";
    public static final String EXTRA_DIVORCE_TIME = "Время развода";
    public static final String EXTRA_IS_DIVORCED = "Мост разведен?";
    @BindView(R.id.toolbarImage) Toolbar mToolbar;
    @BindView(R.id.imageViewPhoto) ImageView mImageViewPhoto;
    @BindView(R.id.linearLayoutButton) LinearLayout mLinearLayoutButton;
    @BindView(R.id.progressBarPhoto) ProgressBar mProgressBarPhoto;
    @BindView(R.id.textViewDescription) TextView mTextViewDescription;
    private Bridge mBridge;
    private int mImageBridgeResId;
    private String mDivorceTime;
    private boolean mIsDivorced;

    public static Intent createStartIntent(Context context, Bridge bridge, int imageResId, String divorceTime, boolean isDivorced) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(EXTRA_BRIDGE, bridge);
        intent.putExtra(EXTRA_BRIDGE_IMAGE, imageResId);
        intent.putExtra(EXTRA_DIVORCE_TIME, divorceTime);
        intent.putExtra(EXTRA_IS_DIVORCED, isDivorced);
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
        mImageBridgeResId = getIntent().getExtras().getInt(EXTRA_BRIDGE_IMAGE);
        mDivorceTime = getIntent().getStringExtra(EXTRA_DIVORCE_TIME);
        mIsDivorced = getIntent().getExtras().getBoolean(EXTRA_IS_DIVORCED);

        ImageView imageViewBridge = findViewById(R.id.fragment).findViewById(R.id.imageViewBridge);
        TextView textViewBridgeName = findViewById(R.id.fragment).findViewById(R.id.textViewBridgeName);
        TextView textViewDivorceTime = findViewById(R.id.fragment).findViewById(R.id.textViewDivorceTime);

        mProgressBarPhoto.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        mProgressBarPhoto.setVisibility(View.VISIBLE);
        StringBuffer photo = new StringBuffer(MainActivity.BASE_URL);
        if (mIsDivorced) {
            photo.append(mBridge.getPhotoClose());
        } else {
            photo.append(mBridge.getPhotoOpen());
        }
        Glide.with(this)
                .load(photo)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBarPhoto.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImageViewPhoto);

        imageViewBridge.setImageResource(mImageBridgeResId);
        textViewBridgeName.setText(mBridge.getName());
        textViewDivorceTime.setText(mDivorceTime);
        mTextViewDescription.setText(Html.fromHtml(mBridge.getDescription()));

        mLinearLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                Toast.makeText(InfoActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Не работает пока =(
    public void showDialog()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);
        NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        //view.findViewById(R.id.textViewTitle).setText(mBridge.name);
        //numberPicker.setMaxValue(75);
        //numberPicker.setMinValue(15);
        //numberPicker.setValue(30);

        //numberPicker.setEnabled(true);
        //numberPicker.setDisplayedValues();
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //picker.setValue((newVal < oldVal)?oldVal-15:oldVal+15);
            }

        });

        //numberPicker.setliste

        /* NumberPicker.OnChangedListener numberPickerOnChangedListener = new NumberPicker.OnChangedListener() {
    @Override
    public void onChanged(NumberPicker picker, int oldVal, int newVal) {
        // получаем позицию из tag объекта NumberPicker
        int position = (Integer) picker.getTag();
        ItemData item = adapter.getItem(position);
        item.number = newVal;
    }
};*/

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder//.setMessage(R.string.dialog_message)
                .setView(R.layout.dialog);
        //.setTitle("Title");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.create().show();
    }
    }
}

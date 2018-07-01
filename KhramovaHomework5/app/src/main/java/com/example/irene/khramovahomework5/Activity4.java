package com.example.irene.khramovahomework5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity4 extends AppCompatActivity {
    private static final String EXTRA_TIME = "Time";

    @BindView(R.id.buttonTo4Again) Button buttonTo4Again;
    @BindView(R.id.textView4) TextView textView4;
    @BindView(R.id.textViewTime) TextView textViewTime;

    public static Intent createStartIntent(Context context, long time) {
        Intent intent = new Intent(context, Activity4.class);
        intent.putExtra(EXTRA_TIME, time);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        ButterKnife.bind(this);

        buttonTo4Again.setText(getString(R.string.button_goto_again));
        textView4.setText(getString(R.string.text_activity, "4"));

        setTimeText(getIntent());

        buttonTo4Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(createStartIntent(Activity4.this, System.currentTimeMillis()));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setTimeText(intent);
    }

    private void setTimeText(Intent intent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        textViewTime.setText(dateFormat.format(intent.getLongExtra(EXTRA_TIME, 0)));
    }
}

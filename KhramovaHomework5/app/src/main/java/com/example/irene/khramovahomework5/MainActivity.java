package com.example.irene.khramovahomework5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.buttonTo4) Button buttonTo4;
    @BindView(R.id.buttonTo2) Button buttonTo2;
    @BindView(R.id.textView1) TextView textView1;

    public static Intent createStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonTo2.setText(getString(R.string.button_goto, "2"));
        buttonTo4.setText(getString(R.string.button_goto, "4"));
        textView1.setText(getString(R.string.text_activity, "1"));

        buttonTo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity4.createStartIntent(MainActivity.this, System.currentTimeMillis()));
            }
        });

        buttonTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity2.createStartIntent(MainActivity.this));
            }
        });
    }
}

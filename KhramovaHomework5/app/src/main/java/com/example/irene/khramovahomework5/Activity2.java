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

public class Activity2 extends AppCompatActivity {

    @BindView(R.id.buttonTo3) Button buttonTo3;
    @BindView(R.id.textView2) TextView textView2;

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Activity2.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        ButterKnife.bind(this);

        buttonTo3.setText(getString(R.string.button_goto, "3"));
        textView2.setText(getString(R.string.text_activity, "2"));

        buttonTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity3.createStartIntent(Activity2.this));
            }
        });
    }
}

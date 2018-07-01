package com.example.irene.khramovahomework5;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity3 extends AppCompatActivity {
    public static final int INPUT_REQUEST = 42;
    public static final String EXTRA_INPUT = "Input";

    @BindView(R.id.buttonTo5) Button buttonTo5;
    @BindView(R.id.buttonTo1) Button buttonTo1;
    @BindView(R.id.textView3) TextView textView3;
    @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Activity3.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        ButterKnife.bind(this);

        buttonTo5.setText(getString(R.string.button_goto, "5"));
        buttonTo1.setText(getString(R.string.button_goto, "1"));
        textView3.setText(getString(R.string.text_activity, "3"));

        buttonTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.createStartIntent(Activity3.this));
            }
        });

        buttonTo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Activity5.createStartIntent(Activity3.this), INPUT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INPUT_REQUEST) {
            if(resultCode == RESULT_OK) {
                Snackbar.make(constraintLayout, data.getStringExtra(EXTRA_INPUT), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.irene.khramovahomework5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity5 extends AppCompatActivity {

    @BindView(R.id.buttonResult) Button buttonResult;
    @BindView(R.id.textView5) TextView textView5;
    @BindView(R.id.editText) EditText editText;

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Activity5.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        ButterKnife.bind(this);

        buttonResult.setText(getString(R.string.button_result));
        textView5.setText(getString(R.string.text_activity, "5"));

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Activity3.EXTRA_INPUT, editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}

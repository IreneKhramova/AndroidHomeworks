package com.example.irene.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonTask1)
    public void onTask1Click() {
        startActivity(Task1Activity.createStartIntent(MainActivity.this));
    }

    @OnClick(R.id.buttonTask2)
    public void onTask2Click() {
        startActivity(Task2Activity.createStartIntent(MainActivity.this));
    }
}

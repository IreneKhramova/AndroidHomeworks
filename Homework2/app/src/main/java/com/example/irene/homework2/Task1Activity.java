package com.example.irene.homework2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Task1Activity extends AppCompatActivity {

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Task1Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);
    }
}

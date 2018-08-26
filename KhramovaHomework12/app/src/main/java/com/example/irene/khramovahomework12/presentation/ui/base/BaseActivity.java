package com.example.irene.khramovahomework12.presentation.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.irene.khramovahomework12.MyApplication;
import com.example.irene.khramovahomework12.di.ApplicationComponents;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ApplicationComponents getApplicationComponents() {
        return ((MyApplication) getApplication()).getApplicationComponents();
    }
}

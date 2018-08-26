package com.example.irene.khramovahomework12;

import android.app.Application;

import com.example.irene.khramovahomework12.di.ApplicationComponents;

public class MyApplication extends Application {

    private ApplicationComponents applicationComponents;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponents = ApplicationComponents.getInstance(this);
    }

    public ApplicationComponents getApplicationComponents() {
        return applicationComponents;
    }
}

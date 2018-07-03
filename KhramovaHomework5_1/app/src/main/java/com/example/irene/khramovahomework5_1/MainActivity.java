package com.example.irene.khramovahomework5_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    public static Intent createStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        onCreateNavigationDrawer();
    }
}

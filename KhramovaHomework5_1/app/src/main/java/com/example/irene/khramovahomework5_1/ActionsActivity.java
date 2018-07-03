package com.example.irene.khramovahomework5_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class ActionsActivity extends BaseActivity {

    public static Intent createStartIntent(Context context) {
        Intent intent = new Intent(context, ActionsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_actions);
        onCreateNavigationDrawer();
    }
}

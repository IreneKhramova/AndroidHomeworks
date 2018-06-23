package com.example.irene.homework4;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int DP_SPACE = 8;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info :
                        new AlertDialog.Builder(MainActivity.this).setMessage(R.string.info)
                                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                        return true;
                    case R.id.home :
                        Toast.makeText(MainActivity.this, R.string.home, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(mAdapter.getItemViewType(position)){
                    case CardsAdapter.TYPE_CARD_HALF:
                        return 1;
                    case CardsAdapter.TYPE_CARD_FULL:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);

        List<BaseInfoItem> cards = new ArrayList<>();
        cards.add(new DetailInfoItem("Квитанции", R.drawable.ic_bill, "- 40 080,55 \u20BD", true));
        cards.add(new DetailInfoItem("Счетчики", R.drawable.ic_counter, "Подайте показания", true));
        cards.add(new DetailInfoItem("Рассрочка", R.drawable.ic_installment, "Сл. платеж 25.02.2018", false));
        cards.add(new DetailInfoItem("Страхование", R.drawable.ic_insurance, "Полис до 12.01.2019", false));
        cards.add(new DetailInfoItem("Интернет и ТВ", R.drawable.ic_tv, "Баланс 350 \u20BD", false));
        cards.add(new DetailInfoItem("Домофон", R.drawable.ic_homephone, "Подключен", false));
        cards.add(new DetailInfoItem("Охрана", R.drawable.ic_guard, "Нет", false));

        cards.add(new BaseInfoItem("Контакты УК и служб", R.drawable.ic_uk_contacts));
        cards.add(new BaseInfoItem("Мои заявки", R.drawable.ic_request));
        cards.add(new BaseInfoItem("Памятка жителя А101", R.drawable.ic_about));

        mAdapter = new CardsAdapter(cards);
        mRecyclerView.setAdapter(mAdapter);

        int pxSpan = Converter.dpToPx(this, DP_SPACE);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(pxSpan));
    }
}
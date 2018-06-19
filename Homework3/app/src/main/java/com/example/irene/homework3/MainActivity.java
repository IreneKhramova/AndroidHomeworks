package com.example.irene.homework3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textViewCard)
    TextView textViewCard;

    @BindView(R.id.editTextFirstName)
    EditText editTextFirstName;

    @BindView(R.id.editTextLastName)
    EditText editTextLastName;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextLogin)
    EditText editTextLogin;

    @BindView(R.id.editTextRegion)
    EditText editTextRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.profile_menu);

        Profile profile = new Profile("7898769", "Анастасия",
                "Антонина", "anykee.box@gmail.ru", "HIE023UOI88",
                "Санкт-Петербург");

        textViewCard.setText(getString(R.string.card_number, profile.getCardNumber()));
        editTextFirstName.setText(profile.getFirstName());
        editTextLastName.setText(profile.getLastName());
        editTextEmail.setText(profile.getEmail());
        editTextLogin.setText(profile.getLogin());
        editTextRegion.setText(profile.getRegion());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Назад", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.actionEdit) {
                    Toast.makeText(MainActivity.this, "Редактируем профиль", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.imageButtonEditRegion)
    public void onEditRegionClick() {
        Toast.makeText(this, "Редактируем регион", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.textViewExit)
    public void onExitClick() {
        Toast.makeText(this, "Выходим из аккаунта", Toast.LENGTH_SHORT).show();
    }
}

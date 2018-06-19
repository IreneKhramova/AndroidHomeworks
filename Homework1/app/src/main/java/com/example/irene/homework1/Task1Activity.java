package com.example.irene.homework1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Task1Activity extends AppCompatActivity {
    @BindView(R.id.editTextName) EditText editTextName;
    @BindView(R.id.textViewStudents) TextView textViewStudents;
    @BindView(R.id.buttonSave) Button buttonSave;
    @BindView(R.id.buttonShow) Button buttonShow;

    private TreeSet<String> mStudents = new TreeSet<>();

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Task1Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);
        ButterKnife.bind(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] names = editTextName.getText().toString().split(",");

                for(String student : names) {
                    mStudents.add(student.trim());
                }

                editTextName.setText("");
            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewStudents.setText("");
                for(String name : mStudents) {
                    textViewStudents.append(name + "\n");
                }
            }
        });
    }
}

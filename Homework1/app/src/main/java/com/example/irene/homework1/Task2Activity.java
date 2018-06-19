package com.example.irene.homework1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Task2Activity extends AppCompatActivity {
    @BindView(R.id.editTextInfo) EditText editTextInfo;
    @BindView(R.id.buttonShowList) Button buttonShowList;
    @BindView(R.id.textViewStudentsList) TextView textViewStudentsList;

    private Map<Long, Student> mStudents = new HashMap<>();

    public static Intent createStartIntent(Context context) {
        return new Intent(context, Task2Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        ButterKnife.bind(this);

        editTextInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String[] info = editTextInfo.getText().toString().split(" ");

                    Student student = new Student(info[0], info[1], info[2], info[3]);
                    mStudents.put(System.currentTimeMillis(), student);

                    editTextInfo.setText("");
                    return true;
                }
                return false;
            }
        });

        buttonShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewStudentsList.setText("");

                for (Map.Entry<Long, Student> entry : mStudents.entrySet()) {
                    Student student = entry.getValue();
                    textViewStudentsList.append(entry.getKey() + " " + student.getName() + " "
                            + student.getSurname() + " " + student.getGrade() + " "
                            + student.getBirthdayYear() + "\n");
                }
            }
        });
    }
}

package com.example.roshan.activityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class edit extends Activity {
    Button applyBtn;
    EditText stName;
    EditText stAge;
    EditText stMarks;
    EditText stResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_student_record);
        stName = findViewById(R.id.nameEdit);
        stAge = findViewById(R.id.ageEdit);
        stMarks = findViewById(R.id.marksEdit);
        stResult = findViewById(R.id.resultEdit);

        applyBtn = findViewById(R.id.applyButton);

        final Student student = (Student) getIntent().getSerializableExtra("student_object_by_edit");

        stName.setText(student.getStudentName());
        stAge.setText(Integer.toString(student.getStudentAge()));
        stMarks.setText(Double.toString(student.getStudentMarks()));
        stResult.setText(student.getResult());

        applyBtn.setEnabled(true);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(stName,stAge,stMarks,stResult)) {
                    student.setStudentName(stName.getText().toString());
                    student.setStudentAge(Integer.parseInt(stAge.getText().toString()));
                    student.setStudentMarks(Double.parseDouble(stMarks.getText().toString()));
                    student.setResult(stResult.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra("to_main_with_love", student);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(v.getContext(), "Something is missing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid(EditText f1, EditText f2, EditText f3, EditText f4) {
        String v1 = f1.getText().toString();
        String v2 = f2.getText().toString();
        String v3 = f3.getText().toString();
        String v4 = f4.getText().toString();

        if (!v1.isEmpty() && isValidInt(v2) && isValidFloat(v3) && !v4.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidInt(String s) {

        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private boolean isValidFloat(String s) {
        try {
            Double.parseDouble(s);
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}

package com.example.roshan.activityapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.color.darker_gray;
import static android.R.color.transparent;


public class MainActivity extends Activity {
    public static final int EDIT_REQ_TO_CHILD = 0;
    public static final int ADD_REQ_TO_CHILD = 1;
    Button addBtn;
    Button editBtn;
    Button deleteBtn;
    ListView list;
    ArrayList<Student> studentList = new ArrayList<>();
    MyAdapter adapter;
    int itemPosition = -1;
    String fileName = "studentData";

    public void addItems(String x) {
        try {
            File file = new File(getApplicationContext().getFilesDir(), x);
            file.setReadable(true);
            InputStream inputStream = new FileInputStream(file);
            InputStream buffered = new BufferedInputStream(inputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(buffered);
            studentList = (ArrayList<Student>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            addItems(fileName);
            // adapter.notifyDataSetChanged();
        }

        if (savedInstanceState != null)
            studentList = (ArrayList<Student>) savedInstanceState.getSerializable("studentsList");
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        //list of student objects
        adapter = new MyAdapter(this, R.layout.list_row_layout, studentList);
        list.setAdapter(adapter);
        addBtn = findViewById(R.id.addButtonAtListActivity);
        editBtn = findViewById(R.id.editButton);
        deleteBtn = findViewById(R.id.deleteButton);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditStudentRecord.class);
                startActivityForResult(intent, ADD_REQ_TO_CHILD);
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                addBtn.setEnabled(false);
                final Student student = studentList.get(position);
                itemPosition = -1;
                itemPosition = position;
                view.setBackgroundColor(getResources().getColor(darker_gray));
                adapter.notifyDataSetChanged();
                //for edit button
                editBtn.setEnabled(true);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSentByEditButton = new Intent(v.getContext(), edit.class);
                        intentSentByEditButton.putExtra("student_object_by_edit", student);
                        startActivityForResult(intentSentByEditButton, EDIT_REQ_TO_CHILD);
                        editBtn.setEnabled(false);
                        deleteBtn.setEnabled(false);
                        view.setBackgroundColor(getResources().getColor(transparent));
                        addBtn.setEnabled(true);
                    }
                });
                //for delete Button
                deleteBtn.setEnabled(true);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentList.remove(position);
                        adapter.notifyDataSetChanged();
                        editBtn.setEnabled(false);
                        deleteBtn.setEnabled(false);
                        view.setBackgroundColor(getResources().getColor(transparent));
                        addBtn.setEnabled(true);
                    }
                });
            }
        });

    }

// On Activity Result Method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQ_TO_CHILD) {
            if (resultCode == Activity.RESULT_OK) {
                Student studentX = (Student) data.getSerializableExtra("student_object_by_apply_button");
                studentList.add(studentX);
            }
        }

        if (requestCode == EDIT_REQ_TO_CHILD) {
            if (resultCode == Activity.RESULT_OK) {
                Student studentX = (Student) data.getSerializableExtra("to_main_with_love");
                studentList.set(itemPosition, studentX);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("studentsList", studentList);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ListIterator<Student>studentListIterator = studentList.listIterator();
        File dataFile = new File(getApplicationContext().getFilesDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(studentList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //setting A List Adapter for List View//
    private class MyAdapter extends ArrayAdapter<Student> {

        private final Context context;
        private final List<Student> studentList;

        public MyAdapter(Context context, int resource, List<Student> objects) {
            super(context, resource, objects);
            this.context = context;
            this.studentList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_row_layout, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.studentName = convertView.findViewById(R.id.student_name_text_field);
                viewHolder.studentAge = convertView.findViewById(R.id.student_age_text_field);
                viewHolder.studentMarks = convertView.findViewById(R.id.student_marks_text_field);
                viewHolder.studentResult = convertView.findViewById(R.id.student_result_text_field);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Student student = getItem(position);
            //  convertView.setBackgroundColor(getResources().getColor(position == itemPosition ? R.color.colorPrimary : R.color.colorAccent));
            viewHolder.studentName.setText(student.getStudentName());
            viewHolder.studentAge.setText(Integer.toString(student.getStudentAge()) + " years");
            viewHolder.studentMarks.setText(Double.toString(student.getStudentMarks()) + "%");
            viewHolder.studentResult.setText(student.getResult());

            return convertView;
        }

        class ViewHolder {
            private TextView studentName;
            private TextView studentAge;
            private TextView studentMarks;
            private TextView studentResult;
        }

    }
}



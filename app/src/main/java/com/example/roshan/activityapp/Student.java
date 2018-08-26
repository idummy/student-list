package com.example.roshan.activityapp;

import java.io.Serializable;

public class Student implements Serializable {
    private static int id;
    private String studentName;
    private int studentAge;
    private double studentMarks;
    private String result;

    public Student() {
    }

    public Student(String name, int age, double marks, String result) {
        this.studentName = name;
        this.studentAge = age;
        this.studentMarks = marks;
        this.result = result;
    }

    public static int getId() {
        return id;
    }

    public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setStudentMarks(double studentMarks) {
        this.studentMarks = studentMarks;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getStudentMarks() {
        return studentMarks;
    }

    public String getResult() {
        return result;
    }

    public int getStudentAge() {
        return studentAge;
    }

    public String getStudentName() {
        return studentName;
    }
}



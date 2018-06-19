package com.example.irene.homework1;

public class Student {
    private String name;
    private String surname;
    private String grade;
    private String birthdayYear;

    public Student(String name, String surname, String grade, String birthdayYear) {
        this.name = name;
        this.surname = surname;
        this.grade = grade;
        this.birthdayYear = birthdayYear;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGrade() {
        return grade;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }
}
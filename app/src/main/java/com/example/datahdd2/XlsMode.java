package com.example.datahdd2;

public class XlsMode {
    private String studentid;
    private String studentName;
    private String studentClass;
    private String studentBench;
    private String studentAge;
    private String studentGender;
    private String studentGrade;


    public XlsMode(String studentid, String studentName,
                   String studentClass, String studentBench,
                   String studentAge, String studentGender,
                   String studentGrade) {
        this.studentid = studentid;
        this.studentName = studentName;
        this.studentClass = studentClass;
        this.studentBench = studentBench;
        this.studentAge = studentAge;
        this.studentGender = studentGender;
        this.studentGrade = studentGrade;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentBench() {
        return studentBench;
    }

    public void setStudentBench(String studentBench) {
        this.studentBench = studentBench;
    }

    public String getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }


}

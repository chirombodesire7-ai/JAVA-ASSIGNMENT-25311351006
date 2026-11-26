
package QUESTION2;

import java.util.Date;

public class Student {

    private String studentId;
    private String name;
    private String course;
    private double mark;
    private Date dateAdded;

    public Student(String studentId, String name, String course,
                   double mark, Date dateAdded) {

        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.mark = mark;
        this.dateAdded = dateAdded;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public double getMark() {
        return mark;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    @Override
    public String toString() {
        return "ID: " + studentId +
               ", Name: " + name +
               ", Course: " + course +
               ", Mark: " + mark +
               ", Date Added: " + dateAdded;
    }
}

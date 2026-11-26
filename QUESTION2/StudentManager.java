
package QUESTION2;

import java.util.Vector;
import java.util.Stack;
import java.util.Enumeration;
import java.util.Random;
import java.util.Date;
import java.util.GregorianCalendar;

public class StudentManager {

    private Vector<Student> students = new Vector<Student>();
    private Stack<Student> undoStack = new Stack<Student>();
    private Random random = new Random();

    public void addStudent(String name, String course, double mark) {

        int id;

        do {
            id = 100000 + random.nextInt(900000);
        } while (idExists(id));

        GregorianCalendar calendar = new GregorianCalendar();
        Date dateAdded = calendar.getTime();

        Student student = new Student(
            String.valueOf(id), name, course, mark, dateAdded
        );

        students.add(student);
        undoStack.push(student);

        System.out.println("Student added successfully.");
    }

    public void addLoadedStudent(Student student) {
        students.add(student);
    }

    public void clearRecords() {
        students.clear();
        undoStack.clear();
    }

    private boolean idExists(int id) {

        Enumeration<Student> records = students.elements();

        while (records.hasMoreElements()) {
            Student student = records.nextElement();

            if (student.getStudentId().equals(String.valueOf(id))) {
                return true;
            }
        }

        return false;
    }

    public void undo() {

        if (!undoStack.empty()) {

            Student student = undoStack.pop();
            students.remove(student);

            System.out.println("Last added student removed.");

        } else {
            System.out.println("Nothing to undo.");
        }
    }

    public void displayStudents() {

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        Enumeration<Student> records = students.elements();

        while (records.hasMoreElements()) {
            System.out.println(records.nextElement());
        }
    }

    public Vector<Student> getStudents() {
        return students;
    }
}

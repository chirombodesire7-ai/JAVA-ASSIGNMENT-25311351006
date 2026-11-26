
package QUESTION2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class StudentRecordsGUI extends JFrame {

    private JTextField nameField;
    private JTextField courseField;
    private JTextField markField;
    private JTextArea displayArea;

    private StudentManager manager = new StudentManager();

    private final Path folder;
    private final Path studentFile;
    private final Path backupFile;

    public StudentRecordsGUI() {

        Path workingDirectory = Paths.get(
            System.getProperty("user.dir")
        );

        if (workingDirectory.getFileName().toString()
                .equalsIgnoreCase("QUESTION2")) {
            folder = workingDirectory;
        } else {
            folder = workingDirectory.resolve("QUESTION2");
        }

        studentFile = folder.resolve("students.txt");
        backupFile = folder.resolve("students.bak");

        setTitle("Student Records Manager");
        setSize(750, 550);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(
            new GridLayout(3, 2, 5, 5)
        );

        inputPanel.setBorder(
            BorderFactory.createTitledBorder("Student Details")
        );

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Mark:"));
        markField = new JTextField();
        inputPanel.add(markField);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton displayButton = new JButton("Display");
        JButton undoButton = new JButton("Undo");
        JButton searchButton = new JButton("Search");

        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(searchButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(
            new Font("Monospaced", Font.PLAIN, 13)
        );

        JScrollPane scrollPane = new JScrollPane(displayArea);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Student Records")
        );

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());

        saveButton.addActionListener(e -> saveRecords());

        loadButton.addActionListener(e -> loadRecords());

        displayButton.addActionListener(e -> displayStudents());

        undoButton.addActionListener(e -> {
            manager.undo();
            displayStudents();
        });

        searchButton.addActionListener(e -> searchStudent());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addStudent() {

        String name = nameField.getText().trim();
        String course = courseField.getText().trim();

        if (name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter the student's name and course.");
            return;
        }

        try {

            double mark = Double.parseDouble(
                markField.getText().trim()
            );

            if (mark < 0 || mark > 100) {
                JOptionPane.showMessageDialog(this,
                    "Mark must be between 0 and 100.");
                return;
            }

            manager.addStudent(name, course, mark);

            JOptionPane.showMessageDialog(this,
                "Student added successfully.");

            nameField.setText("");
            courseField.setText("");
            markField.setText("");

            displayStudents();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric mark.");
        }
    }

    private void displayStudents() {

        displayArea.setText("");

        Enumeration<Student> records =
            manager.getStudents().elements();

        while (records.hasMoreElements()) {

            Student student = records.nextElement();

            displayArea.append(
                student.toString() + "\n\n"
            );
        }

        if (manager.getStudents().isEmpty()) {
            displayArea.setText("No student records found.");
        }
    }

    private void saveRecords() {

        try {

            Files.createDirectories(folder);

            int count = 0;

            try (PrintWriter writer = new PrintWriter(
                    new BufferedWriter(
                        new FileWriter(studentFile.toFile())
                    ))) {

                Enumeration<Student> records =
                    manager.getStudents().elements();

                while (records.hasMoreElements()) {

                    Student student = records.nextElement();

                    writer.println(
                        student.getStudentId() + "|" +
                        student.getName() + "|" +
                        student.getCourse() + "|" +
                        student.getMark() + "|" +
                        student.getDateAdded().getTime()
                    );

                    count++;
                }

                writer.flush();

                if (writer.checkError()) {
                    throw new IOException(
                        "Error writing student records."
                    );
                }
            }

            // Create backup using FileInputStream and FileOutputStream

            try (
                FileInputStream input = new FileInputStream(
                    studentFile.toFile()
                );

                FileOutputStream output = new FileOutputStream(
                    backupFile.toFile()
                )
            ) {

                int data;

                while ((data = input.read()) != -1) {
                    output.write(data);
                }
            }

            JOptionPane.showMessageDialog(this,
                count + " records saved successfully.\n\n" +
                "Student file:\n" +
                studentFile.toAbsolutePath() +
                "\n\nBackup file:\n" +
                backupFile.toAbsolutePath());

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(this,
                "Saving failed: " + ex.getMessage());
        }
    }

    private void loadRecords() {

        try {

            if (!Files.exists(studentFile)) {
                JOptionPane.showMessageDialog(this,
                    "No saved student file found.\n" +
                    studentFile.toAbsolutePath());
                return;
            }

            int count = 0;

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(studentFile.toFile())
                )) {

                manager.clearRecords();

                String line;

                while ((line = reader.readLine()) != null) {

                    StringTokenizer tokenizer =
                        new StringTokenizer(line, "|");

                    if (tokenizer.countTokens() != 5) {
                        continue;
                    }

                    String id = tokenizer.nextToken();
                    String name = tokenizer.nextToken();
                    String course = tokenizer.nextToken();

                    double mark = Double.parseDouble(
                        tokenizer.nextToken()
                    );

                    long time = Long.parseLong(
                        tokenizer.nextToken()
                    );

                    Student student = new Student(
                        id, name, course, mark, new Date(time)
                    );

                    manager.addLoadedStudent(student);
                    count++;
                }
            }

            displayStudents();

            JOptionPane.showMessageDialog(this,
                count + " student records loaded successfully.");

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(this,
                "Loading failed: " + ex.getMessage());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                "Invalid data in the student records file.");
        }
    }

    private void searchStudent() {

        String searchName = JOptionPane.showInputDialog(
            this, "Enter student name to search:"
        );

        if (searchName == null || searchName.trim().isEmpty()) {
            return;
        }

        Scanner scanner = new Scanner(searchName);

        String nameToFind = scanner.nextLine().trim();
        scanner.close();

        boolean found = false;
        int count = 0;

        displayArea.setText("");

        Enumeration<Student> records =
            manager.getStudents().elements();

        while (records.hasMoreElements()) {

            Student student = records.nextElement();

            if (student.getName().trim().toLowerCase()
                    .contains(nameToFind.toLowerCase())) {

                displayArea.append(
                    student.toString() + "\n\n"
                );

                found = true;
                count++;
            }
        }

        if (found) {

            JOptionPane.showMessageDialog(this,
                count + " matching student record(s) found.");

        } else {

            displayArea.setText("Student not found.");

            JOptionPane.showMessageDialog(this,
                "No student found with the name: " + nameToFind);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentRecordsGUI();
            }
        });
    }
}

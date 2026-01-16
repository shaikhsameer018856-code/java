import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.LinkedList;
import java.util.Stack;

class Student {
    int rollno;
    String name;
    int age;

    Student(int rollno, String name, int age) {
        this.rollno = rollno;
        this.name = name;
        this.age = age;
    }
}

class Action {
    String type;
    Student student;
    Student old;

    Action(String type, Student student, Student old) {
        this.type = type;
        this.student = student;
        this.old = old;
    }
}

public class Students extends JFrame {

    LinkedList<Student> list = new LinkedList<>();
    Stack<Action> undo = new Stack<>();

    JTextField roll, name, age;

    JTable table;
    DefaultTableModel model;

    Students() {

        setTitle("Student Management System");
        setSize(750, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        inputPanel.add(new JLabel("Roll No:"));
        roll = new JTextField(8);
        inputPanel.add(roll);

        inputPanel.add(new JLabel("Name:"));
        name = new JTextField(15);
        inputPanel.add(name);

        inputPanel.add(new JLabel("Age:"));
        age = new JTextField(5);
        inputPanel.add(age);

        JButton addbtn = new JButton("Add");
        inputPanel.add(addbtn);

        add(inputPanel, BorderLayout.NORTH);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        JButton deletebtn = new JButton("Delete");
        JButton updbtn = new JButton("Update");
        JButton displaybtn = new JButton("Display All");
        JButton searchbtn = new JButton("Search");
        JButton sortbtn = new JButton("Sort");
        JButton undobtn = new JButton("Undo");

        buttonPanel.add(deletebtn);
        buttonPanel.add(updbtn);
        buttonPanel.add(displaybtn);
        buttonPanel.add(searchbtn);
        buttonPanel.add(sortbtn);
        buttonPanel.add(undobtn);

        
        add(buttonPanel, BorderLayout.CENTER);

        // ---------------- TABLE ----------------
        model = new DefaultTableModel(new String[]{"Roll No", "Name", "Age"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Students List"));

        
        add(scroll, BorderLayout.SOUTH);

        

        addbtn.addActionListener(e -> {
            try {
                int r = Integer.parseInt(roll.getText());
                String n = name.getText();
                int a = Integer.parseInt(age.getText());

                Student s = new Student(r, n, a);
                list.add(s);
                undo.push(new Action("add", s, null));
                refreshTable();

                JOptionPane.showMessageDialog(this, "Added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!!");
            }
        });

        deletebtn.addActionListener(e -> {
            try {
                int r = Integer.parseInt(roll.getText());
                Student s = find(r);
                if (s != null) {
                    list.remove(s);
                    undo.push(new Action("del", s, null));
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Deleted!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        updbtn.addActionListener(e -> {
            try {
                int r = Integer.parseInt(roll.getText());
                Student s = find(r);
                if (s != null) {
                    Student old = new Student(s.rollno, s.name, s.age);
                    s.name = name.getText();
                    s.age = Integer.parseInt(age.getText());

                    undo.push(new Action("upd", s, old));
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Updated!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        displaybtn.addActionListener(e -> refreshTable());

        searchbtn.addActionListener(e -> {
            try {
                int r = Integer.parseInt(roll.getText());
                Student s = find(r);

                model.setRowCount(0);
                if (s != null) {
                    model.addRow(new Object[]{s.rollno, s.name, s.age});
                } else {
                    JOptionPane.showMessageDialog(this, "Not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        sortbtn.addActionListener(e -> {
            list.sort(Comparator.comparingInt(x -> x.rollno));
            refreshTable();
            JOptionPane.showMessageDialog(this, "Sorted by Roll No!");
        });

        undobtn.addActionListener(e -> {
            if (undo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nothing to undo!");
                return;
            }
            Action ac = undo.pop();
            if (ac.type.equals("add")) list.remove(ac.student);
            if (ac.type.equals("del")) list.add(ac.student);
            if (ac.type.equals("upd")) {
                ac.student.name = ac.old.name;
                ac.student.age = ac.old.age;
            }
            refreshTable();
            JOptionPane.showMessageDialog(this, "Undo Done!");
        });

        setVisible(true);
    }

    void refreshTable() {
        model.setRowCount(0);
        for (Student s : list)
            model.addRow(new Object[]{s.rollno, s.name, s.age});
    }

    Student find(int r) {
        for (Student s : list)
            if (s.rollno == r) return s;
        return null;
    }

    public static void main(String[] args) {
        new Students();
    }
}
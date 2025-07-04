package model;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class RegisterPage extends JFrame {
    RegisterPage(){
        Font f1 = new Font("Calibri",Font.PLAIN,20);
        Font f2 = new Font("Segoe UI Emoji",Font.BOLD,35);

        JLabel title = new JLabel("\uD83D\uDCDD Register",JLabel.CENTER);
        title.setForeground(new Color(0, 255, 180));
        JLabel l1 = new JLabel("Set Username:");
        JTextField t1 = new JTextField(10);
        JLabel l4 = new JLabel("Email:");
        JTextField t4 = new JTextField(20);
        JLabel l2 = new JLabel("Set Password:");
        JPasswordField t2 = new JPasswordField(10);
        JLabel l3 = new JLabel("Confirm Password:");
        JPasswordField t3 = new JPasswordField(10);
        JButton b1 = new JButton("Sign Up");
        b1.setBackground(new Color(0, 153, 102));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setOpaque(true);
        JButton b2 = new JButton("Back");
        b2.setBackground(new Color(70, 70, 70));
        b2.setForeground(new Color(200, 200, 200));
        b2.setFocusPainted(false);
        b2.setBorderPainted(false);
        b2.setOpaque(true);


        title.setFont(f2);
        l1.setFont(f1);
        l2.setFont(f1);
        l3.setFont(f1);
        l4.setFont(f1);
        t1.setFont(f1);
        t2.setFont(f1);
        t3.setFont(f1);
        t4.setFont(f1);
        b1.setFont(f1);
        b2.setFont(f1);

        Container c = getContentPane();
        c.setLayout(null);

        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l4);
        c.add(t4);
        c.add(l2);
        c.add(t2);
        c.add(l3);
        c.add(t3);
        c.add(b1);
        c.add(b2);

        int labelX = 200, fieldX = 380, yStart = 125, width = 180, height = 30, gap = 50;

        title.setBounds(250, 40, 300, 50);

        l1.setBounds(labelX, yStart, width, height);
        t1.setBounds(fieldX, yStart, width, height);

        l2.setBounds(labelX, yStart + gap, width, height);
        t2.setBounds(fieldX, yStart + gap, width, height);

        l3.setBounds(labelX, yStart + 2 * gap, width, height);
        t3.setBounds(fieldX, yStart + 2 * gap, width, height);

        l4.setBounds(labelX, yStart + 3 * gap, width, height);
        t4.setBounds(fieldX, yStart + 3 * gap, width, height);

        b1.setBounds(310, yStart + 4 * gap + 10, 150, 40);
        b2.setBounds(310, yStart + 5 * gap + 10, 150, 40);

        b1.addActionListener(
                a->{
                    String username = t1.getText().trim();
                    String email = t4.getText().trim();
                    String password = new String(t2.getPassword());
                    String confirm_pass = new String(t3.getPassword());
                    if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_pass.isEmpty()){
                        JOptionPane.showMessageDialog(null,"All fields are required");
                        t1.setText("");
                        t2.setText("");
                        t3.setText("");
                        t4.setText("");
                        return;
                    }
                    if(password.equals(confirm_pass))
                    {
                        String url = "jdbc:mysql://localhost:3306/expense_tracker";
                        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
                            String check = "SELECT * FROM users WHERE username=? OR email=?";
                            try(PreparedStatement checkstmt = con.prepareStatement(check))
                            {
                                checkstmt.setString(1,username);
                                checkstmt.setString(2,email);
                                ResultSet rs = checkstmt.executeQuery();
                                if(rs.next())
                                {
                                    JOptionPane.showMessageDialog(null, "Username or Email already exists");
                                    return;
                                }
                            }
                            String sql = "insert into users(username,email,password) values(?, ?, ?)";
                            try(PreparedStatement pst = con.prepareStatement(sql))
                            {
                                pst.setString(1,username);
                                pst.setString(2,email);
                                pst.setString(3,password);
                                pst.executeUpdate();
                                JOptionPane.showMessageDialog(null,"SignUp Successfull, Redirecting to Login Page...");
                                new LoginPage();
                                dispose();
                            }
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Passwords do not match");
                        t2.setText("");
                        t3.setText("");
                    }
                }
        );
        b2.addActionListener(
                a->{
                    new LoginPage();
                }
        );

        setVisible(true);
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Register Page");

    }

    public static void main(String[] args) {
        new RegisterPage();
    }
}

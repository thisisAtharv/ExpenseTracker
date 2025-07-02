package model;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class LoginPage extends JFrame{
    LoginPage(){
        Font f1 = new Font("Calibri",Font.PLAIN,20);
        Font f2 = new Font("Segoe UI Emoji",Font.BOLD,35);

        JLabel title = new JLabel("\uD83D\uDCC8 Smart Expense Tracker",JLabel.CENTER);
//        title.setForeground(new Color(72, 133, 237));
        title.setForeground(new Color(0, 255, 200));
        JLabel l1 = new JLabel("Username:");
        JTextField t1 = new JTextField(15);
        JLabel l2 = new JLabel("Password:");
        JPasswordField t2 = new JPasswordField(15);
        JButton b1 = new JButton("Login");
//        b1.setForeground(Color.WHITE);
//        b1.setBackground(new Color(0, 153, 255));
        b1.setForeground(Color.WHITE);
        b1.setBackground(new Color(50, 202, 202));
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setOpaque(true);
        JLabel l3 = new JLabel("Don't have an account? Register here");
        l3.setForeground(new Color(50, 202, 202));
        l3.setCursor(new Cursor(Cursor.HAND_CURSOR));

        title.setFont(f2);
        l1.setFont(f1);
        t1.setFont(f1);
        l2.setFont(f1);
        t2.setFont(f1);
        b1.setFont(f1);
        l3.setFont(f1);

        title.setBounds(150, 40, 500, 50);
        l1.setBounds(150, 160, 120, 30);
        t1.setBounds(280, 160, 300, 30);
        l2.setBounds(150, 220, 120, 30);
        t2.setBounds(280, 220, 300, 30);
        b1.setBounds(300, 280, 200, 40);
        l3.setBounds(240, 340, 400, 30);

        Container c = getContentPane();
        c.setLayout(null);

        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(t2);
        c.add(b1);
        c.add(l3);

        b1.addActionListener(
                a->{
                    String username = t1.getText();
                    String password = new String(t2.getPassword());
                    if(username.isEmpty() || password.isEmpty()){
                        JOptionPane.showMessageDialog(null,"All fields are required");
                        t1.setText("");
                        t2.setText("");
                        return;
                    }
                    String url = "jdbc:mysql://localhost:3306/expense_tracker";
                    try(Connection con = DriverManager.getConnection(url,"root","Atharv@123"))
                    {
                        String sql = "select * from users where username=? and password=?";
                        try(PreparedStatement pst = con.prepareStatement(sql))
                        {
                            pst.setString(1,t1.getText());
                            String s1 = new String(t2.getPassword());
                            pst.setString(2,s1);

                            ResultSet rs = pst.executeQuery();
                            if(rs.next())
                            {
                                JOptionPane.showMessageDialog(null,"Successfull");
                                new Home(username);
                                dispose();
                            }
                            else {
                                JOptionPane.showMessageDialog(null,"User does not Exist");
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                    }
                }
        );

        l3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null,"Redirecting to Register Page...");
                new RegisterPage();
                dispose();
            }
        });
        setVisible(true);
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login Page");
    }

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        }
        catch(Exception ex){
            System.err.println("Failed to initialize LaF");
        }
        new LoginPage();
    }
}
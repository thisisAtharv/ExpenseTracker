package model;

import javax.swing.*;
import java.awt.*;

class Home extends JFrame {
    Home(String username){
        Font f1 = new Font("Segoe UI Emoji",Font.PLAIN,20);
        Font f2 = new Font("Futura",Font.BOLD,28);
        Font f3 = new Font("Noto Sans",Font.ITALIC,20);

        JLabel title = new JLabel("\uD83D\uDC4B Welcome, "+username+"!",JLabel.CENTER);
        JButton b1 = new JButton("← Logout");
        JButton b2 = new JButton("➕ Add Transaction");
        JButton b3 = new JButton("\uD83D\uDCCA View Transactions");
        JButton b4 = new JButton("\uD83D\uDCCA Analytics");
        JButton b5 = new JButton("\uD83D\uDCC1 Manage Categories");

        Container c = getContentPane();
        c.setLayout(null);
        c.add(title);
        c.add(b1);
        c.add(b2);
        c.add(b3);
        c.add(b4);
        c.add(b5);

        title.setFont(f2);
        b1.setFont(f1);
        b2.setFont(f1);
        b3.setFont(f1);
        b4.setFont(f1);
        b5.setFont(f1);

        title.setBounds(165, 40, 400, 50);
        b1.setBounds(620,50, 150, 30);
        b2.setBounds(245,120,235,40);
        b3.setBounds(245,180,235,40);
        b4.setBounds(245,240,235,40);
        b5.setBounds(245,300,235,40);

        b1.addActionListener(
                a->{
                    JOptionPane.showMessageDialog(null,"Redirecting to Login Page...");
                    new LoginPage();
                    dispose();
                }
        );

        b2.addActionListener(
                a->{
//                    JOptionPane.showMessageDialog(null,"Redirecting to Add Expense Page...");
                    new AddExpense(username);
                    dispose();
                }
        );

        b3.addActionListener(
                a->{
                    new ViewExpense(username);
                    dispose();
                }
        );

        b4.addActionListener(
                a->{
                    new Analytics(username);
                    dispose();
                }
        );

        b5.addActionListener(
                a->{
//                    JOptionPane.showMessageDialog(null,"Redirecting to Category Page...");
                    new Category(username);
                    dispose();
                }
        );


        setVisible(true);
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Home Page");
    }

    public static void main(String[] args) {
        new Home("Atharv");
    }
}

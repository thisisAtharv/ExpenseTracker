package model;

import javax.swing.*;
import java.awt.*;

class Category extends JFrame {
    Category(String username){
        Font f1 = new Font("Futura",Font.BOLD,28);
        Font f2 = new Font("Segoe UI Emoji",Font.PLAIN,20);

        JLabel title = new JLabel("\uD83D\uDCC1 Manage Categories");
        title.setForeground(new Color(0, 255, 180));
        JButton b1 = new JButton("➕ Create Category");
        JButton b2 = new JButton("\uD83D\uDCCB View Categories");
        JButton b3 = new JButton("Back");
        b3.setBackground(new Color(70, 70, 70));
        b3.setForeground(new Color(200, 200, 200));
        b3.setFocusPainted(false);
        b3.setBorderPainted(false);
        b3.setOpaque(true);

        Color buttonBg = new Color(50, 50, 50);
        Color buttonFg = new Color(255, 255, 255);

        for (JButton btn : new JButton[]{b1, b2}) {
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
        }

        Container c = getContentPane();
        c.setLayout(null);

        c.add(title);
        c.add(b1);
        c.add(b2);
        c.add(b3);

        title.setFont(f1);
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);

        title.setBounds(240, 40, 400, 50);
        b1.setBounds(280,140,235,40);
        b2.setBounds(280,200,235,40);
        b3.setBounds(280,260,235,40);

        b1.addActionListener(
                a->{
                    new CreateCategory(username);
                    dispose();
                }
        );

        b2.addActionListener(
                a->{
                    new ViewCategory(username);
                    dispose();
                }
        );

        b3.addActionListener(
                a->{
                    JOptionPane.showMessageDialog(null,"Redirecting to Home Page...");
                    new Home(username);
                    dispose();
                }
        );

        setVisible(true);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Category Page");
    }

    public static void main(String[] args) {
        new Category("Atharv");
    }
}

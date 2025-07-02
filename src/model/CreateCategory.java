package model;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class CreateCategory extends JFrame {
    private Color selectedColor = Color.RED;
    CreateCategory(String username){
        Font f1 = new Font("Calibri",Font.PLAIN,28);
        Font f2 = new Font("Segoe UI Emoji",Font.PLAIN,20);

        JLabel l1 = new JLabel("Category Name:");
        JTextField t1 = new JTextField(15);
        JLabel l2 = new JLabel("Category Color:");
        JButton b1 = new JButton("Pick Color");
        b1.setForeground(new Color(255, 255, 255));
        b1.setBackground(new Color(50, 50, 50));
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setOpaque(true);
        JPanel p1 = new JPanel();
        p1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        p1.setPreferredSize(new Dimension(30,30));
        p1.setBackground(selectedColor);
        JButton b2 = new JButton("Create");
        b2.setBackground(new Color(0, 153, 102));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);
        b2.setBorderPainted(false);
        b2.setOpaque(true);
        JButton b3 = new JButton("Back");
        b3.setBackground(new Color(70, 70, 70));
        b3.setForeground(new Color(200, 200, 200));
        b3.setFocusPainted(false);
        b3.setBorderPainted(false);
        b3.setOpaque(true);

        Container c = getContentPane();
        c.setLayout(null);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(b1);
        c.add(p1);
        c.add(b2);
        c.add(b3);

        l1.setFont(f1);
        t1.setFont(f1);
        l2.setFont(f1);
        b1.setFont(f2);
        b2.setFont(f1);
        b3.setFont(f2);

        l1.setBounds(150, 100, 200, 30);
        t1.setBounds(340, 100, 200, 40);
        l2.setBounds(150, 150, 200, 30);
        b1.setBounds(340, 150, 140, 30);
        p1.setBounds(490, 150, 30, 30);
        b2.setBounds(300, 220, 200, 40);
        b3.setBounds(300,280,200,40);

        b1.addActionListener(
                a->{
                    Color color = JColorChooser.showDialog(null,"Select a Color",selectedColor);
                    if(color!=null)
                    {
                        selectedColor = color;
                        p1.setBackground(selectedColor);
                    }
                }
        );

        b2.addActionListener(
                a->{
                    String categoryName = t1.getText().trim();
                    if(categoryName.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,"Please enter a category name.");
                        return;
                    }

                    String hexCode = "#"+Integer.toHexString(selectedColor.getRGB()).substring(2).toUpperCase();

                    String url = "jdbc:mysql://localhost:3306/expense_tracker";

                    try(Connection con = DriverManager.getConnection(url,"root","Atharv@123"))
                    {
                        int userId = -1;
                        String getSql = "select id from users where username = ?";
                        try(PreparedStatement pst = con.prepareStatement(getSql))
                        {
                            pst.setString(1,username);
                            ResultSet rs = pst.executeQuery();
                            if(rs.next())
                            {
                                userId = rs.getInt("id");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"User not found!");
                                return;
                            }
                        }

                        String sql = "insert into categories(user_id,name,color) values(?, ?, ?)";
                        try(PreparedStatement pst = con.prepareStatement(sql))
                        {
                            pst.setInt(1,userId);
                            pst.setString(2,categoryName);
                            pst.setString(3,hexCode);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,"Category created successfully");
                            new CreateCategory(username);
                            dispose();
                        }
                    }
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                        return;
                    }
                }
        );

        b3.addActionListener(
                a->{
                    new Category(username);
                    dispose();
                }
        );

        setVisible(true);
        setSize(800,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Create Category");

    }

    public static void main(String[] args) {
        new CreateCategory("Atharv");
    }
}

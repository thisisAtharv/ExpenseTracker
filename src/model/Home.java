package model;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

class Home extends JFrame {
    private boolean isDarkTheme = true;
    Home(String username){
        Font f1 = new Font("Segoe UI Emoji",Font.PLAIN,20);
        Font f2 = new Font("Futura",Font.BOLD,28);
        Font f3 = new Font("Futura",Font.PLAIN,15);

        JLabel title = new JLabel("\uD83D\uDC4B Welcome, "+username+"!",JLabel.CENTER);
        title.setForeground(new Color(135, 206, 250)); // LightSkyBlue
        JButton b1 = new JButton("← Logout");
        JComboBox<String> themeBox = new JComboBox<>(new String[]{
                "🌓 FlatMacDark", "☀️ FlatMacLight", "💡 IntelliJ"
        });
        themeBox.setBackground(new Color(30, 30, 30));
        themeBox.setForeground(Color.WHITE);
        JButton b2 = new JButton("➕ Add Transaction");
        JButton b3 = new JButton("\uD83D\uDCCA View Transactions");
        JButton b4 = new JButton("\uD83D\uDCCA Analytics");
        JButton b5 = new JButton("\uD83D\uDCC1 Manage Categories");

        Color buttonBg = new Color(50, 50, 50);
        Color buttonFg = new Color(255, 255, 255);

        for (JButton btn : new JButton[]{b1, b2, b3, b4, b5}) {
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
        c.add(themeBox);
        c.add(b2);
        c.add(b3);
        c.add(b4);
        c.add(b5);

        title.setFont(f2);
        b1.setFont(f1);
        themeBox.setFont(f3);
        b2.setFont(f1);
        b3.setFont(f1);
        b4.setFont(f1);
        b5.setFont(f1);

        title.setBounds(165, 40, 400, 50);
        b1.setBounds(620, 50, 150, 30);
        themeBox.setBounds(620, 90, 151, 30);
        b2.setBounds(245,120,235,40);
        b3.setBounds(245,180,235,40);
        b4.setBounds(245,240,235,40);
        b5.setBounds(245,300,235,40);

        b1.setForeground(Color.RED);

        b1.addActionListener(
                a->{
                    JOptionPane.showMessageDialog(null,"Redirecting to Login Page...");
                    new LoginPage();
                    dispose();
                }
        );

        themeBox.addActionListener(
                a->{
                    String val = (String) themeBox.getSelectedItem();
                    setAppTheme(val);
                });

        b2.addActionListener(
                a->{
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
    public static void setAppTheme(String themeName){
        try{
            switch(themeName){
                case "🌓 FlatMacDark":
                    UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    break;
                case "☀️ FlatMacLight":
                    UIManager.setLookAndFeel(new FlatMacLightLaf());
                    break;
                case "💡 IntelliJ":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                default:
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
            for (Frame frame : JFrame.getFrames()) {
                SwingUtilities.updateComponentTreeUI(frame);
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Home("Atharv");
    }
}

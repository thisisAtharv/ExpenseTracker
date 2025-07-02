package model;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;


class Analytics extends JFrame {
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    Analytics(String username){
        Font f1 = new Font("Futura",Font.PLAIN,22);
        Font f2 = new Font("Segio UI Emoji",Font.BOLD,33);
        Font filterFont = new Font("Segio UI Emoji",Font.BOLD,18);

        JLabel title = new JLabel("📊 Analytics for " + username,JLabel.CENTER);
        title.setForeground(new Color(255, 255, 255));
        title.setBackground(new Color(0, 102, 204));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        topPanel.add(title,BorderLayout.CENTER);

        //FILTER PANEL :-
        String[] year = {"All", "2023", "2024", "2025"};
        JComboBox<String> yearBox = new JComboBox<>(year);
        yearBox.setFont(filterFont);
        String selectedYear = yearBox.getSelectedItem().toString();

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        JLabel l4 = new JLabel("Filter by Year:",JLabel.CENTER);
        l4.setFont(filterFont);
        filterPanel.add(l4);
        filterPanel.add(yearBox);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));


        //TOP mix panel :-
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(topPanel,BorderLayout.NORTH);
        topContainer.add(filterPanel,BorderLayout.SOUTH);

        //bar chart snippet :-
        //-------------

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        updatebarChart(username,selectedYear,dataset);
        JFreeChart barChart = ChartFactory.createBarChart(
                "Yearly Income & Expense",
                "Month",
                "Amount (₹)",
                dataset
        );

        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, new Color(0, 204, 102));
        renderer.setSeriesPaint(1, new Color(255, 51, 51));

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setFont(f2);
        chartPanel.setPreferredSize(new Dimension(700,400));
        chartPanel.setMouseWheelEnabled(true);

        yearBox.addActionListener(
                a->{
                    String selYear = yearBox.getSelectedItem().toString();
                    updatebarChart(username,selYear,dataset);
                }
        );

    //-------------

        JButton b1 = new JButton("Back");
        b1.setBackground(new Color(70, 70, 70));
        b1.setForeground(new Color(200, 200, 200));
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setOpaque(true);
        b1.addActionListener(
                a->{
                    JOptionPane.showMessageDialog(null,"Redirecting to Home Page...");
                    new Home(username);
                    dispose();
                }
        );

        JPanel bottomPanel = new JPanel();
//        bottomPanel.setBackground(new Color(230, 230, 230));
        bottomPanel.add(b1);

        title.setFont(f2);
        b1.setFont(f1);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(10,10));
        c.add(topContainer,BorderLayout.NORTH);
        c.add(chartPanel,BorderLayout.CENTER);
        c.add(bottomPanel,BorderLayout.SOUTH);

        setVisible(true);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Analytics Page");
    }

    private void updatebarChart(String username,String selectedYear, DefaultCategoryDataset dataset){
        dataset.clear();

        for(String type : new String[]{"Income","Expense"}){
            for(String monthName : months){
                dataset.setValue(0.0,type,monthName);
            }
        }

        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
            String sql = """
        SELECT MONTH(date) AS months, type, SUM(amount) AS total
        FROM transactions
        WHERE username = ? AND (? = 'All' OR YEAR(date) = ?)
        GROUP BY MONTH(date), type
        ORDER BY MONTH(date)
    """;
            try(PreparedStatement pst = con.prepareStatement(sql)){
                pst.setString(1,username);
                pst.setString(2,selectedYear);
                pst.setInt(3, selectedYear.equals("All") ? 0 : Integer.parseInt(selectedYear));
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    int month = rs.getInt("months");
                    String monthName = months[month-1];
                    String type = rs.getString("type");
                    double total = rs.getDouble("total");

                    dataset.setValue(total,type.substring(0,1).toUpperCase()+type.substring(1),monthName);
                }
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
            return;
        }
    }

    public static void main(String[] args) {
        new Analytics("Atharv");
    }
}

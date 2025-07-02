package model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class ViewCategory extends JFrame {
    ArrayList<Integer> categoryIds = new ArrayList<>();
    ViewCategory(String username){

        Font f1 = new Font("Futura",Font.BOLD,25);
        Font f2 = new Font("Segoe UI Emoji",Font.PLAIN,18);

        JLabel title = new JLabel("Categories for "+username+" :",JLabel.CENTER);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(new Color(255,255,255));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] columnNames = {"C_Name","Color","Save","Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return column == 0 || column == 1;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Calibri",Font.BOLD,18));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(224,224,224));

        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                String hexColor = (String)table.getValueAt(row,1);
                try{
                    c.setForeground(Color.decode(hexColor));
                }
                catch (Exception e)
                {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }

        });

        JButton b1 = new JButton("Back");
        b1.setBackground(new Color(70, 70, 70));
        b1.setForeground(new Color(200, 200, 200));
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setOpaque(true);
        b1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        b1.addActionListener(
                a->{
                    new Category(username);
                    dispose();
                }
        );


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.add(title, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
//        bottomPanel.setBackground(new Color(224,224,224));
        bottomPanel.add(b1);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(20,20));
        c.add(topPanel,BorderLayout.NORTH);
        c.add(scrollPane,BorderLayout.CENTER);
        c.add(bottomPanel,BorderLayout.SOUTH);

        title.setFont(f1);
        table.setFont(f2);
        b1.setFont(f2);
        //Get the user _id.
        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        try(Connection con =  DriverManager.getConnection(url,"root","Atharv@123"))
        {
            int userId = -1;
            String getUserid = "select id from users where username = ?";
            try(PreparedStatement pst = con.prepareStatement(getUserid))
            {
                pst.setString(1,username);
                try(ResultSet rs = pst.executeQuery()){
                    if(rs.next())
                    {
                        userId = rs.getInt("id");
                    }
                }
            }
            //Fetch the categories.
            if(userId!=-1)
            {
                String sql = "select id,name,color from categories where user_id = ?";
                try(PreparedStatement pst = con.prepareStatement(sql))
                {
                    pst.setInt(1,userId);
                    try(ResultSet rs = pst.executeQuery())
                    {
                        while(rs.next())
                        {
                            String name = rs.getString("name");
                            String color = rs.getString("color");
                            categoryIds.add(rs.getInt("id"));
                            tableModel.addRow(new Object[]{name,color,"\uD83D\uDCBE Save","❌ Delete"});
                        }
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"User not found");
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return;
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                int categoryId = categoryIds.get(row);

                if(col==2){ //Save_Column
                    String newName = table.getValueAt(row,0).toString().trim();
                    String newColor = table.getValueAt(row,1).toString().trim();
                    if(newName.isEmpty() || newColor.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,"Fields cannot be empty");
                        return;
                    }
                    try(Connection con = DriverManager.getConnection(url,"root","Atharv@123"))
                    {
                        String sql = "UPDATE categories SET name=?, color=? WHERE id=?";
                        try(PreparedStatement pst = con.prepareStatement(sql))
                        {
                            pst.setString(1,newName);
                            pst.setString(2,newColor);
                            pst.setInt(3,categoryId);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,"Category updated.");
                        }
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }

                } else if(col==3){ //Delete_Column
                    int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this category?");
                    if(confirm == JOptionPane.YES_OPTION){
                        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
                            String delSql = "DELETE FROM categories where id=?";
                            try(PreparedStatement pst = con.prepareStatement(delSql)){
                                pst.setInt(1,categoryId);
                                pst.executeUpdate();
                                ((DefaultTableModel)table.getModel()).removeRow(row);
                                categoryIds.remove(row);
                                JOptionPane.showMessageDialog(null,"Category deleted.");
                            }
                        }
                        catch(Exception exc){
                            JOptionPane.showMessageDialog(null,exc.getMessage());
                        }
                    }
                }
            }
        });

        setVisible(true);
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("View Category");
    }

    public static void main(String[] args) {
        new ViewCategory("Atharv");

    }
}

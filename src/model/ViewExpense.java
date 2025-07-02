package model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.View;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

class ViewExpense extends JFrame {
    String url = "jdbc:mysql://localhost:3306/expense_tracker";
    DefaultTableModel tableModel;
    JTable table;
    String username;
    ViewExpense(String username){
        this.username = username;
        Font f1 = new Font("Futura",Font.BOLD,30);
        Font f2 = new Font("Segeo UI Emoji",Font.PLAIN,18);

        JLabel title = new JLabel("Transactions for "+username,JLabel.CENTER);
        title.setForeground(new Color(255, 255, 255));
        title.setBackground(new Color(0, 102, 204));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.add(title,BorderLayout.CENTER);

        JLabel warningLabel = new JLabel();
        warningLabel.setForeground(Color.RED);


        String[] columnNames = {"Category","Description","Amount","Date & Time","Type","id"};
        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setFont(new Font("Calibri",Font.BOLD,20));
        table.setGridColor(new Color(224,224,224));

        JScrollPane scrollPane = new JScrollPane(table);

        HashMap<String,String> categoryColors = new HashMap<>();

        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
            String loadHex = "select name,color from categories where user_id = (select id from users where username = ?)";
            try(PreparedStatement pst = con.prepareStatement(loadHex))
            {
                pst.setString(1,username);
                ResultSet rsn = pst.executeQuery();
                while(rsn.next())
                {
                    String s1 = rsn.getString("name");
                    String s2 = rsn.getString("color");
                    categoryColors.put(s1.toLowerCase(),s2);
                }
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return;
        }

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                String catName = value.toString().toLowerCase();
                String hexColor = categoryColors.getOrDefault(catName, "#000000");
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
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component co = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                String type = table.getValueAt(row,4).toString().toLowerCase();
                try{
                    if (type.equals("income")) co.setForeground(new Color(50, 225, 50));
                    else co.setForeground(Color.RED);
                }
                catch (Exception ex)
                {
                    co.setForeground(Color.BLACK);
                }
                return co;
            }
        });

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        JLabel l5 = new JLabel("Type:");
        String[]transTypes = {"All","Income","Expense"};
        JComboBox<String> typeFilter = new JComboBox<>(transTypes);
        JLabel l6 = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        filterPanel.add(l5);
        filterPanel.add(typeFilter);
        filterPanel.add(l6);
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);

        typeFilter.addActionListener(
                a->{
                    applyFilters(typeFilter.getSelectedItem().toString(),searchField.getText());
                }
        );

        searchBtn.addActionListener(
                a->{
                    applyFilters(typeFilter.getSelectedItem().toString(),searchField.getText());
                }
        );


        JLabel incLabel = new JLabel("Total Income: ₹0.00");
        JLabel expLabel = new JLabel("Total Expense: ₹0.00");
        JLabel balLabel = new JLabel("Current Balance: ₹0.00");

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,16));
        summaryPanel.add(incLabel);
        summaryPanel.add(expLabel);
        summaryPanel.add(balLabel);
        summaryPanel.add(warningLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(1,20,10,20));
        centerPanel.add(summaryPanel,BorderLayout.SOUTH);
        centerPanel.add(filterPanel,BorderLayout.NORTH);
        centerPanel.add(scrollPane,BorderLayout.CENTER);


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
        JButton b2 = new JButton("Edit");
        b2.setBackground(new Color(0, 153, 102));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);
        b2.setBorderPainted(false);
        b2.setOpaque(true);
        b2.addActionListener(
                a->{
                    int row = table.getSelectedRow();
                    if(row==-1){
                        JOptionPane.showMessageDialog(null,"Please select a transaction to edit.");
                        return;
                    }
                    int modelRow = table.convertRowIndexToModel(row);
                    int selectedRowId = Integer.parseInt(tableModel.getValueAt(modelRow, 5).toString());
                    String oldCategory = table.getValueAt(row,0).toString();
                    String oldDesc = table.getValueAt(row,1).toString();
                    String oldAmount = table.getValueAt(row,2).toString();
                    String oldDateTime = table.getValueAt(row,3).toString();
                    String oldType = table.getValueAt(row,4).toString();


                    String newDesc = JOptionPane.showInputDialog(null,"Edit Description:",oldDesc);
                    String newAmountstr = JOptionPane.showInputDialog(null,"Edit Amount:",oldAmount);
                    String newDateTime = JOptionPane.showInputDialog(null,"Edit Date & Time (Format: yyyy-MM-dd HH:mm:ss):",oldDateTime);
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setLenient(false);
                        sdf.parse(newDateTime);
                    }
                    catch (ParseException ex){
                        JOptionPane.showMessageDialog(null,"Invalid Date-Time format. Use yyyy-MM-dd HH:mm:ss");
                        return;

                    }
                    String[] types = {"Income","Expense"};
                    String newType = (String)JOptionPane.showInputDialog(null,"Select Type:","Edit Type:",JOptionPane.QUESTION_MESSAGE,null,types,oldType);

                    String[] categoryArray = categoryColors.keySet().toArray(new String[0]);
                    String newCategory = (String) JOptionPane.showInputDialog(null,"Select Category:","Edit Category",JOptionPane.QUESTION_MESSAGE,null,categoryArray,oldCategory);

                    if(newCategory!=null && newDesc!=null && newAmountstr != null && newDateTime!= null && newType!=null){
                        try (Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
                            String query = "UPDATE transactions SET category_name = ?, description = ?, amount = ?, date = ?, type = ? WHERE id = ? AND username = ?";
                            try(PreparedStatement pst = con.prepareStatement(query)){
                                pst.setString(1, newCategory);
                                pst.setString(2, newDesc);
                                pst.setDouble(3, Double.parseDouble(newAmountstr));
                                pst.setString(4, newDateTime);
                                pst.setString(5, newType.toLowerCase());
                                pst.setInt(6, selectedRowId);
                                pst.setString(7,username);
                                int updated = pst.executeUpdate();
                                if(updated>0){
                                    JOptionPane.showMessageDialog(null,"Transaction Updated.");
                                    dispose();
                                    new ViewExpense(username);
                                }
                            }
                        }
                        catch (Exception ex){
                            JOptionPane.showMessageDialog(null,ex.getMessage());
                            return;
                        }
                        }
                    }

        );
        JButton b3 = new JButton("Delete");
        b3.setBackground(new Color(232, 66, 66));
        b3.addActionListener(
                a->{
                    int row = table.getSelectedRow();
                    if(row==-1){
                        JOptionPane.showMessageDialog(null,"Please select a transaction to delete.");
                        return;
                    }
                    int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this transaction ?","Confirm Delete",JOptionPane.YES_OPTION);
                    if (confirm == JOptionPane.YES_OPTION){
                        int id = (int) tableModel.getValueAt(row,5);
                        deleteTransactionbyID(id);
                        tableModel.removeRow(row);
                        JOptionPane.showMessageDialog(null,"Transaction deleted successfully!");
                    }

                }
        );


        JPanel btmPanel = new JPanel();
//        btmPanel.setBackground(new Color(51, 50, 50));
        btmPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        btmPanel.add(b1);
        btmPanel.add(b2);
        btmPanel.add(b3);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(20,20));
        c.add(topPanel,BorderLayout.NORTH);
        c.add(centerPanel,BorderLayout.CENTER);
        c.add(btmPanel,BorderLayout.SOUTH);

        title.setFont(f1);
        table.setFont(f2);
        incLabel.setFont(f2);
        expLabel.setFont(f2);
        balLabel.setFont(f2);
        warningLabel.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);


        double totalincome = 0;
        double totalexpense = 0;

        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
            String loadSql = "select id,category_name,description,amount,date,type from transactions where username = ?";
            try(PreparedStatement pst = con.prepareStatement(loadSql))
            {
                pst.setString(1,username);
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    int id = rs.getInt("id");
                    String name = rs.getString("category_name");
                    String displayName = name.substring(0,1).toUpperCase() + name.substring(1);
                    String desc = rs.getString("description");
                    double amount = rs.getDouble("amount");
                    String date = rs.getString("date");
                    String type = rs.getString("type");
                    type = type.substring(0,1).toUpperCase()+type.substring(1);

                    if(type.equals("Income")){
                        totalincome+=amount;
                    }
                    else if (type.equals("Expense")){
                        totalexpense+=amount;
                    }

                    String amountStr = String.format("%.2f", amount);

                    tableModel.addRow(new Object[]{displayName,desc,amountStr,date,type,id});

                }
                double balance = totalincome - totalexpense;
                double minBalance = 0.0;
                incLabel.setText("Total Income: ₹" + String.format("%.2f", totalincome));
                expLabel.setText("Total Expense: ₹" + String.format("%.2f", totalexpense));
                balLabel.setText("Current Balance: ₹" + String.format("%.2f", balance));
                if (balance < minBalance) {
                    warningLabel.setText("⚠️ Warning: You are over budget!");
                }
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return;
        }

        setVisible(true);
        setSize(1107,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("View Expenses Page");
    }
    private void deleteTransactionbyID(int id){
        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
            String deleteSql = "DELETE from transactions where id = ?";
            try(PreparedStatement pst = con.prepareStatement(deleteSql)){
                pst.setInt(1,id);
                pst.executeUpdate();
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error while deleting: " + ex.getMessage());
        }
    }

    void applyFilters(String type,String keyword){
        tableModel.setRowCount(0);
        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123")){
            String sql = "SELECT category_name, description, amount, date, type FROM transactions WHERE username = ?";
            if(!type.equals("All")){
                sql+="AND type = ?";
            }
            if(!keyword.trim().isEmpty()){
                sql+=" AND (category_name LIKE ? OR description LIKE ?)";
            }
            try(PreparedStatement pst = con.prepareStatement(sql)){
                int i=1;
                pst.setString(i++,username);
                if(!type.equals("All")){
                    pst.setString(i++,type.toLowerCase());
                }
                if (!keyword.trim().isEmpty()) {
                    pst.setString(i++, "%" + keyword + "%");
                    pst.setString(i++, "%" + keyword + "%");
                }
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    String cat = rs.getString("category_name");
                    String desc = rs.getString("description");
                    double amt = rs.getDouble("amount");
                    String date = rs.getString("date");
                    String t = rs.getString("type");

                    tableModel.addRow(new Object[]{
                            cat.substring(0,1).toUpperCase()+cat.substring(1),
                            desc,
                            String.format("%.2f", amt),
                            date,
                            t.substring(0,1).toUpperCase()+t.substring(1)
                    });
                }
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewExpense("Atharv");
    }
}

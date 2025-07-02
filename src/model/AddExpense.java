package model;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

class AddExpense extends JFrame {
    AddExpense(String username){
        Font f1 = new Font("Futura",Font.BOLD,40);
        Font f2 = new Font("Segeo UI Emoji",Font.PLAIN,22);

        JLabel title = new JLabel("Add Transactions",JLabel.CENTER);
        title.setForeground(new Color(0, 255, 180));
        JLabel l3 = new JLabel("Category:");
        JComboBox<CategoryItem> cbCategory = new JComboBox<>();
        JLabel l4 = new JLabel("Type:");
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Expense","Income"});
        JLabel l1 = new JLabel("Enter Amount:");
        JTextField t1 = new JTextField(10);
        JLabel l2 = new JLabel("Description:");
        JTextField t2 = new JTextField(15);
        JButton subBtn = new JButton("Submit");
        subBtn.setBackground(new Color(0, 153, 102));
        subBtn.setForeground(Color.WHITE);
        subBtn.setFocusPainted(false);
        subBtn.setBorderPainted(false);
        subBtn.setOpaque(true);
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(70, 70, 70));
        backBtn.setForeground(new Color(200, 200, 200));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);

        title.setFont(f1);
        l3.setFont(f2);
        cbCategory.setFont(f2);
        l4.setFont(f2);
        cbType.setFont(f2);
        l1.setFont(f2);
        t1.setFont(f2);
        l2.setFont(f2);
        t2.setFont(f2);
        subBtn.setFont(f2);
        backBtn.setFont(f2);

        title.setBounds(240, 20, 330, 40);
        l3.setBounds(200, 100, 200, 30);
        cbCategory.setBounds(400, 100, 200, 30);
        l4.setBounds(200, 160, 200, 30);
        cbType.setBounds(400, 160, 200, 30);
        l1.setBounds(200, 220, 200, 30);
        t1.setBounds(400, 220, 200, 30);
        l2.setBounds(200,280,300,30);
        t2.setBounds(400,280,200,30);
        subBtn.setBounds(300,380,200,40);
        backBtn.setBounds(300, 440, 200, 40);


        Container c = getContentPane();
        c.setLayout(null);
        c.add(title);
        c.add(l3);
        c.add(cbCategory);
        c.add(l4);
        c.add(cbType);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(t2);
        c.add(subBtn);
        c.add(backBtn);

        cbCategory.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if(value instanceof CategoryItem){
                    CategoryItem item = (CategoryItem) value;
                    try{
                        c.setForeground(Color.decode(item.colorHex));
                    }catch(Exception e)
                    {
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });
        cbType.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                Component co = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                try{
                    if(value.equals("Income")){
                        co.setForeground(new Color(50, 225, 50));
                    }
                    else{
                        co.setForeground(Color.RED);
                    }
                }
                catch (Exception e){
                    co.setForeground(Color.BLACK);
                }
                return co;
            }
        });

        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        try(Connection con = DriverManager.getConnection(url,"root","Atharv@123"))
        {
            String sql = "select name,color from categories where user_id = (select id from users where username = ?)";
            try(PreparedStatement pst = con.prepareStatement(sql))
            {
                pst.setString(1,username);
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    String name = rs.getString("name");
                    String color = rs.getString("color");
                    cbCategory.addItem(new CategoryItem(name, color));
                }
                con.close();
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return;
        }
        subBtn.addActionListener(
                a->{
                    String type = cbType.getSelectedItem().toString().toLowerCase();
                    CategoryItem selected = (CategoryItem) cbCategory.getSelectedItem();
                    String cat_name = selected.name.toLowerCase();
                    String desc = t2.getText().trim();
                    String s1 = t1.getText().trim();
                    double balance = 0.0;
                    if (desc.isEmpty() || s1.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,"Fields Cannot be Empty.");
                        return;
                    }
                    double amount;
                    try{
                        amount = Double.parseDouble(s1);
                        if(amount<=0){
                            JOptionPane.showMessageDialog(null,"Amount must be positive");
                            return;
                        }
                    }
                    catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(null,"Please enter a valid number for amount.");
                        return;
                    }
                    double total = amount + balance;
                    double result = balance - amount;
                    try(Connection con = DriverManager.getConnection(url,"root","Atharv@123"))
                    {
                        String sql = "INSERT into transactions(username,category_name,description,amount,type) values(?, ?, ?, ?, ?)";
                        try(PreparedStatement pst = con.prepareStatement(sql))
                        {
                            pst.setString(1,username);
                            pst.setString(2,cat_name);
                            pst.setString(3,desc);
                            pst.setDouble(4,amount);
                            pst.setString(5,type);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,"Successfully Transaction Added");
                            t1.setText("");
                            t2.setText("");
                        }
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                        return;
                    }
                }
        );

        backBtn.addActionListener(
                a->{
                    JOptionPane.showMessageDialog(null,"Redirecting to Home page...");
                    new Home(username);
                    dispose();
                }
        );

        setVisible(true);
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Add Expense Page");
    }

    public static void main(String[] args) {
        new AddExpense("Atharv");
    }

}

class CategoryItem
{
    String name;
    String colorHex;
    CategoryItem(String name, String colorHex)
    {
        this.name = name;
        this.colorHex = colorHex;
    }
    @Override
    public String toString(){
        return name;
    }
}
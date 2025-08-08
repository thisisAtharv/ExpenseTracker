# 💸 Smart Expense Tracker

[![Java](https://img.shields.io/badge/Built%20With-Java-blue.svg)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-orange.svg)](https://www.mysql.com/)
[![UI](https://img.shields.io/badge/UI-Java%20Swing-green.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Smart Expense Tracker is a desktop application built with Java Swing and MySQL that lets you record, categorize, and analyze income and expenses. Features include category management with colors, transaction filtering & search, year-wise analytics with bar charts (JFreeChart), and a customizable FlatLaf theme for a modern UI.

---

## 📷 Preview

![Login Page](assets/login.png)  
_User login screen._  

![Register Page](assets/register.png)  
_New user registration form._  

![Home Page](assets/homepage.png)  
_Main dashboard with theme customization using FlatLaf.
Easily switch between themes via the dropdown, alongside quick navigation to core features._  

![Add Expense Page](assets/addexpense.png)  
_Form to add a new income or expense entry._  

![Transactions Page](assets/transactions.png)  
_Page showing a list of all income/expense transactions with filter, search, update and delete transactions features._  

![Analytics Page](assets/Analytics.png)  
_Analytics view with month-wise income and expense bar chart. Also you can filter by year._  

![Create Category Page](assets/createcategory.png)  
_Form to create a new category with a name and color._  

![Manage Categories Page](assets/managecategory.png)  
_View, edit, or delete existing categories._  


---

## 🧠 Features

- ✅ User Registration & Secure Login
- ✅ Add/Edit/Delete Transactions
- ✅ Manage Custom Categories (Name + Color)
- ✅ Income/Expense Summary Panel
- ✅ Real-time Minimum Balance Warning
- ✅ Monthly Analytics with Bar Charts (via JFreeChart)
- ✅ Filter & Search Transactions by Type/Keyword
- ✅ Clean, intuitive Swing UI with modern layout

---

## 🗃️ Tech Stack

| Layer        | Technology             |
|-------------|------------------------|
| 💻 Language  | Java (JDK 17+)         |
| 🖥️ UI       | Java Swing (AWT/Swing) |
| 🗄️ Database | MySQL 8.x              |
| 📊 Charts   | JFreeChart              |

---

## ⚙️ Database Schema

### 🔐 `users` Table
```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100),
  password VARCHAR(100) NOT NULL
);
```
### 🗂️  `categories` Table
```sql
CREATE TABLE categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  color VARCHAR(7) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```
### 💳 `transactions` Table
```sql
CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  category_name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  amount DECIMAL(10,2) NOT NULL,
  date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  type ENUM('income','expense') NOT NULL DEFAULT 'expense'
);
```

## 🚀 Getting Started

### 1. Clone the Repo
```bash
git clone https://github.com/thisisAtharv/ExpenseTracker.git
cd ExpenseTracker
```
### 2. Configure MySQL
- Create a database: expense_tracker
- Run the provided SQL script (schema.sql) or create tables manually (see above)
### 3. Update DB Credentials
Open your relevant .java files and update the database connection info:
```java
String url = "jdbc:mysql://localhost:3306/expense_tracker";
String user = "root";
String password = "your_password";
```
### 4. Build & Run
- Compile the project using your IDE (e.g., IntelliJ, Eclipse) or the terminal
- Run the Main.java or Login.java class to launch the application

## 📂 Project Structure
```bash
src/
├── db/
│   └── DBConnection.java
├── model/
│   ├── AddExpense.java
│   ├── Analytics.java
│   ├── Category.java
│   ├── CreateCategory.java
│   ├── Home.java
│   ├── LoginPage.java
│   ├── RegisterPage.java
│   ├── ViewCategory.java
│   └── ViewExpense.java
```

## 📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 🙌 Acknowledgements
- [Java Swing Documentation – Oracle](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html)
- [JFreeChart – Official Site](http://www.jfree.org/jfreechart/)

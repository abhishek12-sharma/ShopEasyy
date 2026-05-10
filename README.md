# ShopEasy — E-Commerce Desktop Application

A full-featured desktop shopping application built with **Java Swing**, **JDBC**, and **MySQL** that simulates a real-world online shopping system.

---

## Features

- **User Authentication** — Login and Signup with validation
- **Home Page** — Auto sliding banner with product sections
- **Product Browsing** — Browse by category (Electronics, Clothing, Books, Sports, Home & Kitchen)
- **Search** — Search products by name
- **Category Filter** — Filter products using dropdown
- **Product Sections** — Popular, For Men, For Women, Books & Learning
- **Shopping Cart** — Add, remove items and view total
- **Order Management** — Place orders and track status (Active/Completed/Cancelled)
- **Profile Page** — View and update profile information
- **Order History** — View all past orders with item details

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java Swing | Desktop GUI |
| JDBC | Database Connectivity |
| MySQL | Database |
| XAMPP | Local Server |

---

## Project Structure
ShopEasy/
├── src/
│   ├── ui/          → All UI Screens
│   ├── dao/         → Database Access Objects
│   ├── model/       → Data Models
│   └── Main.java    → Entry Point
├── lib/             → MySQL Connector JAR
├── images/          → Product Images
├── shop_easy.sql    → Database File
├── run.bat          → Compile and Run
└── Launch.bat       → Launch App

---

## Requirements

- Java JDK 11 or higher
- XAMPP (MySQL + Apache)
- MySQL Connector/J JAR

---

## Setup Instructions

**Step 1 — Clone Repository**
```bash
git clone https://github.com/ayushdwivedi1280/ShopEasy.git
```

**Step 2 — Install XAMPP**

Download from [apachefriends.org](https://www.apachefriends.org) and install.

**Step 3 — Import Database**

- Open XAMPP and Start MySQL and Apache
- Open phpMyAdmin at http://localhost/phpmyadmin
- Create new database named `shop_easy`
- Click Import, select `shop_easy.sql` and click Go

**Step 4 — Run Application**

Double click `run.bat` or run in terminal:
```bash
javac -cp "lib/mysql-connector-j-9.7.0.jar" src/dao/*.java src/model/*.java src/ui/*.java src/Main.java
java -cp "lib/mysql-connector-j-9.7.0.jar;src" Main
```

---

## Database Structure

| Table | Purpose |
|---|---|
| users | User accounts |
| products | Product catalog |
| categories | Product categories |
| cart | Shopping cart items |
| orders | Placed orders |
| order_items | Items inside each order |

---

## App Flow
Login / Signup
↓
Home Page → Browse Products
↓
Add to Cart
↓
Checkout → Order Placed
↓
Profile → Order History → Track Status

---

## Author

**Ayush Dwivedi**
GitHub: [ayushdwivedi1280](https://github.com/ayushdwivedi1280)

---

## License

This project is licensed under the MIT License.

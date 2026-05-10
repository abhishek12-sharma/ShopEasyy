-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 10, 2026 at 12:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shop_easy`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`id`, `user_id`, `product_id`, `quantity`) VALUES
(26, 3, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Electronics'),
(2, 'Clothing'),
(3, 'Books'),
(4, 'Beauty'),
(5, 'Sports'),
(6, 'Home & Kitchen');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT 'Active',
  `order_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `total_amount`, `status`, `order_date`) VALUES
(1, 1, 3597.00, 'Cancelled', '2026-05-10 01:38:00'),
(2, 1, 2999.00, 'Cancelled', '2026-05-10 01:38:23'),
(3, 1, 45999.00, 'Active', '2026-05-10 01:39:47'),
(4, 1, 3598.00, 'Active', '2026-05-10 08:28:25');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(150) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `price`, `quantity`) VALUES
(1, 1, 2, 'Laptop Stand', 599.00, 1),
(2, 1, 12, 'Wireless Earbuds', 1499.00, 2),
(3, 2, 11, 'Smart Watch', 2999.00, 1),
(4, 3, 6, 'Samsung TV', 45999.00, 1),
(5, 4, 11, 'Smart Watch', 2999.00, 1),
(6, 4, 45, 'Women Tops', 599.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT 0,
  `section` varchar(50) DEFAULT 'NULL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `image_path`, `category_id`, `stock`, `section`) VALUES
(1, 'Wireless Headphones', 1299.00, 'images/headphones.jpg', 1, 10, 'popular'),
(2, 'Laptop Stand', 599.00, 'images/stand.jpg', 1, 15, 'popular'),
(3, 'Cotton T-Shirt', 199.00, 'images/tshirt.jpg', 2, 50, 'men'),
(4, 'Java Book', 449.00, 'images/book.jpg', 3, 20, 'books'),
(5, 'iPhone 15', 79999.00, 'images/iphone.jpg', 1, 5, 'popular'),
(6, 'Samsung TV', 45999.00, 'images/tv.jpg', 1, 8, 'popular'),
(7, 'Nike Shoes', 3999.00, 'images/shoes.jpg', 2, 20, 'men'),
(8, 'Jeans', 1299.00, 'images/jeans.jpg', 2, 30, 'men'),
(9, 'Python Book', 549.00, 'images/python.jpg', 3, 15, 'books'),
(10, 'Data Science Book', 699.00, 'images/ds.jpg', 3, 12, 'books'),
(11, 'Smart Watch', 2999.00, 'images/watch.jpg', 1, 10, 'popular'),
(12, 'Wireless Earbuds', 1499.00, 'images/earbuds.jpg', 1, 15, 'popular'),
(13, 'Men Jacket', 1999.00, 'images/jacket.jpg', 2, 20, 'men'),
(14, 'Men Formal Shirt', 899.00, 'images/shirt.jpg', 2, 30, 'men'),
(15, 'Women Dress', 1299.00, 'images/dress.jpg', 2, 25, 'women'),
(16, 'Women Handbag', 1599.00, 'images/handbag.jpg', 2, 20, 'women'),
(17, 'React JS Book', 599.00, 'images/react.jpg', 3, 10, 'books'),
(18, 'Gaming Mouse', 899.00, 'images/mouse.jpg', 1, 30, 'popular'),
(19, 'Apple iPad', 45999.00, 'images/ipad.jpg', 1, 8, 'popular'),
(20, 'Sony Camera', 55999.00, 'images/camera.jpg', 1, 5, 'popular'),
(21, 'Gaming Keyboard', 1999.00, 'images/keyboard.jpg', 1, 20, 'popular'),
(22, 'Power Bank', 1299.00, 'images/powerbank.jpg', 1, 25, 'popular'),
(23, 'USB Hub', 799.00, 'images/usbhub.jpg', 1, 30, 'popular'),
(24, 'Moisturizer', 699.00, 'images/moist.jpg', 4, 20, 'NULL'),
(25, 'Lakme Face Wash', 299.00, 'images/lakme.jpg', 4, 50, NULL),
(26, 'Mamaearth Vitamin C Serum', 599.00, 'images/serum.jpg', 4, 40, NULL),
(27, 'Nivea Body Lotion', 349.00, 'images/lotion.jpg', 4, 60, NULL),
(28, 'Maybelline Lipstick', 499.00, 'images/lipstick.jpg', 4, 35, NULL),
(29, 'Minimalist Sunscreen SPF 50', 449.00, 'images/sunscreen.jpg', 4, 45, NULL),
(30, 'Dot & Key Moisturizer', 525.00, 'images/moistu.jpg', 4, 30, NULL),
(31, 'Mamaearth Onion Hair Oil', 399.00, 'images/hairoil.jpg', 4, 50, NULL),
(32, 'Biotique Face Pack', 275.00, 'images/facepack.jpg', 4, 25, NULL),
(33, 'Foxtale Cleanser', 399.00, 'images/cleanser.jpg', 4, 30, NULL),
(34, 'Plum Green Tea Toner', 450.00, 'images/toner.jpg', 4, 20, NULL),
(35, 'Hair Serum', 499.00, 'images/hserum.jpg', 4, 35, NULL),
(36, 'Men Sports Shoes', 2499.00, 'images/sports-shoes.jpg', 2, 15, 'men'),
(37, 'Men Casual Shirt', 699.00, 'images/casual-shirt.jpg', 2, 25, 'men'),
(38, 'Men Shorts', 499.00, 'images/shorts.jpg', 2, 30, 'men'),
(39, 'Women Kurti', 799.00, 'images/kurti.jpg', 2, 20, 'women'),
(40, 'Women Saree', 1999.00, 'images/saree.jpg', 2, 15, 'women'),
(41, 'Women Sandals', 899.00, 'images/sandals.jpg', 2, 25, 'women'),
(42, 'DSA Book', 649.00, 'images/dsa.jpg', 3, 20, 'books'),
(43, 'System Design Book', 749.00, 'images/system.jpg', 3, 15, 'books'),
(44, 'AI & ML Book', 849.00, 'images/aiml.jpg', 3, 10, 'books'),
(45, 'Women Tops', 599.00, 'images/tops.jpg', 2, 25, 'women'),
(46, 'Women Leggings', 449.00, 'images/leggings.jpg', 2, 30, 'women'),
(47, 'Cricket Bat', 1299.00, 'images/bat.jpg', 5, 15, NULL),
(48, 'Football', 599.00, 'images/football.jpg', 5, 20, NULL),
(49, 'Badminton Racket', 799.00, 'images/racket.jpg', 5, 25, NULL),
(50, 'Yoga Mat', 499.00, 'images/yoga.jpg', 5, 30, NULL),
(51, 'Dumbbells Set', 1999.00, 'images/dumbbells.jpg', 5, 10, NULL),
(52, 'Cycling Helmet', 899.00, 'images/helmet.jpg', 5, 12, NULL),
(53, 'Air Fryer', 3999.00, 'images/airfryer.jpg', 6, 8, NULL),
(54, 'Coffee Maker', 2499.00, 'images/coffee.jpg', 6, 10, NULL),
(55, 'Blender', 1499.00, 'images/blender.jpg', 6, 15, NULL),
(56, 'Dinner Set', 1299.00, 'images/dinnerset.jpg', 6, 20, NULL),
(57, 'Microwave', 8999.00, 'images/microwave.jpg', 6, 5, NULL),
(58, 'Rice Cooker', 1799.00, 'images/ricecooker.jpg', 6, 12, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`) VALUES
(1, 'Ayush Dwivedi', 'ayushdwivedi3008@gmail.com', '1234'),
(2, 'Abhishek Sharma', 'amansharma120704@gmail.com', '123456789'),
(3, 'av', 'abcd', '2999');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

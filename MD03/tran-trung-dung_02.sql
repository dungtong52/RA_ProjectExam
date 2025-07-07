CREATE DATABASE `book_management_db`;
USE `book_management_db`;

--
-- PHẦN 2: Thiết kế cơ sở dữ liệu
--
-- 1. Thiết kế cơ sở dữ liệu theo ERD về quản lý các đơn sách online.
CREATE TABLE `books`(
	`book_id` VARCHAR(10),
    `name` VARCHAR(100),
    `author` VARCHAR(100),
    `genre` VARCHAR(50),
    `year` INT,
    `price` FLOAT,
    PRIMARY KEY (`book_id`)
);
CREATE TABLE `customers`(
	`customer_id` VARCHAR(10),
    `customer_full_name` VARCHAR(150) NOT NULL,
    `customer_email` VARCHAR(255) UNIQUE,
    `customer_phone` VARCHAR(15) UNIQUE,
    `customer_dob` DATE,
    PRIMARY KEY (`customer_id`)
);
CREATE TABLE `orders`(
	`order_id` INT AUTO_INCREMENT,
    `customer_id` VARCHAR(10) NOT NULL,
    `book_id` VARCHAR(10) NOT NULL,
    `order_date` DATE,
    `order_status` ENUM('Delivered', 'Cancelled', 'Processing'),
    PRIMARY KEY (`order_id`),
    CONSTRAINT `fk_customers_orders`
    FOREIGN KEY (`customer_id`)
    REFERENCES `customers`(`customer_id`)
    ON DELETE CASCADE,
    CONSTRAINT `fk_books_orders`
    FOREIGN KEY (`book_id`)
    REFERENCES `books`(`book_id`)
    ON DELETE CASCADE
);
CREATE TABLE `payment`(
	`payment_id` INT AUTO_INCREMENT,
    `order_id` INT NOT NULL,
    `payment_method` ENUM('Credit Card', 'Bank Transfer', 'Cash', 'E-wallet'),
    `payment_amount` FLOAT,
    `payment_date` DATE,
    `payment_status` ENUM('Success', 'Failed', 'Pending'),
    PRIMARY KEY (`payment_id`),
    CONSTRAINT `fk_orders_payment`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders`(`order_id`)
    ON DELETE CASCADE
);
-- 2. Thêm cột gender có kiểu dữ liệu là enum với các giá trị 'Nam', 'Nữ', 'Khác' trong bảng customer.
ALTER TABLE `customers`
ADD COLUMN `gender` ENUM('Nam', 'Nữ', 'Khác');

-- 3. Thêm cột quantity trong bảng order có kiểu dữ liệu là integer, có rằng buộc NOT NULL và giá trị mặc định là 1. 
-- Cột này thể hiện số lượng sách đã đặt trong đơn hàng đã đặt.
ALTER TABLE `orders`
ADD COLUMN `quantity` INT NOT NULL DEFAULT 1;

-- 4. Thêm rằng buộc cho cột payment_amount trong bảng Payment phải có giá trị lớn hơn 0 và có kiểu dữ liệu là DECIMAL(10, 2).
ALTER TABLE `payment`
MODIFY `payment_amount` DECIMAL(10, 2)
CONSTRAINT `rb_payment_amount` CHECK (`payment_amount` > 0);

--
-- PHẦN 3: Thao tác với dữ liệu các bảng
--
-- 1. Thêm dữ liệu vào các bảng theo yêu cầu
INSERT INTO `books`(`book_id`, `name`, `author`, `genre`, `year`, `price`)
VALUES
('B001', 'Learn SQL', 'John Smith', 'Education', 2019, 45.5),
('B002', 'Python Basics', 'Jane Doe', 'Programming', 2020, 55.0),
('B003', 'Marketing 101', 'Alice Kim', 'Business', 2021, 60.0),
('B004', 'Creative Design', 'David Lee', 'Design', 2022, 50.0);

INSERT INTO `customers`(`customer_id`, `customer_full_name`, `customer_email`, `customer_phone`, `customer_dob`, `gender`)
VALUES
('C001', 'Nguyen Van A', 'a@gmail.com', '0901234001', '1990-01-01', 'Nam'),
('C002', 'Tran Thi B', 'b@gmail.com', '0901234002', '1991-02-02', 'Nữ'),
('C003', 'Le Van C', 'c@gmail.com', '0901234003', '1992-03-03', 'Nam'),
('C004', 'Pham Thi D', 'd@gmail.com', '0901234004', '1993-04-04', 'Nữ'),
('C005', 'Do Van E', 'e@gmail.com', '0901234005', '1994-05-05', 'Nam'),
('C006', 'Hoang Thi F', 'f@gmail.com', '0901234006', '1995-06-06', 'Nữ'),
('C007', 'Vo Van G', 'g@gmail.com', '0901234007', '1996-07-07', 'Nam'),
('C008', 'Bui Thi H', 'h@gmail.com', '0901234008', '1997-08-08', 'Nữ'),
('C009', 'Dang Van I', 'i@gmail.com', '0901234009', '1998-09-09', 'Nam'),
('C010', 'Ngo Thi K', 'k@gmail.com', '0901234010', '1999-10-10', 'Nữ');

INSERT INTO `orders`(`customer_id`, `book_id`, `order_date`, `order_status`, `quantity`)
VALUES
('C001', 'B001', '2025-06-01', 'Delivered', 1),
('C002', 'B002', '2025-06-02', 'Cancelled', 2),
('C003', 'B003', '2025-06-03', 'Processing', 3),
('C004', 'B004', '2025-06-04', 'Delivered', 1),
('C005', 'B001', '2025-06-05', 'Cancelled', 2),
('C006', 'B002', '2025-06-06', 'Processing', 3),
('C007', 'B003', '2025-06-07', 'Delivered', 1),
('C008', 'B004', '2025-06-08', 'Cancelled', 2),
('C009', 'B001', '2025-06-09', 'Processing', 3),
('C010', 'B002', '2025-06-10', 'Delivered', 1),
('C001', 'B003', '2025-06-11', 'Cancelled', 2),
('C002', 'B004', '2025-06-12', 'Processing', 3),
('C003', 'B001', '2025-06-13', 'Delivered', 1),
('C004', 'B002', '2025-06-14', 'Cancelled', 2),
('C005', 'B003', '2025-06-15', 'Processing', 3),
('C006', 'B004', '2025-06-16', 'Delivered', 1),
('C007', 'B001', '2025-06-17', 'Cancelled', 2),
('C008', 'B002', '2025-06-18', 'Processing', 3),
('C009', 'B003', '2025-06-19', 'Delivered', 1),
('C010', 'B004', '2025-06-20', 'Cancelled', 2);

INSERT INTO `payment`(`order_id`, `payment_method`, `payment_amount`, `payment_date`, `payment_status`)
VALUES
(1, 'Credit Card', 45.5, '2025-06-01', 'Success'),
(2, 'Bank Transfer', 55.0, '2025-06-02', 'Failed'),
(3, 'Cash', 60.0, '2025-06-03', 'Pending'),
(4, 'Credit Card', 50.0, '2025-06-04', 'Success'),
(5, 'E-wallet', 45.5, '2025-06-05', 'Pending'),
(6, 'E-wallet', 55.0, '2025-06-06', 'Success'),
(7, 'Credit Card', 60.0, '2025-06-07', 'Failed'),
(8, 'Bank Transfer', 50.0, '2025-06-08', 'Pending'),
(9, 'Cash', 45.5, '2025-06-09', 'Success'),
(10, 'Credit Card', 55.0, '2025-06-10', 'Pending');

-- 2. Viết câu UPDATE cho phép cập nhật trạng thái thanh toán trong bảng Payment:
	-- Cập nhật trạng thái thanh toán thành "Success" nếu số tiền thanh toán (payment_amount) > 0 và phương thức thanh toán là "Credit Card".
	-- Cập nhật trạng thái thanh toán thành "Pending" nếu phương thức thanh toán là "Bank Transfer" và số tiền thanh toán nhỏ hơn 100.
	-- Chỉ cập nhật trạng thái thanh toán cho những giao dịch có ngày thanh toán (payment_date) là trước ngày hiện tại (CURRENT_DATE)
UPDATE `payment`
SET `payment_status` = 'Success'
WHERE `payment_amount` > 0 
	AND `payment_method` = 'Credit Card' 
    AND `payment_date` < CURRENT_DATE;

UPDATE `payment`
SET `payment_status` = 'Pending'
WHERE `payment_amount` < 100 
	AND `payment_method` = 'Bank Transfer'
    AND `payment_date` < CURRENT_DATE;
    
-- 3. Xóa các bản ghi trong bảng Payment nếu trạng thái thanh toán là "Pending" và phương thức thanh toán là "Cash".
DELETE FROM `payment`
WHERE `payment_status` = 'Pending' 
	AND `payment_method` = 'Cash';

--
-- PHẦN 4: Truy vấn dữ liệu
--
-- 1. Lấy 7 khách hàng gồm mã, tên, email, ngày sinh, giới tính, sắp xếp theo tên tăng dần.
SELECT `customer_id`, `customer_full_name`, `customer_email`, `customer_dob`, `gender`
FROM `customers`
ORDER BY `customer_full_name` ASC
LIMIT 7;

-- 2. Lấy thông tin các cuốn sách gồm mã, tên, danh mục, sắp xếp theo giá tiền giảm dần.
SELECT `book_id`, `name`, `genre`
FROM `books`
ORDER BY `price` DESC;

-- 3. Lấy thông tin các khách hàng gồm mã khách hàng, tên khách hàng, tên sách đã đặt và có trạng thái order là "Cancelled".
SELECT o.`customer_id`, c.`customer_full_name`, b.`name`
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `books` b ON b.`book_id` = o.`book_id`
WHERE o.`order_status` = 'Cancelled';

-- 4. Lấy danh sách cuốn sách gồm mã sách, mã khách hàng, tên sách đã đặt, và số lượng sách
-- cho các cuốn sách có trạng thái "Delivered", sắp xếp theo số lượng giảm dần.
SELECT o.`book_id`, o.`customer_id`, b.`name`, o.`quantity`
FROM `orders` o
JOIN `books` b ON b.`book_id` = o.`book_id`
WHERE o.`order_status` = 'Delivered'
ORDER BY o.`quantity` DESC;

-- 5. Lấy danh sách các khách hàng gồm mã sách, tên khách hang, mã khách hàng, tên sách đã đặt, và số lượng sách
-- đã đặt cho các khách hàng có số lượng sách đặt trong khoảng từ 2 đến 5, sắp xếp theo tên học viên
SELECT o.`book_id`, c.`customer_full_name`, o.`customer_id`, b.`name`, o.`quantity`
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `books` b ON b.`book_id` = o.`book_id`
WHERE o.`quantity` BETWEEN 2 AND 5
ORDER BY c.`customer_full_name`;

-- 6. Lấy danh sách các khách đặt ít nhất 7 quyển sách nhưng trạng thái thanh toán đang ở 'Pending'.
SELECT o.`customer_id`, c.`customer_full_name`, SUM(o.`quantity`) AS 'Số sách đã đặt'
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `payment` p ON p.`order_id` = o.`order_id`
WHERE p.`payment_status` = 'Pending'
GROUP BY o.`customer_id`
HAVING SUM(o.`quantity`) >= 7;

-- 7. Lấy danh sách các khách và số tiền đã thanh toán với trạng thái 'Success'.
SELECT o.`customer_id`, c.`customer_full_name`,  SUM(p.`payment_amount`) AS 'Số tiền đã thanh toán'
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `payment` p ON p.`order_id` = o.`order_id`
WHERE p.`payment_status` = 'Success'
GROUP BY o.`customer_id`;

-- 8. Lấy danh sách 7 khách đầu tiên có số lượng sách đặt lớn hơn 1, sắp xếp 
-- theo số lượng sách đặt giảm dần, gồm mã khách, tên khách, số lượng quantity và trạng thái order
SELECT o.`customer_id`, c.`customer_full_name`, SUM(o.`quantity`) AS 'Số sách đã đặt'
FROM `orders` AS o
JOIN `customers` AS c ON c.`customer_id` = o.`customer_id`
GROUP BY o.`customer_id`
HAVING SUM(o.`quantity`) > 1
ORDER BY SUM(o.`quantity`) DESC
LIMIT 7;
-- (Ghi chú: không thể thêm order_status vào select vì như vậy sẽ tính tổng quantity theo customer_id và order_status)
-- SELECT o.`customer_id`, c.`customer_full_name`, SUM(o.`quantity`) AS 'Số sách đã đặt', o.`order_status`
-- FROM `orders` AS o
-- JOIN `customers` AS c ON c.`customer_id` = o.`customer_id`
-- GROUP BY o.`customer_id`, o.`order_status`
-- HAVING SUM(o.`quantity`) > 1
-- ORDER BY SUM(o.`quantity`) DESC
-- LIMIT 7;

-- 9. Lấy thông tin sách có số lượng đặt nhiều nhất
SELECT b.*, SUM(o.`quantity`) AS 'Số quyển đã đặt'
FROM `orders` o
JOIN `books` b ON b.`book_id` = o.`book_id`
GROUP BY o.`book_id`
HAVING SUM(o.`quantity`) >= ALL (
	SELECT SUM(o.`quantity`)
    FROM `orders`
    GROUP BY `book_id`
	);

-- 10. Lấy danh sách các khách có ngày sinh trước năm 2000 và đã thanh toán thành công.
SELECT o.`customer_id`, c.`customer_full_name`, c.`customer_dob`, p.`payment_status`
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `payment` p ON p.`order_id` = o.`order_id`
WHERE p.`payment_status` = 'Success' 
	AND c.`customer_dob` < '2000-01-01';

--
-- PHẦN 5: Tạo View
--
-- 1. Tạo view view_all_customer_order để lấy danh sách tất cả các khách hàng và sách đã đăng ký, 
-- gồm mã khách, tên khách, mã sách, tên sách và số lượng quantity đã đăng ký
CREATE VIEW `view_all_customer_order` AS
SELECT c.`customer_id`, c.`customer_full_name`, o.`book_id`, b.`name`, o.`quantity`
FROM `orders` o
RIGHT JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `books` b ON b.`book_id` = o.`book_id`
ORDER BY c.`customer_id`;

SELECT * FROM `view_all_customer_order`;

-- 2. Tạo view view_successful_payment để lấy danh sách các khách đã thanh toán thành công, gồm mã khách, 
-- tên khách và số tiền thanh toán, chỉ lấy các giao dịch có trạng thái thanh toán "Success"
CREATE VIEW `view_successful_payment` AS
SELECT o.`customer_id`, c.`customer_full_name`, p.`payment_amount`
FROM `orders` o
JOIN `customers` c ON c.`customer_id` = o.`customer_id`
JOIN `payment` p ON p.`order_id` = o.`order_id`
WHERE p.`payment_status` = 'Success';

SELECT * FROM `view_successful_payment`;

-- PHẦN 6: Tạo Trigger
-- 1. Tạo một trigger để kiểm tra và đảm bảo rằng số quantity trong bảng order không bị giảm xuống dưới 1 khi có sự thay đổi. 
-- Nếu số quantity nhỏ hơn 1, trigger sẽ thông báo lỗi SIGNAL SQLSTATE và không cho phép cập nhật.
DELIMITER //
CREATE TRIGGER `check_quantity`
BEFORE UPDATE ON `orders`
FOR EACH ROW
BEGIN
	IF NEW.`quantity` < 1 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Number of orders cannot be less than 1';
	END IF;
END //
DELIMITER ;

-- Kiểm tra TRIGGER `check_quantity`
UPDATE `orders`
SET `quantity` = 0
WHERE `order_id` = 1;

-- 2. Tạo một trigger để khi thực hiện chèn dữ liệu vào bảng Payment, sẽ tự động kiểm tra trạng thái thanh toán, 
-- nếu như trạng thái thanh toán là “Success” thì tiến hành cập nhật trạng thái order_status của ở bảng order tương ứng
-- với đơn đăng ký đó thành “Delivered”
DELIMITER //
CREATE TRIGGER `check_payment_status`
AFTER INSERT ON `payment`
FOR EACH ROW
BEGIN
	IF NEW.`payment_status` = 'Success' THEN
		UPDATE `orders`
        SET `order_status` = 'Delivered'
        WHERE `order_id` = NEW.`order_id`;
	END IF;
END //
DELIMITER ;

-- Kiểm tra TRIGGER `check_payment_status`
INSERT INTO `payment`(`order_id`, `payment_method`, `payment_amount`, `payment_date`, `payment_status`)
VALUES
(11, 'Cash', 75.5, '2025-07-01', 'Success'),
(12, 'Credit Card', 55.5, '2025-07-02', 'Pending');

--
-- PHẦN 7: Tạo Store Procedure
--
-- 1. Tạo một stored procedure có tên GetAllCustomerOrders để lấy thông tin của các sách khách đã đăng ký theo id của khách
DELIMITER //
CREATE PROCEDURE GetAllCustomerOrders(
	IN p_customer_id VARCHAR(10)
)
BEGIN
	SELECT c.`customer_id`, c.`customer_full_name`, b.*
	FROM `orders` o
	JOIN `customers` c ON c.`customer_id` = o.`customer_id`
	JOIN `books` b ON b.`book_id` = o.`book_id`
	WHERE c.`customer_id` = p_customer_id;
END //
DELIMITER ;

CALL GetAllCustomerOrders('C001');

-- 2. Tạo một stored procedure có tên UpdateOrder để thực hiện thao tác cập nhật một bản ghi trong vào bảng Order dựa theo khóa chính.
DELIMITER //
CREATE PROCEDURE UpdateOrder(
	IN p_order_id INT,
	IN p_customer_id VARCHAR(10),
	IN p_book_id VARCHAR(10),
	IN p_quantity INT
)
BEGIN
	UPDATE `orders`
	SET 
		`customer_id` = p_customer_id,
        `book_id` = p_book_id,
        `quantity` = p_quantity
	WHERE `order_id` = p_order_id;
END //
DELIMITER ;

CALL UpdateOrder(1, 'C009', 'B001', 10);
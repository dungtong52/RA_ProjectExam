INSERT INTO users (name, dob, email, sex, phone, password, create_at, role, status)
VALUES
-- Admin
('Admin User', '1990-01-01', 'admin@example.com', TRUE, '0900000001', 'admin123', '2025-01-01', 'ADMIN', TRUE),

-- Students
('Nguyen Hieu', '1995-05-20', 'hieu95@example.com', TRUE, '0911111111', '123456', '2025-01-01', 'STUDENT', TRUE),
('Tran Hoa', '1998-07-11', 'hoa98@example.com', FALSE, '0911111112', '123456', '2025-01-01', 'STUDENT', TRUE),
('Le Nam', '1997-02-14', 'nam97@example.com', TRUE, '0911111113', '123456', '2025-01-01', 'STUDENT', TRUE),
('Pham Tuan', '1999-09-09', 'tuan99@example.com', TRUE, '0911111114', '123456', '2025-01-01', 'STUDENT', TRUE),
('Doan My', '2000-03-25', 'my00@example.com', FALSE, '0911111115', '123456', '2025-01-01', 'STUDENT', TRUE),
('Hoang Vu', '1996-08-18', 'vu96@example.com', TRUE, '0911111116', '123456', '2025-01-01', 'STUDENT', TRUE),
('Nguyen Ha', '1995-12-30', 'ha95@example.com', FALSE, '0911111117', '123456', '2025-01-01', 'STUDENT', TRUE),
('Tran Kien', '1994-04-10', 'kien94@example.com', TRUE, '0911111118', '123456', '2025-01-01', 'STUDENT', TRUE),
('Le Anh', '2001-06-05', 'anh01@example.com', FALSE, '0911111119', '123456', '2025-01-01', 'STUDENT', TRUE),
('Nguyen Son', '1993-10-21', 'son93@example.com', TRUE, '0911111120', '123456', '2025-01-01', 'STUDENT', TRUE),
('Pham Linh', '1997-01-12', 'linh97@example.com', FALSE, '0911111121', '123456', '2025-01-01', 'STUDENT', TRUE),
('Tran Quang', '1998-11-02', 'quang98@example.com', TRUE, '0911111122', '123456', '2025-01-01', 'STUDENT', TRUE),
('Do Bao', '1996-07-15', 'bao96@example.com', TRUE, '0911111123', '123456', '2025-01-01', 'STUDENT', TRUE),
('Nguyen Lan', '1995-03-08', 'lan95@example.com', FALSE, '0911111124', '123456', '2025-01-01', 'STUDENT', TRUE),
('Hoang Phu', '1999-12-19', 'phu99@example.com', TRUE, '0911111125', '123456', '2025-01-01', 'STUDENT', TRUE),
('Le Minh', '1992-09-23', 'minh92@example.com', TRUE, '0911111126', '123456', '2025-01-01', 'STUDENT', TRUE),
('Tran Dao', '1997-05-28', 'dao97@example.com', FALSE, '0911111127', '123456', '2025-01-01', 'STUDENT', TRUE),
('Nguyen Khanh', '2000-01-17', 'khanh00@example.com', TRUE, '0911111128', '123456', '2025-01-01', 'STUDENT', TRUE),
('Pham Giang', '1994-06-06', 'giang94@example.com', FALSE, '0911111129', '123456', '2025-01-01', 'STUDENT', TRUE),
('Hoang Duy', '1996-02-03', 'duy96@example.com', TRUE, '0911111130', '123456', '2025-01-01', 'STUDENT', TRUE);

INSERT INTO courses (name, duration, instructor, create_at, image, status)
VALUES ('Java Cơ bản', 30, 'Nguyen Van Giang', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Spring Boot Nâng cao', 45, 'Le Thi Hoa', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Cơ sở dữ liệu SQL', 20, 'Pham Van Minh', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Python cho người mới', 25, 'Tran Thi Mai', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('JavaScript & React', 35, 'Hoang Duc', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Node.js & Express', 30, 'Nguyen Huy', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Machine Learning cơ bản', 40, 'Le Quoc', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE),
       ('Thiết kế Web Responsive', 20, 'Pham Lan', '2025-01-01',
        'https://rikkei.edu.vn/wp-content/uploads/2023/05/khoa-hoc-lap-trinh-rikkei-academy.jpg', TRUE);

INSERT INTO enrollments (user_id, course_id, registered_at, status)
VALUES (2, 1, '2025-02-01 10:00:00', 'WAITING'),
       (3, 2, '2025-02-01 11:00:00', 'CONFIRM'),
       (4, 3, '2025-02-02 09:30:00', 'DENIED'),
       (5, 4, '2025-02-02 10:30:00', 'CONFIRM'),
       (6, 5, '2025-02-03 14:00:00', 'WAITING'),
       (7, 6, '2025-02-03 15:00:00', 'CANCEL'),
       (8, 7, '2025-02-04 09:00:00', 'WAITING'),
       (9, 8, '2025-02-04 10:00:00', 'CONFIRM'),
       (10, 1, '2025-02-05 11:30:00', 'CANCEL'),
       (11, 2, '2025-02-05 12:00:00', 'DENIED'),
       (12, 3, '2025-02-06 09:15:00', 'WAITING'),
       (13, 4, '2025-02-06 10:20:00', 'CONFIRM'),
       (14, 5, '2025-02-07 13:45:00', 'DENIED'),
       (15, 6, '2025-02-07 14:30:00', 'WAITING'),
       (16, 7, '2025-02-08 08:50:00', 'CONFIRM'),
       (17, 8, '2025-02-08 09:30:00', 'CANCEL'),
       (18, 1, '2025-02-09 10:10:00', 'WAITING'),
       (19, 2, '2025-02-09 11:05:00', 'CONFIRM'),
       (20, 3, '2025-02-10 12:00:00', 'DENIED'),
       (21, 4, '2025-02-10 13:00:00', 'CANCEL');


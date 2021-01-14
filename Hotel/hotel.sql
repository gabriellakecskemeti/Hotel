  -- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
DROP DATABASE IF EXISTS hotel;
CREATE DATABASE IF NOT EXISTS hotel;
USE hotel;

drop table if exists category;
drop table if exists guests;
drop table if exists rooms;
drop table if exists staff;
drop table if exists bookings;
drop table if exists invoice;


CREATE TABLE category(
  id int NOT NULL auto_increment primary key,
  name varchar(30) NOT NULL,
  capacity int NOT NULL,
  price double NOT NULL,
  roomsize int NOT NULL,
  facilities varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO category ( name, capacity, price, roomsize, facilities) VALUES
( ' single room', 1, 23, 15, 'WLAN, coffee machine, TV, Mini bar'),
( ' double room', 2, 33, 25, 'WLAN, coffee machine, TV'),
( 'superior double room', 3, 43, 35, 'WLAN, coffee machine, TV, Mini bar, Kingsize bed'),
( ' suite', 4, 53, 45, 'WLAN, coffee machine, TV, Mini bar');


CREATE TABLE staff (
  id int(9) NOT NULL auto_increment primary key,
  username varchar(20) NOT NULL,
  firstname varchar(40) NOT NULL,
  lastname varchar(50) NOT NULL,
  password varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO staff (username, firstname,lastname, password) VALUES
('Georg', 'Georg', 'Postman', '1000:c445bc8d8bdd340c43e0dffccd9db174:5ca3daa4cfe8b462dcfe71e5eab95fced93fc5e0c773d7215fa8b1685d753ef6a36a7eb8ff7532c895ec72cf4c6db8993097e4adc96b1c014e780af311d85078'),
('Hannah', 'Hannah','doe', '1000:d41ffae9c156160c9ce06ca82d50cf74:26cddb21fab936cc746287f9fadd386d438aaab469debe5cb6d66b22350384b567a2df385e705ad7f7bdaec9f7df1a298e053fe29613469694c70fc11600f81d'),
('Eva', 'Eva','Müller', '1000:7574d5870fd9d7a7bbca9f1b3034cd08:3e1abfc64729d8c69d4ffba91878b654af8306b683bb99ef5d3296312fb30fcb0b821613aa6d4858dc97372d9690538574780fae9333950ccff6c478e814a154');



CREATE TABLE rooms (
  id int NOT NULL auto_increment primary key,
  room_number int NOT NULL,
  description varchar(255) NOT NULL,
  fk_category_id int NOT NULL,
foreign key (fk_category_id) references category (id) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO rooms ( room_number, description, fk_category_id) VALUES
(111, 'View to the forest, bathtube', 1),
(112, 'View to the forest, antiallergic wood floor', 4),
(113, 'View to the forest, king size bed', 2),
(114, 'View to the Hills, bathtube', 3);




CREATE TABLE guests (
  id int NOT NULL auto_increment primary key,
  first_name varchar(40) NOT NULL,
  last_name varchar(50) NOT NULL,
  address varchar(50) NOT NULL,
  zip varchar(10) NOT NULL,
  country varchar(20) NOT NULL,
  email varchar(50) DEFAULT NULL,
  birth date NOT NULL,
  phone_number varchar(30) DEFAULT NULL,
  document varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO guests ( first_name, last_name, address, zip, country, email, birth, phone_number, document) VALUES
('Rami', 'lechner', 'North street 9', '010', 'US', 'rami@gmail.com', '1990-02-06', 315400482, 'passport 1234'),
('Sheila', 'Wunder', 'North street 9', '010', 'US', 'sola@gmail.com', '1994-09-16', 322020343, 'passport NO(23345)'),
 ('JOE', 'lala', 'Elisabeth Strasse 15', '119966', 'Deutschland', 'joe@gmail.com', '1980-02-03', 45320343, 'passport NO(22345)'),
('shira', 'saka', 'Viadukt Allee 44', '1180', 'Österreich', 'shira@gmail.com', '1991-03-06', 453020343, 'passport NO(09345)'),
('well', 'smith', 'Heiligen Städter Str 25', '1190', 'Österreich', 'well@gmail.com', '1992-04-09', 303090343, 'passport NO98345)'),
( 'mike', 'jonse', 'Mai street 3', '010', 'GB', 'jonse2@gmail.com', '1994-03-16', 2147483647, 'passport NO(171345)'),
( 'Klara', 'Molnar', 'Petöfi utca 33', '9111', 'HU', 'maia@gmail.com', '1996-12-06', 2147483647, 'passport NO(1923455)'),
('Martha', 'Bitter', 'Petöfi utca 33.', '9111', 'HU', 'jens@gmail.com', '1992-09-02', 30300043, 'passport NO(1923345)'),
( 'Peter', 'Bolla', 'Petöfi utca 33.', '9111', 'HU', 'kim@gmail.com', '1995-09-02', 345020343, 'passport NO(10005)'),
( 'Lara', 'Lasyn', 'Tiergarten Str 3', '95645', 'Deutschland', 'lana@gmail.com', '1994-10-16', 303093343, 'passport NO(170235)');





CREATE TABLE bookings (
  id int NOT NULL auto_increment primary key,
  arrival_date date NOT NULL,
  departure_date date NOT NULL,
  total_price int(11) NOT NULL,
  payment_type enum('credit card','paypal','banktransfer','cash') NOT NULL,
  status enum('BOOKED','CANCELLED','PAID','OPENED') NOT NULL,
  notes varchar(255) DEFAULT NULL,
  fk_guest_id int NOT NULL,
  fk_room_id int NOT NULL,
  fk_staff_id int NOT NULL,
foreign key (fk_guest_id) REFERENCES guests (id) on delete cascade ON UPDATE CASCADE,
foreign key (fk_room_id) REFERENCES rooms (id) on delete cascade ON UPDATE CASCADE,
foreign key (fk_staff_id) REFERENCES staff (id) on delete cascade ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO bookings ( arrival_date, departure_date, total_price, payment_type, status, notes, fk_guest_id, fk_room_id, fk_staff_id) VALUES
( '2020-12-14', '2020-12-21', 23, 'credit card', 'booked', 'he was angry man', 1, 1, 1),
( '2020-12-15', '2020-12-21', 33, 'paypal', 'paid', NULL, 2, 2, 2),
( '2020-12-15', '2020-12-21', 33, 'paypal', 'paid', NULL, 3, 2, 3),
( '2020-12-22', '2020-12-24', 0, 'banktransfer', 'cancelled', NULL, 4, 1, 2),
( '2020-12-25', '2020-12-27', 0, 'banktransfer', 'cancelled', NULL, 4, 1, 2),
( '2020-12-20', '2020-12-22', 43, 'cash', 'booked', NULL, 5, 3, 1),
( '2020-12-20', '2020-12-23', 43, 'paypal', 'booked', NULL, 6, 4, 2),
( '2020-12-20', '2020-12-23', 53, 'cash', 'cancelled', NULL, 7, 1, 3);



CREATE TABLE invoice (
  id int NOT NULL  auto_increment primary key,
  date timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  fk_bookings_id int NOT NULL,
  fk_staff_id int NOT NULL,
foreign key fk_bookings_id  (fk_bookings_id) REFERENCES bookings (id) on delete cascade ON UPDATE CASCADE,
foreign key fk_staff_id (fk_staff_id) REFERENCES staff (id) on delete cascade ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO invoice (date, fk_bookings_id, fk_staff_id) VALUES
('2020-12-23 15:58:44', 1, 1),
('2020-12-15 12:12:29', 8, 3);


COMMIT;


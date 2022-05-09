create database demo;

use demo;

create table todo_item
(
    id   uuid primary key,
    name varchar(255)
);
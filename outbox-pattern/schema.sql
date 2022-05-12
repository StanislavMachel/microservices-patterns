--create database demo;

use demo;

create table if not exists todo_item
(
    id   uuid not null primary key,
    name varchar(255)
);

create table if not exists outbox
(
    id        uuid        not null primary key,
    operation varchar(255),
    aggregate varchar(255),
    message   varchar,
    ts        timestamptz not null
);



delete
from todo_item
where 1 = 1;
delete
from outbox
where 1 = 1;


drop table todo_item;
drop table outbox;
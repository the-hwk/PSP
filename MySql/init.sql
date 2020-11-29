create database multichat;

use multichat;

create table users (
	id int primary key auto_increment,
    email nvarchar(30) not null unique,
    nickname nvarchar(30) not null,
    roleId int not null
);

create table passwords (
	id int primary key auto_increment,
    userId int not null,
    value blob not null,
    constraint fk_passwords_users
    foreign key (userId) references users(id) on delete cascade
);

create table rooms (
	id int primary key auto_increment,
    name nvarchar(30) not null
);

create table rooms_data (
	id int primary key auto_increment,
    userId int not null,
    roomId int not null,
    constraint fk_rooms_data_users
    foreign key (userId) references users(id) on delete cascade,
    constraint fk_rooms_data_rooms
    foreign key (roomId) references rooms(id) on delete cascade
);

create table messages (
	id int primary key auto_increment,
    value nvarchar(300) not null,
    userId int not null,
    roomId int not null,
    constraint fk_messages_users
    foreign key (userId) references users(id) on delete cascade,
    constraint fK_messages_rooms
    foreign key (roomId) references rooms(id) on delete cascade
);
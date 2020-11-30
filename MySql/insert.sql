use multichat;

drop procedure if exists InsertMessage;
drop procedure if exists InsertPassword;
drop procedure if exists InsertRoom;
drop procedure if exists InsertUser;
drop procedure if exists InsertRoomData;

#------------------MESSAGES------------------
DELIMITER //
create procedure InsertMessage(value nvarchar(300), userId int, roomId int)
begin
	insert messages(value, userId, roomId) values (value, userId, roomId);
    select id from messages order by id desc limit 1;
end
//

#------------------ROOMS------------------
DELIMITER //
create procedure InsertRoom (name nvarchar(30))
begin
	insert rooms (name) values (name);
    select id from rooms order by id desc limit 1;
end
//

#------------------USERSDATA------------------
DELIMITER //
create procedure InsertUser(email nvarchar(30), nickname nvarchar(30), roleId int)
begin
	insert users (email, nickname, roleId) values (email, nickname, roleId);
    select id from usersdata order by id desc limit 1;
end
//

#------------------PASSWORDS------------------
DELIMITER //
create procedure InsertPassword(userId int, value blob)
begin
	insert passwords (userId, value) values (userId, value);
    select id from passwords order by id desc limit 1;
end
//

#------------------ROOM DATA------------------
DELIMITER //
create procedure InsertRoomData(roomId int, userId int)
begin
	insert rooms_data(userId, roomId) values (userId, roomId);
    select id from rooms_data order by id desc limit 1;
end
//
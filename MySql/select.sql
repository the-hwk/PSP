use multichat;

drop procedure if exists SelectMessageById;
drop procedure if exists SelectMessagesByRoom;

drop procedure if exists SelectPasswordByUser;

drop procedure if exists SelectRoomById;
drop procedure if exists SelectRoomsByUser;

#------------------MESSAGES------------------
DELIMITER //
create procedure SelectMessageById(id int)
begin
	select * from messages where messages.id = id;
end
//

DELIMITER //
create procedure SelectMessageByRoom(roomId int)
begin
	select * from messages where messages.roomId = roomId;
end
//

#------------------PASSWORDS------------------
DELIMITER //
create procedure SelectPasswordByUser(userId int)
begin
	select * from passwords where passwords.userId = userId;
end
//

#------------------ROOMS------------------
DELIMITER //
create procedure SelectRoomById(id int)
begin
	select * from rooms where rooms.id = id;
end
//

DELIMITER //
create procedure SelectRoomsByUser(userId int)
begin
	select rooms.id, rooms.name from rooms_data join rooms on rooms.id = rooms_data.roomId 
		join users on users.id = rooms_data.userId where rooms_data.userId = userId;
end
//

#------------------USERS------------------
DELIMITER //
create procedure SelectUserById(id int)
begin
	select * from users where users.id = id;
end
//

DELIMITER //
create procedure SelectAllUsers()
begin
	select * from users;
end
//
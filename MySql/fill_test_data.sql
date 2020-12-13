use multichat;

insert users (id, email, nickname, role_id) 
	values (1, 'test@mail.ru', 'test_user', 0);
    
insert passwords (id, user_id, value) 
	values (1, 1, 'pass');

insert rooms (id, name) 
	values (1, 'test_room');
    
insert rooms_data(user_id, room_id) 
	values (1, 1);    
    
insert messages(id, value, user_id, room_id, date_val) 
	values (1, 'Test message', 1, 1, '2000-01-01');
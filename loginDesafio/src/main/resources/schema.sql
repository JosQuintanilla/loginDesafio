create table message (
  id identity,
  content varchar (50) not null,
  sender varchar (50) not null,
  sendDate date not null
);
create table usuario(
	id varchar (50) not null,
	created date not null,
	modified date not null,
	last_login date not null,
	token varchar (250) not null,
	name varchar (50) not null,
	email varchar (50) not null,
	password varchar (60) not null
);
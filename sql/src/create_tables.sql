DROP TABLE WORK_EXPR IF EXISTS WORK_EXPR CASCADE;
DROP TABLE EDUCATIONAL_DETAILS IF EXISTS EDUCATIONAL_DETAILS CASCADE;
DROP TABLE MESSAGE IF EXISTS MESSAGE CASCADE;
DROP TABLE CONNECTION_USR IF EXISTS CONNECTION_USR CASCADE;
DROP TABLE USR IF EXISTS USR CASCADE;


CREATE TABLE USR(
	userId varchar(10) UNIQUE NOT NULL, 
	password varchar(10) NOT NULL,
	email text NOT NULL,
	name char(50),
	dateOfBirth date,
	Primary Key(userId));

CREATE TABLE WORK_EXPR(
	userId char(10) NOT NULL, 
	company char(50) NOT NULL, 
	role char(50) NOT NULL,
	location char(50),
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate),
	FOREIGN KEY (userId)
		REFERENCES User(userId)
	);

CREATE TABLE EDUCATIONAL_DETAILS(
	userId char(10) NOT NULL, 
	instituitionName char(50) NOT NULL, 
	major char(50) NOT NULL,
	degree char(50) NOT NULL,
	startdate date,
	enddate date,
	PRIMARY KEY(userId,major,degree),
	FOREIGN KEY(userId)
		REFERENCES User(userId)
	);

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId char(10) NOT NULL,
	receiverId char(10) NOT NULL,
	contents char(500) NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId),
	FOREIGN KEY(senderId) 
		REFERENCES User(userId),
	FOREIGN KEY(receiverId) 
		REFERENCES User(userId)
);

CREATE TABLE CONNECTION_USR(
	userId char(10) NOT NULL, 
	connectionId char(10) NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId),
	FOREIGN KEY(userId) 
		REFERENCES User(userId)
);

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId char(10) NOT NULL,
	receiverId char(10) NOT NULL,
	contents char(500) NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId));

CREATE TABLE CONNECTION_USR(
	userId char(10) NOT NULL, 
	connectionId char(10) NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId));

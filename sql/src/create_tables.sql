DROP TABLE IF EXISTS WORK_EXPR CASCADE;
DROP TABLE IF EXISTS EDUCATIONAL_DETAILS CASCADE;
DROP TABLE IF EXISTS MESSAGE CASCADE;
DROP TABLE IF EXISTS CONNECTION_USR CASCADE;
DROP TABLE IF EXISTS USR CASCADE;


CREATE TABLE USR(
	userId varchar(30) UNIQUE NOT NULL, 
	password varchar(30) NOT NULL,
	email text NOT NULL,
	name char(50),
	dateOfBirth date,
	Primary Key(userId));

CREATE TABLE WORK_EXPR(
	userId char(30) NOT NULL, 
	company char(50) NOT NULL, 
	role char(50) NOT NULL,
	location char(50),
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate),
	FOREIGN KEY (userId)
		REFERENCES USR(userId)
	);

CREATE TABLE EDUCATIONAL_DETAILS(
	userId char(30) NOT NULL, 
	instituitionName char(50) NOT NULL, 
	major char(50) NOT NULL,
	degree char(50) NOT NULL,
	startdate date,
	enddate date,
	PRIMARY KEY(userId,major,degree),
	FOREIGN KEY(userId)
		REFERENCES USR(userId)
	);

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId char(30) NOT NULL,
	receiverId char(30) NOT NULL,
	contents char(500) NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId),
	FOREIGN KEY(senderId) 
		REFERENCES USR(userId),
	FOREIGN KEY(receiverId) 
		REFERENCES USR(userId)
);

CREATE TABLE CONNECTION_USR(
	userId char(30) NOT NULL, 
	connectionId char(30) NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId),
	FOREIGN KEY(userId) 
		REFERENCES USR(userId)
);
CREATE SEQUENCE MessageIDSequence START 1 OWNED BY MESSAGE.msgID;

--Edward Martens (862256447), Cody Phu (862107077)
COPY USR(userId, password, email, name, dateOfBirth)
FROM 'USR.csv'
DELIMITER ','
CSV HEADER;

COPY MESSAGE(msgId, senderId, receiverId, contents, sendTime, deleteStatus, status)
FROM 'Message.csv'
DELIMITER ','
CSV HEADER;

COPY CONNECTION_USR(userId, connectionId, status)
FROM 'Connection.csv'
DELIMITER ','
CSV HEADER;

COPY WORK_EXPR(userId, company, role, location, startDate, endDate)
FROM 'Work_Exp.csv'
DELIMITER ','
CSV HEADER;

COPY EDUCATIONAL_DETAILS(userId, instituitionName, major, degree, startdate, enddate)
FROM 'Edu_Dep.csv'
DELIMITER ','
CSV HEADER;


SELECT setval('MessageIDSequence',max(msgId)) FROM MESSAGE

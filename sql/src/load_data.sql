COPY USR(userId, password, email, name, dateOfBirth)
FROM 'FINAL_DATA_USR.csv'
DELIMITER ','
CSV HEADER;

COPY MESSAGE(msgId, senderId, receiverId, contents, sendTime, deleteStatus, status)
FROM 'FINAL_DATA_Message.csv'
DELIMITER ','
CSV HEADER;

COPY CONNECTION_USR(userId, connectionId, status)
FROM 'FINAL_DATA_Connection.csv'
DELIMITER ','
CSV HEADER;

COPY WORK_EXPR(userId, company, role, location, startDate, endDate)
FROM 'FINAL_DATA_Work_ex.csv'
DELIMITER ','
CSV HEADER;

COPY EDUCATIONAL_DETAILS(userId, instituitionName, major, degree, startdate, enddate)
FROM 'FINAL_DATA_Edc_Det.csv'
DELIMITER ','
CSV HEADER;

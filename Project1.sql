--Table for reimbursements
CREATE TABLE ers_reimbursement(
	REIMB_ID			SERIAL PRIMARY KEY,
	REIMB_AMOUNT		FLOAT(2),
	REIMB_SUBMITTED		TIMESTAMP,
	REIMB_RESOLVED		TIMESTAMP,
	REIMB_DESCRIPTION	VARCHAR(250),
	REIMB_RECEIPT		BYTEA,		--This may not be the same as a BLOB; consider researching at a later date.
	REIMB_AUTHOR 		INTEGER,
	REIMB_RESOLVER		INTEGER,
	REIMB_STATUS_ID		INTEGER,
	REIMB_TYPE_ID		INTEGER,
	FOREIGN KEY (REIMB_AUTHOR) REFERENCES ers_users(ERS_USERS_ID),
	FOREIGN KEY (REIMB_RESOLVER) REFERENCES ers_users(ERS_USERS_ID),
	FOREIGN KEY (REIMB_STATUS_ID) REFERENCES ers_reimbursement_status(REIMB_STATUS_ID),
	FOREIGN KEY (REIMB_TYPE_ID) REFERENCES ers_reimbursement_type(REIMB_TYPE_ID)
);

--Table for users
CREATE TABLE ers_users(
	ERS_USERS_ID		SERIAL PRIMARY KEY,
	ERS_USERNAME		VARCHAR(50) UNIQUE,
	ERS_PASSWORD		VARCHAR(50),
	USER_FIRST_NAME		VARCHAR(100),
	USER_LAST_NAME		VARCHAR(100),
	USER_EMAIL			VARCHAR(150) UNIQUE,
	USER_ROLE_ID		INTEGER,
	FOREIGN KEY (USER_ROLE_ID) REFERENCES ers_user_roles(ERS_USER_ROLE_ID)
);

--Reference tables
CREATE TABLE ers_reimbursement_status(
	REIMB_STATUS_ID		INTEGER PRIMARY KEY,
	REIMB_STATUS		VARCHAR(10)
);

CREATE TABLE ers_reimbursement_type(
	REIMB_TYPE_ID		INTEGER PRIMARY KEY,
	REIMB_TYPE			VARCHAR(10)
);

CREATE TABLE ers_user_roles(
	ERS_USER_ROLE_ID	INTEGER PRIMARY KEY,
	USER_ROLE			VARCHAR(10)
);

--(Extremely basic) hashing method (with 'salt' as the salt)
CREATE OR REPLACE FUNCTION hashPassword() RETURNS TRIGGER AS $hash$
	BEGIN
		IF(NEW.ers_password = OLD.ers_password) THEN
			RETURN NEW;
		END IF;
		NEW.ers_password := MD5(NEW.ers_username || NEW.ers_password || 'salt');
		RETURN NEW;
	END;
$hash$ LANGUAGE plpgsql;

--A function used to accept multiple reimbursements at once (array to accept, resolver's id)
CREATE OR REPLACE FUNCTION acceptArray(INT[], INT) RETURNS VOID AS $accept$
	BEGIN
		UPDATE ers_reimbursement SET reimb_status_id = 1, reimb_resolved = CURRENT_TIMESTAMP, reimb_resolver = $2 WHERE reimb_id = ANY($1);
	END;
$accept$ LANGUAGE plpgsql;

--The opposite of the above function; denies multiple reimbursements at once
CREATE OR REPLACE FUNCTION denyArray(INT[], INT) RETURNS VOID AS $deny$
	BEGIN
		UPDATE ers_reimbursement SET reimb_status_id = 2, reimb_resolved = CURRENT_TIMESTAMP, reimb_resolver = $2 WHERE reimb_id = ANY($1);
	END;
$deny$ LANGUAGE plpgsql;

SELECT acceptarray(ARRAY[1, 2, 3, 4], 27);

--Trigger for hashing passwords upon insertion
CREATE TRIGGER hashPass
BEFORE INSERT OR UPDATE ON ers_users
FOR EACH ROW
EXECUTE FUNCTION hashPassword();

SELECT * FROM ers_reimbursement WHERE reimb_author = 23;
SELECT * FROM ers_reimbursement;
SELECT * FROM ers_users;
SELECT * FROM ers_reimbursement_status;
SELECT * FROM ers_reimbursement_type;
SELECT * FROM ers_user_roles;

--[TODO]
SELECT ers_reimbursement.*, ers_users.user_first_name, ers_users.user_last_name FROM ers_reimbursement
LEFT JOIN ers_users ON ers_reimbursement.reimb_author = ers_users.ers_users_id;

--Insert appropriate data
--TRUNCATE TABLE ers_reimbursement_status CASCADE;
INSERT INTO ers_reimbursement_status VALUES(0, 'Pending'),(1, 'Approved'),(2, 'Denied');
SELECT * FROM ers_reimbursement_status;

INSERT INTO ers_reimbursement_type VALUES(0, 'Lodging'),(1, 'Travel'),(2, 'Food'),(3, 'Other');
SELECT * FROM ers_reimbursement_type;

INSERT INTO ers_user_roles VALUES(0, 'Employee'),(1, 'Manager'),(2, 'Admin');
SELECT * FROM ers_user_roles;

--TRUNCATE TABLE ers_users CASCADE;
INSERT INTO ers_users(ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES
('admin', 'password', 'Davin', 'Merry', 'admin@fake-company.com', 2),
('thejohndoe', 'pass1', 'John', 'Doe', 'john_doe89@gmail.com', 0),
('thejanedoe', 'pass2', 'Jane', 'Doe', 'jane_doe91@gmail.com', 0),
('adam_we', 'batman66', 'Adam', 'West', 'therealwest@outlook.com', 0),
('jedwards1', 'thepassword', 'Johnathan', 'Edwards', 'jeedwards79@yahoo.com', 0),
('employer1', 'secretpass1', 'Lawrence', 'Dawson', 'realemployer1@gmail.com', 1),
('employer2', 'secretpass2', 'John', 'Martin', 'realemployer2@gmail.com', 1)
;
SELECT * FROM ers_users;
--SELECT (ers_users_id, ers_username, ers_password, ers_user_role_id) FROM ers_users LEFT JOIN ers_user_roles ON ers_users.user_role_id = ers_user_roles.ers_user_role_id;

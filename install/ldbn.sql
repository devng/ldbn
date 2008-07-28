DROP TABLE IF EXISTS assignment;
CREATE TABLE assignment (
	id INT(10) NOT NULL AUTO_INCREMENT,
	user_id INT(10) NOT NULL, 
	name VARCHAR( 40 ) NOT NULL,
	modified_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
        	ON UPDATE CURRENT_TIMESTAMP,
	xml TEXT NOT NULL ,
	PRIMARY KEY (id)
);

DROP TABLE IF EXISTS user;
CREATE TABLE user (
	user_id INT(10) NOT NULL AUTO_INCREMENT,
	name VARCHAR( 40 ) NOT NULL,
	pass_md5 VARCHAR(32) NOT NULL ,
	email VARCHAR(80) NOT NULL ,
	active TINYINT(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (user_id)
);

DROP TABLE IF EXISTS session;
CREATE TABLE session (
	session_string VARCHAR(32) NOT NULL ,
	user_id INT(10) NOT NULL,
	added_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (session_string)
);


DROP TABLE IF EXISTS activation;
CREATE TABLE activation (
	activation_string VARCHAR(32) NOT NULL ,
	user_id INT(10) NOT NULL,
	added_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	PRIMARY KEY (activation_string)
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
	id INT(10) NOT NULL AUTO_INCREMENT,
	assignment_id INT(10) NOT NULL,
	user_id INT(10) NOT NULL,
	comment_val TEXT NOT NULL ,
	modified_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
        	ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);


USE TEAMALPHA;


DROP TABLE IF EXISTS userRegistration;

CREATE TABLE userRegistration (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	userName VARCHAR(25) NOT NULL,
	emailId VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
	userPassword VARCHAR(100) NOT NULL,
    accountIsActive CHAR(1) DEFAULT 'I' NOT NULL,
    PRIMARY KEY(id),
    UNIQUE(emailId)
  );
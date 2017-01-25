USE TEAMALPHA;

DROP TABLE IF EXISTS user_activation;

CREATE TABLE user_activation (
  userId MEDIUMINT,
  activationCode CHAR(38) NOT NULL,
  expirationDate DATETIME,
  FOREIGN KEY fk_userID (userId)
  REFERENCES user_registration(id)
  ) 

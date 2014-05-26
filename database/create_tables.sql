
/*
 * Needed tables for the app.
 */
-------------------------------------------------------------------------------
-- First we tidy up earlier versions of the tables if they exist to avoid conflicts and to make sure they are holding the needed attributes.

DROP TABLE login;
DROP TABLE location;
DROP TABLE request;
DROP TABLE hint;
DROP TABLE question;
DROP TABLE available_hint;
DROP TABLE answered_question;
DROP TABLE friend_list;
DROP TABLE current_location;

--------------------------------------------------------------------------------------------------
-- Then we create the needed tables

CREATE TABLE login (
  u_id INT(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  u_name VARCHAR(20) NOT NULL,
  email VarCHAR(20) NOT NULL,
  PRIMARY KEY (u_id), 
  UNIQUE KEY (username),
  UNIQUE KEY (email));
  
CREATE TABLE location (
  l_id INT(4) NOT NULL AUTO_INCREMENT,
  x FLOAT NOT NULL,
  y FLOAT NOT NULL,
  l_name VARCHAR(50) NOT NULL, 
  PRIMARY KEY (l_id),
  UNIQUE KEY (l_name));
  
CREATE TABLE request (
  q_id INT(4) NOT NULL,
  requester_id INT(4) NOT NULL,
  friend_id INT(4) NOT NULL,
  reply VARCHAR(50) NOT NULL,
  PRIMARY KEY (q_id, requester_id, friend_id));
  
CREATE TABLE hint (
  h_id INT(4) NOT NULL AUTO_INCREMENT,  
  l_id INT(4) NOT NULL,
  h_content VARCHAR(50) NOT NULL,
  PRIMARY KEY (h_id));


CREATE TABLE question (
  q_id INT(4) NOT NULL AUTO_INCREMENT,
  maker INT(4) NOT NULL,
  q_content VARCHAR(50) NOT NULL,
  ranking INT(4) UNSIGNED NOT NULL,
  reply VARCHAR(50) NOT NULL,
  PRIMARY KEY (q_id),
  UNIQUE KEY (q_content));  

CREATE TABLE available_hint (
  h_id INT(4) NOT NULL,
  q_id INT(4) NOT NULL);
  
CREATE TABLE answered_question (
  q_id INT(4) NOT NULL,
  u_id INT(4) NOT NULL,
  reply VARCHAR(50));
  
CREATE TABLE friend_list (
  u_id1 INT(4) NOT NULL,
  u_ud2 INT(4) NOT NULL);
-------------------------------------------------------------------------------

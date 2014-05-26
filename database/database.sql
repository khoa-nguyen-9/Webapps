
/*
 * Needed tables for the app.
 */

-- First we tidy up earlier versions of the tables if they exist to avoid conflicts and to make sure they are holding the needed attributes.

DROP TABLE Questions;
DROP TABLE Users;
DROP TABLE Requests;
DROP TABLE Hints;
DROP TABLE Locations;

--------------------------------------------------------------------------------------------------

/*
-- Hints table --
Contains the information about the hints. There will be one row for each hint.
*/
CREATE TABLE Hint(
  HintID integer Primary Key, --unique hint ID
  Content text --the content of the hint 
);

/*
-- Locations table --
Contains the information about the locations. There will be one row for each hint.
*/
CREATE TABLE Location(
  LocationID integer Primary Key, --unique location ID
  x double precision NOT NULL, --X coordinate
  y double precision NOT NULL, --Y coordinate
  Name text --the name of the place 
);

/*
-- Users table --
Contains the information about the user. There will be one row for each user.
*/
CREATE TABLE Users(
  `user_id` int(11) NOT NULL AUTO_INCREMENT, -- unique, autoincrementing user ID
  `user_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL, -- unique user's name
  `user_password_hash` varchar(255) COLLATE utf8_unicode_ci NOT NULL, -- user password in salted and hashed format
  `user_email` varchar(64) COLLATE utf8_unicode_ci NOT NULL, -- user's email, unique'
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


/*
-- Questions table --
Contains the information about the questions. There will be one row for each question*/
CREATE TABLE Question(
  `question_id` int(11) NOT NULL AUTO_INCREMENT, --unique question ID
  `content` varchar(150) COLLATE utf8_unicode_ci NOT NULL, --the content of the question
  Makers integer, --user ID of the user who made the question, default is admin
  HintID integer[], --list of hints for the question
  Locations integer[], --the location corresponses to the hints
  Ranking integer, --ranking of the question in leaderboard  
  PRIMARY KEY (`question_id`)
);

/*
-- Requests table --
Contains the information about the requests. There will be one row for each request.
*/
CREATE TABLE Request(
  QuestionID integer NOT null, --the ID of the question
  RequesterID integer NOT null, --the ID of the user who sent request
  RequestForID integer NOT null, --the ID of the user who received the request
  Primary Key(QuestionID,RequesterID,RequestForID) --primary key to ensure no duplication of information 
);

--------------------------------------------------------------------------------------------------

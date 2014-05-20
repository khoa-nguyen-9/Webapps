/*
Tables for the group project.

The definitions can be loaded by running the command:
  psql my_database_name
At the psql prompt you then type:
  \i trader.sql
Ignore any errors from the DROP commands 
(these commands a just removing tables from your database if you have previously set up the database).
  
You can use:   
  pg_dump -f filename database
to save the data and definition of a database in this format. If you want to make complex changes to 
the database you may find it easiest to edit the definitions below and reload this file.
*/

--------------------------------------------------------------------------------------------------
-- First we tidy up earlier versions of the tables if they exist to avoid conflicts.
DROP TABLE Question;
DROP TABLE Users;
DROP TABLE Request;
DROP TABLE Hint;
DROP TABLE Location;

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
  UserID integer Primary Key, --unique user ID
  UserName text unique not null, --unique username for each user
  UserPassword text unique not null, --unique password for user
  Credits double precision not null DEFAULT 10, --credit of the user 
  QuestionID integer[], --list of questions for the user 
  AnsweredQuestionID integer[], --list of questions that have been answered by the user  
  LocationID integer[] --list of locations the user has checked in 
);

/*
-- Questions table --
Contains the information about the questions. There will be one row for each question.
*/
CREATE TABLE Question(
  QuestionID integer Primary Key, --unique question ID
  Content text, --the content of the question
  Makers integer, --user ID of the user who made the question, default is admin
  HintID integer[], --list of hints for the question
  Locations integer[], --the location corresponses to the hints
  Ranking integer --ranking of the question in leaderboard  
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

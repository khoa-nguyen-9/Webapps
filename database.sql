/*
Tables for the trader group project.

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
DROP TABLE Questions;
DROP TABLE Users;
DROP TABLE Requests;
DROP TABLE Hints;
DROP TABLE Locations;
DROP TABLE UserQuestionMatching;

--------------------------------------------------------------------------------------------------
-- Next we create the tables for our database.
/*
-- Users table --
Contains the information about the user. There will be one row for each user.
*/
CREATE TABLE Users (
  UserID integer Primary Key, --unique user ID
  UserName text not null UNIQUE, --unique username for each user
  UserPassword text not null UNIQUE, --unique password for user
  Credits double precision NOT NULL,
  QuestionID integer[] References Questions --list of question for that user 
);

/*
-- Questions table --
Contains the information about the questions. There will be one row for each question.
*/
CREATE TABLE Questions (
  QuestionID integer Primary Key, --unique question ID
  Content text, --the content of the question
  Makers integer References Users, --user ID of the user who made the question, default is admin
  HintID integer[] References Hints, --list of hints for the question
  Locations double precision[][2], --the location corresponses to the hints
  Ranking integer --ranking of the question in leaderboard  
);

/*
-- Hints table --
Contains the information about the hints. There will be one row for each hint.
*/
CREATE TABLE Hints(
  HintID integer Primary Key, --unique hint ID
  Content text --the content of the hint 
);

/*
-- Requests table --
Contains the information about the requests. There will be one row for each request.
*/
CREATE TABLE Requests(
  QuestionID integer NOT null References Questions, --the ID of the question
  RequesterID integer NOT null References Users, --the ID of the user who sent request
  RequestForID integer NOT null References Users, --the ID of the user who received the request
  Primary Key(QuestionID,RequesterID,RequestForID) --primary key to ensure no duplication of information 
);

--------------------------------------------------------------------------------------------------

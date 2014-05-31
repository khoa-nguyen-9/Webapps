<?php

class DB_Question_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new question
     * returns question details
     */
    public function storeQuestion($maker, $qcontent, $qranking, $answer, $rewards, $base, $publish, $hints) {
        $result = mysql_query("INSERT INTO question(maker, qcontent, qranking, answer, rewards, base, publish, hints) VALUES('$maker', '$qcontent', '$qranking', '$answer', '$rewards', '$base', '$publish', '$hints')");
        // check for successful store
        if ($result) {
            // get question details
            $qid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM question WHERE qid = '$qid'") or die(mysql_error());
            // return question details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

   /**
     * Get question by qid
     */
    public function getQuestionByID($qid) {
        $result = mysql_query("SELECT * FROM question WHERE qid = '$qid'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // question not found
            return false;
        }
    }
	
   /**
     * Get question by maker
     */
    public function getQuestionByMaker($maker) {
        $result = mysql_query("SELECT * FROM question WHERE maker = '$maker'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return $result;
        } else {
            // question not found
            return false;
        }
    }
	
   /**
     * Get question by maker
     */
    public function getQuestionByRanking($qranking) {
        $result = mysql_query("SELECT * FROM question WHERE qranking = '$qranking'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return $result;
        } else {
            // question not found
            return false;
        }
    }
	
	/**
     * Get question by maker
     */
    public function getQuestionByBase($base) {
        $result = mysql_query("SELECT * FROM question WHERE base = '$base'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return $result;
        } else {
            // question not found
            return false;
        }
    }
}

?>

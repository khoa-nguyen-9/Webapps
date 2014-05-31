<?php

class DB_Userinfo_Functions {

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
    public function storeUserinfo($uid, $friends, $answered, $checked, $chints, $challenge) {
        $result = mysql_query("INSERT INTO userinfo(uid, friends, answered, checked, chints, challenge) VALUES('$uid', '$friends', '$answered', '$checked', '$chints', '$challenge')");
        // check for successful store
        if ($result) {
            // get userinfo details
            $result = mysql_query("SELECT * FROM userinfo WHERE uid = '$uid'") or die(mysql_error());
            // return userinfo details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

   /**
     * Get userinfo by uid
     */
    public function getUserinfoByID($uid) {
        $result = mysql_query("SELECT * FROM userinfo WHERE uid = '$uid'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // userinfo not found
            return false;
        }
    }
	
   /**
     * Update userinfo 
     */
    public function updateInfo($uid, $field, $newinfo) {
		$result = mysql_query("UPDATE userinfo SET $field = '$newinfo' WHERE uid = $uid");
        // check for successful store
        if ($result) {
            // get userinfo details
            $result = mysql_query("SELECT * FROM userinfo WHERE uid = '$uid'") or die(mysql_error());
            // return userinfo details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
	
}

?>
	
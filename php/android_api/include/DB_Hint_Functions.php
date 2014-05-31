<?php

class DB_Hint_Functions {

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
     * Storing new hint
     * returns hint details
     */
    public function storeHint($lid, $hcontent) {
        $result = mysql_query("INSERT INTO hint(lid, hcontent) VALUES('$lid', '$hcontent')");
        // check for successful store
        if ($result) {
            // get hint details
            $hid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM hint WHERE hid = $hid");
            // return hint details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

   /**
     * Get hint by hid
     */
    public function getHintByID($hid) {
        $result = mysql_query("SELECT * FROM hint WHERE hid = '$hid'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // hint not found
            return false;
        }
    }
}

?>

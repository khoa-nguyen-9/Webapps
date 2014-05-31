<?php

class DB_Location_Functions {

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
     * Storing new location
     * returns location details
     */
    public function storeLocation($x, $y, $lname) {
        $result = mysql_query("INSERT INTO location(x, y, lname) VALUES('$x', '$y', '$lname')");
        // check for successful store
        if ($result) {
            // get location details
            $lid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM location WHERE lid = '$lid'") or die(mysql_error());
            // return location details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
	
	/**
     * Storing new location
     * returns location details
     */
    public function storeLocationByName($lname) {
        $result = mysql_query("INSERT INTO location(lname) VALUES('$lname')");
        // check for successful store
        if ($result) {
            // get location details
            $lid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM location WHERE lid = '$lid'") or die(mysql_error());
            // return location details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
	

   /**
     * Get location by lid
     */
    public function getLocationByID($lid) {
        $result = mysql_query("SELECT * FROM location WHERE lid = '$lid'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // location not found
            return false;
        }
    }
	
    /**
     * Check user is existed or not
     */
    public function isLocationExisted($lname) {
        $result = mysql_query("SELECT * FROM location WHERE lname = '$lname'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // location not found
            return false;
        }
    }
    }

?>

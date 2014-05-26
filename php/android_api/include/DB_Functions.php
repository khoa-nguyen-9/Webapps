<?php

class DB_Functions {

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
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO USERS(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM USERS WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Storing new location
     * returns location details
     */
    public function storeLocation($x, $y, $name) {
        $uuid = uniqid('', true);
        $result = mysql_query("INSERT INTO LOCATION(loc_id, x, y, name) VALUES('$uuid', '$x', '$y', '$name')");
        // check for successful store
        if ($result) {
            // get user details
            $lid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM LOCATION WHERE loc_id = $lid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Storing new hint
     * returns hint details
     */
    public function storeHint($loc_id, $content) {
        $uuid = uniqid('', true);
        $result = mysql_query("INSERT INTO HINT(hint_id, loc_id, content) VALUES('$uuid', '$loc_id', '$content')");
        // check for successful store
        if ($result) {
            // get user details
            $hid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM HINT WHERE hint_id = $hid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Storing new Question
     * returns question details
     */
    public function storeQuestion($maker_id, $content, $ranking, $answer, $reward) {
        $uuid = uniqid('', true);
        $result = mysql_query("INSERT INTO QUESTION(quest_id, maker_id, content, ranking, answer, reward) VALUES('$uuid', '$maker_id', '$content', '$ranking', '$answer', '$reward')");
        // check for successful store
        if ($result) {
            // get user details
            $qid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM LOCATION WHERE hint_id = $qid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM USERS WHERE email = '$email'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

   /**
     * Get location by loc_id
     */
    public function getUserByEmailAndPassword($loc_id) {
        $result = mysql_query("SELECT * FROM LOCATION WHERE loc_id = '$loc_id'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) { 
            return mysql_fetch_array($result);
        } else {
            // user not found
            return false;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from USERS WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>

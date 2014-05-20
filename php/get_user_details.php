<?php
 
/*
 * Following code will get single user details
 * A user is identified by user id (Userid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["userid"])) {
    $id = $_GET['userid'];
 
    // get a user from users table
    $result = mysql_query("SELECT *FROM users WHERE userid = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $user = array();
            $user["userID"] = $result["userid"];
            $user["username"] = $result["username"];
            $user["userPassword"] = $result["userpassword"];
            $user["credits"] = $result["credits"];
            $user["questionid"] = $result["questionid"];
            $user["answeredquestionid"] = $result["answeredquestionid"];
	    $user["locationid"] = $result["locationid"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["user"] = array();
 
            array_push($response["user"], $user);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no user found
            $response["success"] = 0;
            $response["message"] = "No user found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no user found
        $response["success"] = 0;
        $response["message"] = "No user found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>

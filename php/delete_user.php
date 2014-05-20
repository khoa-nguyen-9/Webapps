<?php
 
/*
 * Following code will delete a user from table
 * A user is identified by id (UserID)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['userid'])) {
    $id = $_POST['userid'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("DELETE FROM users WHERE userid = $id");
 
    // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "User successfully deleted";
 
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
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>

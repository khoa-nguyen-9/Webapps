<?php
 
/*
 * Following code will create a new user row
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['userid']) && isset($_POST['username']) && isset($_POST['userpassword'])) {
 
    $ID = $_POST['userid'];
    $name = $_POST['username'];
    $password = $_POST['userpassword'];
 
    // include db connect class
    require_once __DIR__ . '/db_config.php';
 
    // connecting to db
    $db = pg_connect( "$host $port $dbname $credentials"  );
    if(!$db){
      echo "Error : Unable to open database\n";
    } else {
      echo "Opened database successfully\n";
    }
 
    // postgresql inserting a new row

    $result = pg_query($db, "INSERT INTO users(userid, username, userpassword, credits) VALUES('$ID', '$name', '$password', 10)");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "user successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
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

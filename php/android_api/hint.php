<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 *
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request
 */

if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'include/DB_Hint_Functions.php';
    $db = new DB_Hint_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'getHintByID') {
        // Request type is get hint
        $hid = $_POST['hid'];
 
        // check for hint
        $hint = $db->getHintByID($hid);
        if ($hint != false) {
            // hint found
            // echo json with success = 1
            $response["success"] = 1;
            $response["hint"]["hid"] = $hint["hid"];
            $response["hint"]["lid"] = $hint["lid"];
            $response["hint"]["hcontent"] = $hint["hcontent"];
            echo json_encode($response);
        } else {
            // hint not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect hint id!";
            echo json_encode($response);
        }
    } else if ($tag == 'storeHint') {
        // Request type is store hint
        $lid = $_POST['lid'];
        $hcontent = $_POST['hcontent'];

        // store hint
        $hint = $db->storeHint($lid, $hcontent);
        if ($hint) {
            // hint stored successfully
            $response["success"] = 1;
	    $response["hint"]["hid"] = $hint["hid"];
            $response["hint"]["lid"] = $hint["lid"];
            $response["hint"]["hcontent"] = $hint["hcontent"];
            echo json_encode($response);
        } else {
            // hint failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in storing hint";
            echo json_encode($response);
        }
        
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>	
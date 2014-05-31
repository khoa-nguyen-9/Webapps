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
    require_once 'include/DB_Location_Functions.php';
    $db = new DB_Location_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'getLocationByID') {
        // Request type is get location
        $lid = $_POST['lid'];
 
        // check for location
        $location = $db->getLocationByID($lid);
        if ($location != false) {
            // location found
            // echo json with success = 1
            $response["success"] = 1;
            $response["location"]["lid"] = $location["lid"];
            $response["location"]["x"] = $location["x"];
            $response["location"]["y"] = $location["y"];
            $response["location"]["lname"] = $location["lname"];
            echo json_encode($response);
        } else {
            // location not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect location details!";
            echo json_encode($response);
        }
    } else if ($tag == 'storeLocation') {
        // Request type is store location
        $x = $_POST['x'];
        $y = $_POST['y'];
        $lname = $_POST['lname'];
 
	$location = $db->isLocationExisted($lname);
		
        // check if location is already existed
        if ($location != false) {
            // location is already existed
	    $response["success"] = 1;
	    $response["location"]["lid"] = $location["lid"];
            $response["location"]["x"] = $location["x"];
            $response["location"]["y"] = $location["y"];
            $response["location"]["lname"] = $location["lname"];
            echo json_encode($response);
        } else {
            // store location
            $location = $db->storeLocation($x, $y, $lname);
            if ($location) {
                // location stored successfully
                $response["success"] = 1;
		$response["location"]["lid"] = $location["lid"];
                $response["location"]["x"] = $location["x"];
                $response["location"]["y"] = $location["y"];
                $response["location"]["lname"] = $location["lname"];
                echo json_encode($response);
            } else {
                // location failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in storing location";
                echo json_encode($response);
            }
        }
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>
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
    require_once 'include/DB_Userinfo_Functions.php';
    $db = new DB_Userinfo_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'getUserinfoByID') {
        // Request type is get userinfo
        $uid = $_POST['uid'];
 
        // check for userinfo
        $userinfo = $db->getUserinfoByID($uid);
        if ($userinfo != false) {
            // userinfo found
            // echo json with success = 1
            $response["success"] = 1;
            $response["userinfo"]["uid"] = $userinfo["uid"];
            $response["userinfo"]["friends"] = $userinfo["friends"];
            $response["userinfo"]["answered"] = $userinfo["answered"];
			$response["userinfo"]["checked"] = $userinfo["checked"];
            $response["userinfo"]["chints"] = $userinfo["chints"];
            $response["userinfo"]["challenge"] = $userinfo["challenge"];
			$response["userinfo"]["frequest"] = $userinfo["frequest"];
			$response["userinfo"]["lrequest"] = $userinfo["lrequest"];
			$response["userinfo"]["hrequest"] = $userinfo["hrequest"];
			$response["userinfo"]["x"] = $userinfo["x"];
			$response["userinfo"]["y"] = $userinfo["y"];
			$response["userinfo"]["lname"] = $userinfo["lname"];
			$response["userinfo"]["requester"] = $userinfo["requester"];
			$response["userinfo"]["credits"] = $userinfo["credits"];
			$response["userinfo"]["username"] = $userinfo["username"];
            echo json_encode($response);
        } else {
            // userinfo not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect userinfo id!";
            echo json_encode($response);
        }
	} else if ($tag == 'getAllUserinfo') {
		// check for question
        $result = $db->getAllUserinfo();
		if (mysql_num_rows($result) > 0) {
            // userinfo found
            // echo json with success = 1
            $response["success"] = 1;
            $response["userinfos"] = array();
            while ($row = mysql_fetch_array($result)) {
				// temp userinfo array
				$userinfo = array();
				$userinfo["uid"] = $row["uid"];
				$userinfo["friends"] = $row["friends"];
				$userinfo["answered"] = $row["answered"];
				$userinfo["qranking"] = $row["qranking"];
				$userinfo["checked"] = $row["checked"];
				$userinfo["chints"] = $row["chints"];
				$userinfo["challenge"] = $row["challenge"];
                $userinfo["frequest"] = $row["frequest"];
                $userinfo["lrequest"] = $row["lrequest"];
				$userinfo["hrequest"] = $row["hrequest"];
				$userinfo["x"] = $row["x"];
				$userinfo["y"] = $row["y"];
				$userinfo["lname"] = $row["lname"];
				$userinfo["requester"] = $row["requester"];
				$userinfo["credits"] = $row["credits"];
				$userinfo["username"] = $row["username"];
				// push single question into final response array
				array_push($response["userinfos"], $userinfo);
			}
            echo json_encode($response);
        } else {
            // question not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect question maker!";
            echo json_encode($response);
		}
    } else if ($tag == 'storeUserinfo') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		$username = $_POST['username'];
		
        // store userinfo
        $userinfo = $db->storeUserinfo($uid, '', '', '', '', '', '', '', '', '', '', '', '', 10, $username);
        if ($userinfo) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $userinfo["uid"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in storing userinfo";
            echo json_encode($response);
        }
        
	} else if ($tag == 'updateFriends') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update friends
		$friends = $userinfo["friends"] . ' ' . $_POST['friends'];
		
		$update = $db->updateInfo($uid, 'friends', $friends);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }	
	} else if ($tag == 'updateAnswered') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update answered
		$answered = $userinfo["answered"] . ' ' . $_POST['answered'];
		
		$update = $db->updateInfo($uid, 'answered', $answered);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }	
	} else if ($tag == 'updateChecked') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update checked
		$checked = $userinfo["checked"] . ' ' . $_POST['checked'];
		
		$update = $db->updateInfo($uid, 'checked', $checked);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }	
	} else if ($tag == 'updateChints') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update chints
		$chints = $userinfo["chints"] . ' ' . $_POST['chints'];
		
		$update = $db->updateInfo($uid, 'chints', $chints);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $updateo["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }	
	} else if ($tag == 'updateChallenge') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update challenge
		$challenge = $userinfo["challenge"] . ' ' . $_POST['challenge'];
		
		$update = $db->updateInfo($uid, 'challenge', $challenge);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }
	} else if ($tag == 'updateFrequest') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update challenge
		$frequest = $userinfo["frequest"] . ' ' . $_POST['frequest'];
		
		$update = $db->updateInfo($uid, 'frequest', $frequest);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }	
	} else if ($tag == 'updateLrequest') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update challenge
		$lrequest = $userinfo["lrequest"] . ' ' . $_POST['lrequest'];
		
		$update = $db->updateInfo($uid, 'lrequest', $lrequest);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }
	} else if ($tag == 'updateRequester') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update challenge
		$requester = $userinfo["requester"] . ' ' . $_POST['requester'];
		
		$update = $db->updateInfo($uid, 'requester', $requester);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }
	} else if ($tag == 'updateCredits') {
        // Request type is store userinfo
        $uid = $_POST['uid'];
		
        // get userinfo
        $userinfo = $db->getUserinfoByID($uid);
		
		// update challenge
		$credits = $userinfo["credits"] . ' ' . $_POST['credits'];
		
		$update = $db->updateInfo($uid, 'credits', $credits);
		
        if ($update) {
            // userinfo stored successfully
            $response["success"] = 1;
			$response["userinfo"]["uid"] = $update["uid"];
			$response["userinfo"]["friends"] = $update["friends"];
            $response["userinfo"]["answered"] = $update["answered"];
			$response["userinfo"]["checked"] = $update["checked"];
            $response["userinfo"]["chints"] = $update["chints"];
            $response["userinfo"]["challenge"] = $update["challenge"];
			$response["userinfo"]["frequest"] = $update["frequest"];
			$response["userinfo"]["lrequest"] = $update["lrequest"];
			$response["userinfo"]["requester"] = $update["requester"];
			$response["userinfo"]["credits"] = $update["credits"];
			$response["userinfo"]["username"] = $update["username"];
            echo json_encode($response);
        } else {
            // userinfo failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating userinfo";
            echo json_encode($response);
        }
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>	
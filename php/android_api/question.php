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
    require_once 'include/DB_Question_Functions.php';
    $db = new DB_Question_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'getQuestionByID') {
        // Request type is get question
        $qid = $_POST['qid'];
 
        // check for question
        $question = $db->getQuestionByID($qid);
        if ($question != false) {
            // question found
            // echo json with success = 1
            $response["success"] = 1;
            $response["question"]["qid"] = $question["qid"];
            $response["question"]["maker"] = $question["maker"];
            $response["question"]["qcontent"] = $question["qcontent"];
	    $response["question"]["qranking"] = $question["qranking"];
	    $response["question"]["answer"] = $question["answer"];
	    $response["question"]["rewards"] = $question["rewards"];
	    $response["question"]["base"] = $question["base"];
            $response["question"]["publish"] = $question["publish"];
            $response["question"]["hints"] = $question["hints"];
            echo json_encode($response);
        } else {
            // question not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect question id!";
            echo json_encode($response);
        }
    } else if ($tag == 'getQuestionByMaker') {
        // Request type is get question
        $maker = $_POST['maker'];
 
        // check for question
        $result = $db->getQuestionByMaker($maker);
        if (mysql_num_rows($result) > 0) {
			$response["questions"] = array();
            // question found
            // echo json with success = 1
            $response["success"] = 1;
            while ($row = mysql_fetch_array($result)) {
				// temp question array
				$question = array();
				$question["qid"] = $row["qid"];
				$question["maker"] = $row["maker"];
				$question["qcontent"] = $row["qcontent"];
				$question["qranking"] = $row["qranking"];
				$question["answer"] = $row["answer"];
				$question["rewards"] = $row["rewards"];
				$question["base"] = $row["base"];
                                $question["publish"] = $row["publish"];
                                $question["hints"] = $row["hints"];
				// push single question into final response array
				array_push($response["questions"], $question);
			}
            echo json_encode($response);
        } else {
            // question not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect question maker!";
            echo json_encode($response);
        }
	} else if ($tag == 'getQuestionByRanking') {
        // Request type is get question
        $qranking = $_POST['qranking'];
 
        // check for question
        $result = $db->getQuestionByRanking($qranking);
        if (mysql_num_rows($result) > 0) {
			$response["questions"] = array();
            // question found
            // echo json with success = 1
            $response["success"] = 1;
            while ($row = mysql_fetch_array($result)) {
				// temp question array
				$question = array();
				$question["qid"] = $row["qid"];
				$question["maker"] = $row["maker"];
				$question["qcontent"] = $row["qcontent"];
				$question["qranking"] = $row["qranking"];
				$question["answer"] = $row["answer"];
				$question["rewards"] = $row["rewards"];
				$question["base"] = $row["base"];
                                $question["publish"] = $row["publish"];
                                $question["hints"] = $row["hints"];
				// push single question into final response array
				array_push($response["questions"], $question);
			}
            echo json_encode($response);
        } else {
            // question not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect question maker!";
            echo json_encode($response);
        }
		
	} else if ($tag == 'getQuestionByBase') {
        // Request type is get question
        $base = $_POST['base'];
 
        // check for question
        $result = $db->getQuestionByBase($base);
        if (mysql_num_rows($result) > 0) {
			$response["questions"] = array();
            // question found
            // echo json with success = 1
            $response["success"] = 1;
            while ($row = mysql_fetch_array($result)) {
				// temp question array
				$question = array();
				$question["qid"] = $row["qid"];
				$question["maker"] = $row["maker"];
				$question["qcontent"] = $row["qcontent"];
				$question["qranking"] = $row["qranking"];
				$question["answer"] = $row["answer"];
				$question["rewards"] = $row["rewards"];
				$question["base"] = $row["base"];
                                $question["publish"] = $row["publish"];
                                $question["hints"] = $row["hints"];
				// push single question into final response array
				array_push($response["questions"], $question);
			}
            echo json_encode($response);
        } else {
            // question not found
            // echo json with error = 0
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect question maker!";
            echo json_encode($response);
        }	
	} else if ($tag == 'storeQuestion') {
        // Request type is store question
        $maker = $_POST['maker'];
		$qcontent = $_POST['qcontent'];
		$qranking = $_POST['qranking'];
		$answer = $_POST['answer'];
		$rewards = $_POST['rewards'];
		$base = $_POST['base'];
                $publish = $_POST['publish'];
                $hints = $_POST['hints'];

        // store question
        $question = $db->storeQuestion($maker, $qcontent, $qranking, $answer, $rewards, $base, $publish, $hints);
        if ($question) {
            // question stored successfully
            $response["success"] = 1;
            $response["question"]["qid"] = $question["qid"];
            $response["question"]["maker"] = $question["maker"];
            $response["question"]["qcontent"] = $question["qcontent"];
	    $response["question"]["qranking"] = $question["qranking"];
	    $response["question"]["answer"] = $question["answer"];
	    $response["question"]["rewards"] = $question["rewards"];
	    $response["question"]["base"] = $question["base"];
            $response["question"]["publish"] = $question["publish"];
            $response["question"]["hints"] = $question["hints"];
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in storing question";
            echo json_encode($response);
        }
        	
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>			
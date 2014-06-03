package library;
import java.util.ArrayList;
import java.util.List;
 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class QuestionFunctions {
 
    private JSONParser jsonParser;
 
    private static String questionURL = "http://khoatestsite.hostoi.com/android_api/question.php";
    
    private static String get_all_tag = "getAllQuestions";
    private static String get_by_ID_tag = "getQuestionByID";
    private static String get_by_maker_tag = "getQuestionByMaker";
    private static String get_by_ranking_tag = "getQuestionByRanking";
    private static String get_by_base_tag = "getQuestionByBase";
    private static String add_tag = "storeQuestion";
 
    private static String delete_tag = "deleteQuestion";
    
    // constructor
    public QuestionFunctions(){
        jsonParser = new JSONParser();
    }
    
    // get question details by ID
    public JSONObject getQuestionByID(String qid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_ID_tag));
        params.add(new BasicNameValuePair("qid", qid));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
    
    // get question details by maker
    public JSONObject getQuestionByMaker(String maker){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_maker_tag));
        params.add(new BasicNameValuePair("maker", maker));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
    
    // get all the questions details
    public JSONObject getQuestions(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_all_tag));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }    
    
    // get question details by ranking
    public JSONObject getQuestionByRanking(String qranking){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_ranking_tag));
        params.add(new BasicNameValuePair("qranking", qranking));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
    
    // get question details by base
    public JSONObject getQuestionByBase(String base){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_base_tag));
        params.add(new BasicNameValuePair("base", base));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
 
    
    // Add a new question to database
    public JSONObject addQuestion(String maker, String qcontent, String qranking, String answer, String rewards, String base, String publish, String hints){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("maker", maker));
        params.add(new BasicNameValuePair("qcontent", qcontent));
        params.add(new BasicNameValuePair("qranking", qranking));
        params.add(new BasicNameValuePair("answer", answer));
        params.add(new BasicNameValuePair("rewards", rewards));
        params.add(new BasicNameValuePair("base", base));
        params.add(new BasicNameValuePair("publish", publish));
        params.add(new BasicNameValuePair("hints", hints));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        // return json
        return json;
    }
    
    // Delete a question on database
    public JSONObject deleteQuestion(String qid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", delete_tag));
        params.add(new BasicNameValuePair("qid", qid));
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
 
}
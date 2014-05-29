package library;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;
 
public class QuestionFunctions {
 
    private JSONParser jsonParser;
 
    private static String questionURL = "http://khoatestsite.hostoi.com/android_api/";

    private static String getquestion_tag = "getQuestion";
    private static String addquestion_tag = "addQuestion";
 
    // constructor
    public QuestionFunctions(){
        jsonParser = new JSONParser();
    }
    
     //login with user provided email/pass
    public JSONObject getQuestion(String qid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getquestion_tag));
        params.add(new BasicNameValuePair("qid", qid));
        Log.v("questionFunction", "getQuestion");
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        return json;
    }
 
    //register a new user with name/email/pass
    public JSONObject addQuestion(String maker, String qcontent, String qranking, String answer, String rewards){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", addquestion_tag));
        params.add(new BasicNameValuePair("maker", maker));
        params.add(new BasicNameValuePair("qcontent", qcontent));
        params.add(new BasicNameValuePair("qranking", qranking));
        params.add(new BasicNameValuePair("answer", answer));
        params.add(new BasicNameValuePair("rewards", rewards));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(questionURL, params);
        // return json
        return json;
    }
 
}
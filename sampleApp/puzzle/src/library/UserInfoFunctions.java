package library;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class UserInfoFunctions {
 
    private JSONParser jsonParser;
 
    private static String userinfoURL = "http://khoatestsite.hostoi.com/android_api/userinfo.php";
    
    private static String get_tag = "getUserinfoByID";
    private static String get_all_tag = "getAllUserinfo";
    private static String update_friends_tag = "updateFriends";
    private static String update_answered_tag = "updateAnswered";
    private static String update_checked_tag = "updateChecked";
    private static String update_chints_tag = "updateChints";
    private static String update_challenge_tag = "updateChallenge";
    private static String update_frequest_tag = "updateFrequest";
    private static String update_lrequest_tag = "updateLrequest";
    private static String update_requester_tag = "updateRequester";
    private static String update_credits_tag = "updateCredits";
    
    private static String add_tag = "storeUserinfo";
    
    // constructor
    public UserInfoFunctions(){
        jsonParser = new JSONParser();
    }
    
    // get user details by ID
    public JSONObject getUserInfoByID(String uid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // get all the userinfo details
    public JSONObject getAllUserInfo(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_all_tag));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    } 
    
    // update user's friends
    public JSONObject updateFriends(String uid, String friends){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_friends_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("friends", friends));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's answered
    public JSONObject updateAnswered(String uid, String answered){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_answered_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("answered", answered));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's checked
    public JSONObject updateChecked(String uid, String checked){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_checked_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("checked", checked));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's chints
    public JSONObject updateChints(String uid, String chints){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_chints_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("chints", chints));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's challenge
    public JSONObject updateChallenge(String uid, String challenge){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_challenge_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("challenge", challenge));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's friend request
    public JSONObject updateFrequest(String uid, String frequest){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_frequest_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("frequest", frequest));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's location request
    public JSONObject updateLrequest(String uid, String lrequest){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_lrequest_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("lrequest", lrequest));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's location request
    public JSONObject updateRequester(String uid, String requester){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_requester_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("requester", requester));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // update user's credits
    public JSONObject updateCredits(String uid, String credits){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", update_credits_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("credits", credits));
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        return json;
    }
    
    // Add new user to database, only call once when user register
    public JSONObject addUser(String uid, String username){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("username", username));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(userinfoURL, params);
        // return json
        return json;
    }
 
}
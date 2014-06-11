package library;
import java.util.ArrayList;
import java.util.List;
 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class MessageFunctions {
 
    private JSONParser jsonParser;
 
    private static String messageURL = "http://khoatestsite.hostoi.com/android_api/message.php";
    
    private static String get_all_tag = "getAllMessages";
    private static String get_by_sender_tag = "getMessageBySender";
    private static String get_by_receiver_tag = "getMessageByReceiver";
    private static String add_tag = "storeMessage";
    
    // constructor
    public MessageFunctions(){
        jsonParser = new JSONParser();
    }
    
    // get message details by maker
    public JSONObject getMessageBySender(String sender){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_sender_tag));
        params.add(new BasicNameValuePair("sender", sender));
        JSONObject json = jsonParser.getJSONFromUrl(messageURL, params);
        return json;
    }
    
    // get message details by receiver
    public JSONObject getMessageByReceiver(String receiver){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_by_receiver_tag));
        params.add(new BasicNameValuePair("receiver", receiver));
        JSONObject json = jsonParser.getJSONFromUrl(messageURL, params);
        return json;
    }
    
    // get all the questions details
    public JSONObject getMessages(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_all_tag));
        JSONObject json = jsonParser.getJSONFromUrl(messageURL, params);
        return json;
    }        

    // Add a new question to database
    public JSONObject addMessage(String sender, String receiver, String mcontent){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("sender", sender));
        params.add(new BasicNameValuePair("receiver", receiver));
        params.add(new BasicNameValuePair("mcontent", mcontent));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(messageURL, params);
        // return json
        return json;
    }
 
}
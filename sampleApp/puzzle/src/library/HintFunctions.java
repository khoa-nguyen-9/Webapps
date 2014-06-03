package library;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class HintFunctions {
 
    private JSONParser jsonParser;
 
    private static String hintURL = "http://khoatestsite.hostoi.com/android_api/hint.php";
    
    private static String get_tag = "getHintByID";
    private static String add_tag = "storeHint";
 
    // constructor
    public HintFunctions(){
        jsonParser = new JSONParser();
    }
    
    // Get the location by providing id
    public JSONObject getHint(String hid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_tag));
        params.add(new BasicNameValuePair("hid", hid));
        JSONObject json = jsonParser.getJSONFromUrl(hintURL, params);
        return json;
    }
 
    // Add a new hint to database
    public JSONObject addHint(String lid, String hcontent){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("lid", lid));
        params.add(new BasicNameValuePair("hcontent", hcontent));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(hintURL, params);
        // return json
        return json;
    }
 
}
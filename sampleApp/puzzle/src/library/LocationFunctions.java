package library;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
public class LocationFunctions {
 
    private JSONParser jsonParser;
 
    private static String locationURL = "http://khoatestsite.hostoi.com/android_api/location.php";
    
    private static String get_tag = "getLocationByID";
    private static String add_tag = "storeLocation";
 
    // constructor
    public LocationFunctions(){
        jsonParser = new JSONParser();
    }
    
    // Get the location by providing id
    public JSONObject getLocation(String lid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_tag));
        params.add(new BasicNameValuePair("lid", lid));
        JSONObject json = jsonParser.getJSONFromUrl(locationURL, params);
        return json;
    }
 
    // Add new user to database, return ID
    public JSONObject addQuestion(String x, String y, String lname){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_tag));
        params.add(new BasicNameValuePair("x", x));
        params.add(new BasicNameValuePair("y", y));
        params.add(new BasicNameValuePair("lname", lname));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(locationURL, params);
        // return json
        return json;
    }
 
}
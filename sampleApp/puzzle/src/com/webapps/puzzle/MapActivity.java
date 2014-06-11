package com.webapps.puzzle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import library.DatabaseHandler;
import library.UserFunctions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import directions.Routing;
import directions.RoutingListener;

public class MapActivity extends Activity 
                         implements android.location.LocationListener,
                                    OnMarkerClickListener,
                                    OnInfoWindowClickListener,
                                    OnMapClickListener{
                                  
	
	private GoogleMap map;
	private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
	private LocationManager locationManager;
	private Marker userLocation;
	private Marker[] placeMarkers;
	private Marker[] locationMarkers;
	private final int MAX_PLACES = 20; //maximum number of places returned by Google Places API
	private MarkerOptions[] places; //to hold details of each marker
	LatLng lastLatLng,destination;
	Polyline polyLine;
	Marker startMarker,destinationMarker, randomMarker;
	Place hint1, hint2,hint3;
	Marker firstMarker,secondMarker,thirdMarker;
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_CHECKED = "checked";
	private static String KEY_CHINTS = "chints";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
	
	protected DatabaseHandler dbHandler;
    private HashMap<String, String> user;
	private User currentUser;
	
	private final int askCredits = 5;
	
	private Mode mode;
	
	private enum Mode {
		PLACES,
		HINTS,
		LOCATIONS,
		FRIEND_PLACES
	}
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_map);
	    
	    userIcon = R.drawable.yellow_point;
	    foodIcon = R.drawable.red_point;
	    drinkIcon = R.drawable.blue_point;
	    shopIcon = R.drawable.green_point;
	    otherIcon = R.drawable.purple_point;
	    
	    
	    
	    
	    if(map == null){
	    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
	    	if(map != null){
	    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    		
	    		
	    		map.setOnMarkerClickListener((OnMarkerClickListener) this);
	    		map.setOnInfoWindowClickListener((OnInfoWindowClickListener)this);
	    		map.setOnMapClickListener((OnMapClickListener) this);
	    		
	    		
	    		placeMarkers = new Marker[MAX_PLACES];
	    		
	    		User user = (User) getIntent().getSerializableExtra("user");
	    		String modeType = getIntent().getStringExtra("mode");
	    		
	    		if(modeType.equals("places")){
	    			mode = Mode.PLACES;
	    			updatePlaces();
	    		}else if(modeType.equals("hints")){
	    			mode = Mode.HINTS;
	    			getCurrentLocation();
	    			putHints();
	    		}else if(modeType.equals("locations")) {
	    			mode = Mode.LOCATIONS;
	    			if(user != null){
	    				getCurrentLocation();
		    			putRequestedLocations();
		    		}
	    		}else if(modeType.equals("friendPlaces")){
	    			mode = Mode.FRIEND_PLACES;
	    			getCurrentLocation();
	    			putFriendPlaces();
	    		}
	    		
	    		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 15));
	    		/*
	    		
	    		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    		*/
	    		
	    		/*secondMarker = map.addMarker(new MarkerOptions()
				.position(new LatLng(userLocation.getPosition().latitude - 0.000005,
				userLocation.getPosition().longitude-0.000005))
				.title("Hint Check In Test")
				.icon(BitmapDescriptorFactory.fromResource(shopIcon))
				.snippet("Location a hint"));*/
	    		
	    	}
	    }
	  }
	  
	  
	 

	private void putFriendPlaces() {
		int num = getIntent().getIntExtra("numberPlaces", 0);
		locationMarkers = new Marker[MAX_PLACES];
		
		for(int i = 0; i < num && i < MAX_PLACES; i++ ){
			Place place = (Place) getIntent().getSerializableExtra("place"+i);
			
			LatLng ll = new LatLng(place.getX(),place.getY());
			locationMarkers[i] = map.addMarker(new MarkerOptions()
			.position(ll)
			.title(place.getName())
			.icon(BitmapDescriptorFactory.fromResource(foodIcon))
			.snippet("Your friend has been here."));
		}
		
	}




	private void putRequestedLocations() {
		
		int num = getIntent().getIntExtra("checked", 0);
        User user = (User) getIntent().getSerializableExtra("user");

		locationMarkers = new Marker[MAX_PLACES];
		
		for(int i = 0; i < num && i < MAX_PLACES; i++ ){
			User locReq = (User) getIntent().getSerializableExtra("requester"+i);
	        Hint hint = (Hint) getIntent().getSerializableExtra("hint"+i);
	        Place place = (Place) getIntent().getSerializableExtra("place"+i);
	        
	        
	        LatLng ll = new LatLng(place.getX(),place.getY());
			locationMarkers[i] = map.addMarker(new MarkerOptions()
			.position(ll)
			.title(place.getName())
			.icon(BitmapDescriptorFactory.fromResource(foodIcon))
			.snippet("Requested by " + locReq.getUsername()));
		}
		
	}


	private void putHints() {
		Intent intent = getIntent();
		if(intent != null){
			
			Hint hint = (Hint) intent.getSerializableExtra("hint0");
			Place place = (Place) intent.getSerializableExtra("place0");
			
			Hint hint1 = (Hint) intent.getSerializableExtra("hint1");
			Place place1 = (Place) intent.getSerializableExtra("place1");
			
			Hint hint2 = (Hint) intent.getSerializableExtra("hint2");
			Place place2 = (Place) intent.getSerializableExtra("place2");
			
			
			if(place.getName().equals("random")){
				getRandom();
				
				
			}
			else{LatLng firstHint = new LatLng(place.getX(),place.getY());
				firstMarker = map.addMarker(new MarkerOptions()
				.position(firstHint)
				.title(place.getName())
				.icon(BitmapDescriptorFactory.fromResource(foodIcon))
				.snippet("Location of the first hint"));
			}
			LatLng secondHint = new LatLng(place1.getX(),place1.getY());
			secondMarker = map.addMarker(new MarkerOptions()
			.position(secondHint)
			.title(place1.getName())
			.icon(BitmapDescriptorFactory.fromResource(shopIcon))
			.snippet("Location of the second hint"));	
		
			LatLng thirdHint = new LatLng(place2.getX(),place2.getY());
			thirdMarker = map.addMarker(new MarkerOptions()
			.position(thirdHint)
			.title(place2.getName())
			.icon(BitmapDescriptorFactory.fromResource(otherIcon))
			.snippet("Location of the third hint"));
			
			}
	}
	
	
	private void getCurrentLocation(){
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//get last location
		
		double lat,lng;
		
		Location lastLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(lastLoc != null){
			lat = lastLoc.getLatitude();
			lng = lastLoc.getLongitude();
			lastLatLng= new LatLng(lat, lng);
		}else{
			lat = 51.4983;
			lng = -0.1769;
			LatLng LONDON = new LatLng(51.4983, -0.1769);
			lastLatLng = LONDON; //default Location when a previous location has not been registered yet
		}
		//remove any existing marker
		if(userLocation!=null) userLocation.remove();
		//create and set marker properties
		userLocation = map.addMarker(new MarkerOptions()
		.position(lastLatLng)
		.title("You are here")
		.icon(BitmapDescriptorFactory.fromResource(userIcon))
		.snippet("Your last recorded location"));
	}
	
	
	//update location
	private void getRandom(){
		getCurrentLocation();
		
		double lat = lastLatLng.latitude;
		double lng = lastLatLng.longitude;
		
		String types = "food|bar|store|museum|art_gallery";
		try {
		types = URLEncoder.encode(types, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		//query for places, with parameters,
		//excepting json data back
		String placesSearchStr = 
				"https://maps.googleapis.com/maps/api/place/nearbysearch/" +
				"json?location="+lat+","+lng+
				"&radius=1000&sensor=true" +
				"&types="+ types +
			    "&key=AIzaSyBlqzRYkapvxdgv7mtsTiVxQR1iod-jSzk";
	
		new GetRandomPlace().execute(placesSearchStr);                                                      
		 locationManager.requestLocationUpdates(
				 LocationManager.NETWORK_PROVIDER, 
				 30000, 100, this);
	}
	

	//update location
	private void updatePlaces(){
		getCurrentLocation();
		
		double lat = lastLatLng.latitude;
		double lng = lastLatLng.longitude;
		
		String types = "food|bar|store|museum|art_gallery";
		try {
		types = URLEncoder.encode(types, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		//query for places, with parameters,
		//excepting json data back
		String placesSearchStr = 
				"https://maps.googleapis.com/maps/api/place/nearbysearch/" +
				"json?location="+lat+","+lng+
				"&radius=1000&sensor=true" +
				"&types="+ types +
			    "&key=AIzaSyBlqzRYkapvxdgv7mtsTiVxQR1iod-jSzk"; 
		 
		new GetPlaces().execute(placesSearchStr);                                                      
		 locationManager.requestLocationUpdates(
				 LocationManager.NETWORK_PROVIDER, 
				 30000, 100, this); //performnce issues with this method, consider requestSingleUpdate()
		 
		 
	}
	
  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId())
	    {
	    case R.id.logout:
	    	new UserFunctions().logoutUser(this);
	        Intent login = new Intent(this, LoginActivity.class);
	        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(login);
	        finish();
	    	return true;
	    case R.id.how_to_play:
	    	Intent howTo = new Intent(getApplicationContext(), HowToPlayActivity.class);
	        startActivity(howTo);
            return true;
	    case R.id.action_settings:
	    	Intent settings = new Intent(getApplicationContext(), Settings.class);
	        startActivity(settings);
            return true;
	    case R.id.about:
	    	Intent about = new Intent(getApplicationContext(), AboutScreen.class);
	        startActivity(about);
            return true;
        default:
        	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(map != null){
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 30000, 100, this);
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(map!=null){
			locationManager.removeUpdates(this);
			
		}
	}
	
	private class GetPlaces extends AsyncTask<String,Void, String>{
		//first param: search URL as a string. 
		//second param: void as we don't indicate progress
		//third type: background op returns a JSON string representing places
		private int lid;
		private String content;
		
		//fetch and parse places data
		@Override
		protected String doInBackground(String... placesURL) {
			
			
			
			//fetch places
			StringBuilder placesBuilder = new StringBuilder();
			for(String placeSearchURL : placesURL){
				//execute search
				HttpClient placesClient = new DefaultHttpClient();
				try{
					HttpGet placesGet = new HttpGet(placeSearchURL);
					HttpResponse placesResponse = placesClient.execute(placesGet);
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					if(placeSearchStatus.getStatusCode() == 200){
						//good response :)
						HttpEntity placesEntity = placesResponse.getEntity();
						InputStream placesContent = placesEntity.getContent();
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						BufferedReader placesReader = new BufferedReader(placesInput);
						
						//read input a line at a time, append to string builder
						String lineIn;
						while ((lineIn = placesReader.readLine()) != null){
							placesBuilder.append(lineIn);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			return placesBuilder.toString();
		}
		
		protected void onPostExecute(String result){
			//parsing the data from Google places
			for(int i = 0; i < placeMarkers.length; ++i){ //remove old places, when user changes location
				if(placeMarkers[i] != null){
					placeMarkers[i].remove();
				}
						
			}
			
			try{
				//parsing JSON from query response
				JSONObject resultObject = new JSONObject(result);
				JSONArray placesArray = resultObject.getJSONArray("results");
				places = new MarkerOptions[placesArray.length()];
				for(int i = 0; i<placesArray.length(); ++i){
					//parse each place for details to pass to MarkerOptions
					boolean missingValue = false;
					//details about each place
					LatLng placeLL = null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					
					try{ //if any value is missing, we dissmiss the place
						missingValue = false; //assuming all values are there, until found otherwise
						JSONObject placeObject = placesArray.getJSONObject(i);
						JSONObject loc = placeObject.getJSONObject("geometry").
								getJSONObject("location");
						placeLL = new LatLng(
								Double.valueOf(loc.getString("lat")),
								Double.valueOf(loc.getString("lng")));
						JSONArray types = placeObject.getJSONArray("types");
						
						for(int it = 0; it<types.length();++it){
							//get the appropriate type, to display accordingly
							String type = types.get(it).toString();
							if(type.contains("food")){
							    currIcon = foodIcon;
							    break;
							}
							else if(type.contains("bar")){
							    currIcon = drinkIcon;
							    break;
							}
							else if(type.contains("store")){
							    currIcon = shopIcon;
							    break;
							}
							
						}
						
						vicinity = placeObject.getString("vicinity");
						placeName = placeObject.getString("name");
						
						
					}catch(JSONException jse){
						missingValue = true;
						jse.printStackTrace();
					}
					
					if(missingValue){
						places[i] = null;
					}else{
						places[i] = new MarkerOptions()
						.position(placeLL)
						.title(placeName)
						.icon(BitmapDescriptorFactory.fromResource(currIcon))
						.snippet(vicinity);
					}
				}
				
						
			}catch (Exception e){
				e.printStackTrace();
			}
			
			if(places != null && placeMarkers!= null){
				for(int q = 0; 
				    q < places.length && q < placeMarkers.length; ++q ){
					if(places[q] != null){
						placeMarkers[q] = map.addMarker(places[q]);
					}
				}
			}
			
			
		}
		
	}
	
	
	
	private class GetRandomPlace extends AsyncTask<String,Void, String>{
		//first param: search URL as a string. 
		//second param: void as we don't indicate progress
		//third type: background op returns a JSON string representing places
		private int lid;
		private String content;
		
		//fetch and parse places data
		@Override
		protected String doInBackground(String... placesURL) {
			
			
			
			//fetch places
			StringBuilder placesBuilder = new StringBuilder();
			for(String placeSearchURL : placesURL){
				//execute search
				HttpClient placesClient = new DefaultHttpClient();
				try{
					HttpGet placesGet = new HttpGet(placeSearchURL);
					HttpResponse placesResponse = placesClient.execute(placesGet);
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					if(placeSearchStatus.getStatusCode() == 200){
						//good response :)
						HttpEntity placesEntity = placesResponse.getEntity();
						InputStream placesContent = placesEntity.getContent();
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						BufferedReader placesReader = new BufferedReader(placesInput);
						
						//read input a line at a time, append to string builder
						String lineIn;
						while ((lineIn = placesReader.readLine()) != null){
							placesBuilder.append(lineIn);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			return placesBuilder.toString();
		}
		
		protected void onPostExecute(String result){
			//parsing the data from Google places
			for(int i = 0; i < placeMarkers.length; ++i){ //remove old places, when user changes location
				if(placeMarkers[i] != null){
					placeMarkers[i].remove();
				}
						
			}
			
			try{
				//parsing JSON from query response
				JSONObject resultObject = new JSONObject(result);
				JSONArray placesArray = resultObject.getJSONArray("results");
				places = new MarkerOptions[placesArray.length()];
				for(int i = 0; i<placesArray.length(); ++i){
					//parse each place for details to pass to MarkerOptions
					boolean missingValue = false;
					//details about each place
					LatLng placeLL = null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					
					try{ //if any value is missing, we dissmiss the place
						missingValue = false; //assuming all values are there, until found otherwise
						JSONObject placeObject = placesArray.getJSONObject(i);
						JSONObject loc = placeObject.getJSONObject("geometry").
								getJSONObject("location");
						placeLL = new LatLng(
								Double.valueOf(loc.getString("lat")),
								Double.valueOf(loc.getString("lng")));
						JSONArray types = placeObject.getJSONArray("types");
						
						for(int it = 0; it<types.length();++it){
							//get the appropriate type, to display accordingly
							String type = types.get(it).toString();
							if(type.contains("food")){
							    currIcon = foodIcon;
							    break;
							}
							else if(type.contains("bar")){
							    currIcon = drinkIcon;
							    break;
							}
							else if(type.contains("store")){
							    currIcon = shopIcon;
							    break;
							}
							
						}
						
						vicinity = placeObject.getString("vicinity");
						placeName = placeObject.getString("name");
						
						
					}catch(JSONException jse){
						missingValue = true;
						jse.printStackTrace();
					}
					
					if(missingValue){
						places[i] = null;
					}else{
						places[i] = new MarkerOptions()
						.position(placeLL)
						.title(placeName)
						.icon(BitmapDescriptorFactory.fromResource(currIcon))
						.snippet(vicinity);
					}
				}
				
						
			}catch (Exception e){
				e.printStackTrace();
			}
			
			if(places != null && placeMarkers!= null){
				int index = 0;
				for(int q = 0; 
				    q < places.length && q < placeMarkers.length; ++q ){
					if(places[q] != null){
						index = q;
					}
				}
				if(index != 0){
					randomMarker = map.addMarker(places[index]);
					randomMarker.setIcon(BitmapDescriptorFactory.fromResource(foodIcon));
					randomMarker.setSnippet(randomMarker.getSnippet() + " Location of the first hint");
					firstMarker = randomMarker;
				}
			}
			
			
		}
		
	}
	
	
	
	
	
	
	
	

	@Override
	public void onLocationChanged(Location location) {
	    Log.v("MapActivity", "location changed");
	    
	    switch(mode){
	    case PLACES:
	    	updatePlaces();
	    	break;
	    case HINTS:
	    	getCurrentLocation();
			putHints();
	    	break;
	    case LOCATIONS:
	    	getCurrentLocation();
	    	break;
	    case FRIEND_PLACES:
	    	getCurrentLocation();
	    	break;
		default:
	    }
	}
	public void onProviderDisabled(String provider){
	    Log.v("MapActivity", "provider disabled");
	}
	public void onProviderEnabled(String provider) {
	    Log.v("MapActivity", "provider enabled");
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
	    Log.v("MapActivity", "status changed");
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(marker == null) return false;
		
		removeDirectionsMarkers();
	    marker.showInfoWindow();
	    
	    if(marker.equals(startMarker) || marker.equals(userLocation)){
	    	return true;
	    }
		
	    if(Math.abs(marker.getPosition().latitude - lastLatLng.latitude) < 0.0005
	    && Math.abs(marker.getPosition().longitude - lastLatLng.longitude) < 0.0005
	    && !marker.equals(startMarker) && !marker.equals(userLocation)){
			//removeDirectionsMarkers();
	    	if(mode == Mode.HINTS){
	
				int lid = 0;
				int hid = 0;
				Intent intent = getIntent();
		
				if(marker.equals(firstMarker)){
					lid = intent.getIntExtra("Lid1", 0);
					hid = intent.getIntExtra("Hid1", 0);
				}else if(marker.equals(secondMarker)){
					lid = intent.getIntExtra("Lid2", 0);
					hid = intent.getIntExtra("Hid2", 0);
				}else if(marker.equals(thirdMarker)){
					lid = intent.getIntExtra("Lid3", 0);
					hid = intent.getIntExtra("Hid3", 0);
				}
				
				
				
				// TODO: check in for other users
				
				Toast.makeText(this,"You have checked in at " + marker.getTitle(),
				    	Toast.LENGTH_LONG).show();
				dbHandler = new DatabaseHandler(getApplicationContext());
		        user = dbHandler.getUserDetails();
		        currentUser = new User(Integer.parseInt(user.get("uid")),
		        		          null,null,null,null,null,null,null,null,0,null);
		        currentUser.updateUserChecked(lid);
		        currentUser.updateUserChints(hid);
		        currentUser.addCredits(askCredits);
		        currentUser.updateUserCredits(currentUser.getCredits());
				return true;
			}else if(mode == Mode.LOCATIONS){
				currentUser.addCredits(5);
				currentUser.updateUserCredits(currentUser.getCredits());
				
				
				int num = getIntent().getIntExtra("checked", 0); //total number of locations
				
				for(int i =0; i < num; i++){
					if(marker.equals(locationMarkers[i])){
						User locReq = (User) getIntent().getSerializableExtra("requester"+i);
						Hint hint = (Hint) getIntent().getSerializableExtra("hint"+i);
						locReq.updateUserChints(hint.getHintID());
						locReq.addCredits(askCredits);
						locReq.updateUserCredits(locReq.getCredits());
						currentUser.updateUserChecked(hint.getHintID());
					}
				}
				
			}
	    }
	    
	    
	    
		destination = marker.getPosition(); 
		Routing routing = new Routing(Routing.TravelMode.WALKING);
		 //Log.v("start", lastLatLng.toString());
		 
		 if(destination != null){
		 Log.v("destination",destination.toString());
	     routing.registerListener(new RoutingListener(){

			@Override
			public void onRoutingFailure() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRoutingStart() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRoutingSuccess(PolylineOptions mPolyOptions) {
				  PolylineOptions polyoptions = new PolylineOptions();
			      polyoptions.color(Color.BLUE);
			      polyoptions.width(10);
			      polyoptions.addAll(mPolyOptions.getPoints());
			      polyLine = map.addPolyline(polyoptions);

			      
			      // Start marker
			      MarkerOptions options = new MarkerOptions();
			      options.position(lastLatLng);
			      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
			      startMarker = map.addMarker(options);

			      // End marker
			      options = new MarkerOptions();
			      options.position(destination);
			      options.title("hint location");
			      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green)); 
			      
			      destinationMarker = map.addMarker(options);
			}
	    	 
	     });
	     routing.execute(lastLatLng, destination);
	     
	     marker.hideInfoWindow();
	     Toast.makeText(this, "Directions to " + 
	                               marker.getTitle(), Toast.LENGTH_LONG).show();
	     
	     
		 }else{
			 Log.v("destination", "null");
		 }
		return true;
	}

	private void removeDirectionsMarkers() {
		if(polyLine != null && startMarker != null && destinationMarker != null){
			polyLine.remove();
			startMarker.remove();
			destinationMarker.remove();
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Toast.makeText(this,marker.getTitle(),
		    	Toast.LENGTH_SHORT).show();
		//TODO:
		//launch location info activity where users can talk
		//about that location, etc
	}

	@Override
	public void onMapClick(LatLng arg0) {
		removeDirectionsMarkers() ;
	}
}
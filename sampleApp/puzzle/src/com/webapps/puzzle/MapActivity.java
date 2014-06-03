package com.webapps.puzzle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
                                    OnInfoWindowClickListener{
                                  
	
	private GoogleMap map;
	private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
	private LocationManager locationManager;
	private Marker userLocation;
	private Marker[] placeMarkers;
	private final int MAX_PLACES = 20; //maximum number of places returned by Google Places API
	private MarkerOptions[] places; //to hold details of each marker
	LatLng lastLatLng;
	LatLng destination;
	Polyline polyLine;
	Marker startMarker;
	Marker destinationMarker;
	

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
	    		/*Marker london = map.addMarker(new MarkerOptions().position(LONDON)
	    		        .title("London"));
	    		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LONDON, 15));
	    		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    		*/
	    		
	    		map.setOnMarkerClickListener((OnMarkerClickListener) this);
	    		map.setOnInfoWindowClickListener((OnInfoWindowClickListener)this);
	    		
	    		
	    		placeMarkers = new Marker[MAX_PLACES];
	    		updatePlaces();
	    	}
	    }
	  }

	//update location
	private void updatePlaces(){
		//get location manager
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
		
		 /*
		  * The call to getLastKnownLocation() doesn't block -
		  *  which means it will return null if no position is currently available - 
		  *  so you probably want to have a look at passing a LocationListener to the 
		  *  requestLocationUpdates() method instead, which will give you asynchronous updates
		  *   of your location.

			private final LocationListener locationListener = new LocationListener() {
			    public void onLocationChanged(Location location) {
			        longitude = location.getLongitude();
			        latitude = location.getLatitude();
			    }
			}

			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locat
		  */
		
		//remove any existing marker
		if(userLocation!=null) userLocation.remove();
		//create and set marker properties
		userLocation = map.addMarker(new MarkerOptions()
		.position(lastLatLng)
		.title("You are here")
		.icon(BitmapDescriptorFactory.fromResource(userIcon))
		.snippet("Your last recorded location"));
		//move to location
		//map.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
		
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

	@Override
	public void onLocationChanged(Location location) {
	    Log.v("MainActivity", "location changed");
	    updatePlaces();
	}
	public void onProviderDisabled(String provider){
	    Log.v("MainActivity", "provider disabled");
	}
	public void onProviderEnabled(String provider) {
	    Log.v("MainActivity", "provider enabled");
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
	    Log.v("MainActivity", "status changed");
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		 QuestionActivity.checkIn1 = true;
		 Toast.makeText(this, "check in set to true", Toast.LENGTH_LONG).show(); //TODO: remove		
		if(marker == null) return false;
		Log.v("current and toGO", "current: " + lastLatLng.toString() +
				"toGO: " + marker.getPosition().toString());
		
		removeDirectionsMarkers();
	    marker.showInfoWindow();
		
		
		if(Math.abs(marker.getPosition().latitude - lastLatLng.latitude) < 0.0005
		&& Math.abs(marker.getPosition().longitude - lastLatLng.longitude) < 0.0005){
			return true;
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
		if(Math.abs(marker.getPosition().latitude - lastLatLng.latitude) < 0.0005
		&& Math.abs(marker.getPosition().longitude - lastLatLng.longitude) < 0.0005
		&& !marker.getTitle().equals("You are here")){
			removeDirectionsMarkers();
			if(!marker.isInfoWindowShown()){
				marker.showInfoWindow();
			}
			Toast.makeText(this,"You have checked in at " + marker.getTitle(),
			    	Toast.LENGTH_LONG).show();
		   
		}
		
	}
}


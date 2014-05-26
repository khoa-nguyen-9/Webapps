package library;

import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // User table name
    private static final String TABLE_LOGIN = "login";
    // Location table name
    private static final String TABLE_LOCATION = "location";
    // Question table name
    private static final String TABLE_QUESTION = "question";
    // Hint table name
    private static final String TABLE_HINT = "hint";
    // Request table name
    private static final String TABLE_REQUEST = "request";
    // Available hints per question table name
    private static final String TABLE_AV_HINTS = "available_hint";
    // Answered questions table name
    private static final String TABLE_ANS_QUESTION = "answered_question";
    // Friend list table name
    private static final String TABLE_FRIEND_LIST = "friend_list";
    // Current Location table name
    private static final String TABLE_CURR_LOCATION = "current_location";
    
    // Login Table Column names - ID, name, username, pass
    private static final String KEY_U_ID = "u_id";
    private static final String KEY_U_NAME = "u_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
 
    // Location Table Column names - ID, X, Y, Name
    private static final String KEY_L_ID = "l_id";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_L_NAME = "l_name";
    
    // Hint Table Column names - ID, LocationID, Content 
    private static final String KEY_H_ID = "h_id";
    private static final String KEY_H_CONTENT = "h_content";
    
    // Request Table Column names - q_ID, requester_id, friend_id, reply
    private static final String KEY_REQUESTER_ID = "requester_id";
    private static final String KEY_FRIEND_ID = "friend_id";
    private static final String KEY_REPLY = "reply";
    
    // Question Table Column names - ID, maker, content, ranking, answer 
    private static final String KEY_Q_ID = "q_id";
    private static final String KEY_MAKER = "maker";
    private static final String KEY_Q_CONTENT = "q_content";
    private static final String KEY_RANKING = "ranking";
    private static final String KEY_ANSWER = "answer";
    
    // Available Hints per Question Column names - h_id, q_id
    // Answered questions Column names - q_ID, u_ID, reply
    // Friend list - u1_ID, u2_ID
    // Current Location -l_ID, u_ID
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
       createLoginTable(db);
       createLocationTable(db);
       createRequestTable(db);
       createHintTable(db);
       createQuestionTable(db);
       createAvailableHintTable(db);
       createAnsweredQuestionTable(db);
       createFriendListTable(db);
       createCurrentLocationTable(db);
    }
    
    // Create the login table
    private void createLoginTable(SQLiteDatabase db) {
    	 String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                 + KEY_U_ID + " INT(4) NOT NULL AUTO_INCREMENT,"
                 + KEY_USERNAME + " VARCHAR(20) NOT NULL,"
                 + KEY_U_NAME + " VARCHAR(20) NOT NULL,"
                 + KEY_EMAIL + " VarCHAR(20) NOT NULL,"
                 + "PRIMARY KEY (" + KEY_U_ID +")," 
                 + "UNIQUE KEY (" + KEY_USERNAME + "),"
                 + "UNIQUE KEY (" + KEY_EMAIL + ")" + ")";
         db.execSQL(CREATE_LOGIN_TABLE);
    }
    
    // Create the location table
    private void createLocationTable(SQLiteDatabase db) {
    	String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
    			+ KEY_L_ID + " INT(4) NOT NULL AUTO_INCREMENT,"
    			+ KEY_X + " FLOAT NOT NULL,"
    			+ KEY_Y + " FLOAT NOT NULL,"
    			+ KEY_L_NAME + " VARCHAR(50) NOT NULL," 
    			+ "PRIMARY KEY ("+ KEY_L_ID + "),"
    			+ "UNIQUE KEY (" + KEY_L_NAME + ")" + ")";
    	db.execSQL(CREATE_LOCATION_TABLE);
    }
    
    // Create the request table
    private void createRequestTable(SQLiteDatabase db) {
    	String CREATE_REQUEST_TABLE = "CREATE TABLE " + TABLE_REQUEST + "("
    			+ KEY_Q_ID + " INT(4) NOT NULL, " 
    			+ KEY_REQUESTER_ID + " INT(4) NOT NULL, "
    			+ KEY_FRIEND_ID + " INT(4) NOT NULL,"
    			+ KEY_REPLY + " VARCHAR(50) NOT NULL,"
    			+ "PRIMARY KEY (" + KEY_Q_ID + ", " + KEY_REQUESTER_ID + ", " + KEY_FRIEND_ID + ")" + ")";
    	db.execSQL(CREATE_REQUEST_TABLE);
    }
    
    // Create the hint table 
    private void createHintTable(SQLiteDatabase db) {
    	String CREATE_HINT_TABLE = "CREATE TABLE " + TABLE_HINT + "("
    			+ KEY_H_ID + " INT(4) NOT NULL AUTO_INCREMENT, " 
    			+ KEY_L_ID + " INT(4) NOT NULL,"
    			+ KEY_H_CONTENT + " VARCHAR(50) NOT NULL,"
    			+ "PRIMARY KEY (" + KEY_H_ID + ")" + ")";
    	db.execSQL(CREATE_HINT_TABLE);
    }
    
    // Create the question table 
    private void createQuestionTable(SQLiteDatabase db) {
    	String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + "("
    			+ KEY_Q_ID + " INT(4) NOT NULL AUTO_INCREMENT,"
    			+ KEY_MAKER + " INT(4) NOT NULL,"
    			+ KEY_Q_CONTENT + " VARCHAR(50) NOT NULL,"
    			+ KEY_RANKING + " INT(4) UNSIGNED NOT NULL,"
    			+ KEY_ANSWER + " VARCHAR(50) NOT NULL,"
    			+ "PRIMARY KEY (" + KEY_Q_ID + "),"
    			+ "UNIQUE KEY (" + KEY_Q_CONTENT + ")" + ")";
    	db.execSQL(CREATE_QUESTION_TABLE);
    }
    
    // Create the available hint table
    private void createAvailableHintTable(SQLiteDatabase db) {
    	String CREATE_AVAILABLE_HINT_TABLE = "CREATE TABLE " + TABLE_AV_HINTS + "("
    			+ KEY_H_ID + " INT(4) NOT NULL,"
    			+ KEY_Q_ID + " INT(4) NOT NULL" + ")";
    	db.execSQL(CREATE_AVAILABLE_HINT_TABLE);
    }
    
    // Create the answered question table
    private void createAnsweredQuestionTable(SQLiteDatabase db) {
    	String CREATE_ANSWERED_QUESTION_TABLE = "CREATE TABLE " + TABLE_ANS_QUESTION + "("
    			+ KEY_Q_ID + " INT(4) NOT NULL,"
    			+ KEY_U_ID + " INT(4) NOT NULL,"
    			+ KEY_REPLY + " VARCHAR(50)" + ")";
    	db.execSQL(CREATE_ANSWERED_QUESTION_TABLE);
    }
    
    // Create the friend list table
    private void createFriendListTable(SQLiteDatabase db) {
    	String CREATE_FRIEND_LIST_TABLE = "CREATE TABLE " + TABLE_FRIEND_LIST + "("
    			+ KEY_U_ID + "1" + " INT(4) NOT NULL,"
    			+ KEY_U_ID + "2" + " INT(4) NOT NULL" + ")";
    	db.execSQL(CREATE_FRIEND_LIST_TABLE);
    }
    
    // Create the current location table
    private void createCurrentLocationTable(SQLiteDatabase db) {
    	String CREATE_CURRENT_LOCATION_TABLE = "CREATE TABLE " + TABLE_CURR_LOCATION + "("
    			+ KEY_L_ID + " INT(4) NOT NULL,"
    			+ KEY_U_ID + " INT(4) NOT NULL" + ")";
    	db.execSQL(CREATE_CURRENT_LOCATION_TABLE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AV_HINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANS_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR_LOCATION);
        
        // Create tables again
        onCreate(db);
    }
 
    // Add new user to the login table
    public void addLogin(String name, String email, String username) { 
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USERNAME, name);  // Username
        newValues.put(KEY_U_NAME, email);   // user's name
        newValues.put(KEY_EMAIL, username); // email
        
    	//Insert new values
    	insertNewRow(TABLE_LOGIN, newValues);
    }
    
    // Add new location to the location table
    public void addLocation(String x, String y, String name) {
    	ContentValues newValues = new ContentValues();
    	newValues.put(KEY_X, x);         // X coordinate
    	newValues.put(KEY_Y, y);         // Y coordinate
    	newValues.put(KEY_L_NAME, name); // location's name
    	
    	//Insert new values
    	insertNewRow(TABLE_LOCATION, newValues);
    }
    
    // Add new request to the request table
    public void addRequest(String q_id, String requester_id, String friend_id, String reply) {
    	ContentValues newValues = new ContentValues();
    	newValues.put(KEY_Q_ID, q_id);                 // question's id
    	newValues.put(KEY_REQUESTER_ID, requester_id); // requester's id
    	newValues.put(KEY_FRIEND_ID, friend_id);       // friend's id
    	newValues.put(KEY_REPLY, reply);               // reply
    	
    	//Insert new values
    	insertNewRow(TABLE_REQUEST, newValues);
    }
    
    // Add new hint to the hint table
    public void addHint(String l_id, String h_content) {
    	ContentValues newValues = new ContentValues();
    	newValues.put(KEY_L_ID, l_id);            // hint's question id
    	newValues.put(KEY_H_CONTENT, h_content);  // hint's content
    	
    	//Insert new values
    	insertNewRow(TABLE_HINT, newValues);
    }
    
    // Add new question to the question table
    public void addQuestion(String maker, String q_content, String ranking, String reply) {
    	ContentValues newValues = new ContentValues();
    	newValues.put(KEY_MAKER, maker);          // id of the question's maker
    	newValues.put(KEY_Q_CONTENT, q_content);  // question's content
    	newValues.put(KEY_RANKING, ranking);      // question's rank
    	newValues.put(KEY_REPLY, reply);          // question's answer
    	
    	//Insert new values
    	insertNewRow(TABLE_QUESTION, newValues);
    }
    
    // Insert new row in a given table
    // PRE: Number & format of values are correct.
    private void insertNewRow(String nameTable, ContentValues newValues) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
        // Insert row
        db.insert(nameTable, null, newValues);
        db.close(); // Closing database connection
 
    }

    // Get the username's provided ID
    public Integer getUserID(String username) {
    	// Create the SELECT query
    	String getUserIDQuery = "SELECT " + KEY_U_ID + " FROM " + TABLE_LOGIN + 
    			" WHERE " + KEY_USERNAME + " = '" + username + "'";
    	
    	// Get the row 
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(getUserIDQuery, null);
    	cursor.close();
    	return Integer.parseInt(cursor.getString(0));
    }
 
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    // Return the number of rows in a table.
    public int getRowCount() {
        // Create the count query
    	String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        
    	// Count the rows
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // Return the number of rows
        return rowCount;
    }
    
    public String returnRows() {
    	String response = "";
    	String countQuery = "SELECT * FROM " + TABLE_LOGIN;
    	SQLiteDatabase db = this.getReadableDatabase();
    	for (int j = 0; j < getRowCount(); j++) {
    		Cursor cursor = db.rawQuery(countQuery, null);
    		response += cursor.getColumnName(j);
    	}
    	Log.v("LT", response);
    	return response;
    }
    
    // Empty all tables
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Delete all rows from all tables
        db.delete(TABLE_LOGIN, null, null);
        db.delete(TABLE_LOCATION, null, null);
        db.delete(TABLE_HINT, null, null);
        db.delete(TABLE_QUESTION, null, null);
        db.delete(TABLE_REQUEST, null, null);
        
        db.close();
    }
}

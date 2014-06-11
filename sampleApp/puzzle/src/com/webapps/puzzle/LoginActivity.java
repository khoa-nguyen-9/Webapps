package com.webapps.puzzle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    ImageView imgLogo;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        imgLogo = (ImageView) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
 
        // Login button Click Event
        imgLogo.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
				progressDialog.setMessage("Logging in...");
				LoginTask loginTask = new LoginTask(LoginActivity.this, progressDialog);
				loginTask.execute();
                
                
            }
        });
 
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    
    public void showLoginError(int responseCode) {
    	int duration = Toast.LENGTH_LONG;
    	Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, "Login Error", duration);
		toast.show();
    }
}
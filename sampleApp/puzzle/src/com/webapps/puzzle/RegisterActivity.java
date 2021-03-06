package com.webapps.puzzle;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class RegisterActivity extends Activity {
    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    TextView registerErrorMsg;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
 
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
 
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
				progressDialog.setMessage("Registering...");
				RegisterTask registerTask = new RegisterTask(RegisterActivity.this, progressDialog);
				registerTask.execute();
            }
        });
 
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }

	public void registerReport(Integer responseCode) {
    	int duration = Toast.LENGTH_LONG;
    	Context context = getApplicationContext();
		
		if (responseCode == 0) {
			Toast toast = Toast.makeText(context, "Register Error", duration);
			toast.show();
		}
		else {
			Toast toast = Toast.makeText(context, "Register Success", duration);
			toast.show();
            Intent i = new Intent(getApplicationContext(),
                    DashboardActivity.class);
            startActivity(i);
            finish();
		}
		
		
		
	}
}
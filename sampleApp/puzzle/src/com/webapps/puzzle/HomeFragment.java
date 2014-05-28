package com.webapps.puzzle;

import java.util.HashMap;

import library.DatabaseHandler;
import library.UserFunctions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {
	
	Button btnLogout;
    DatabaseHandler dbHandler;
    private HashMap<String, String> user;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.dashboard, container, false);
		
		btnLogout = (Button) view.findViewById(R.id.btnLogout);
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext());
        user = dbHandler.getUserDetails();
        TextView emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        emailTextView.setText(user.get("email"));
        

        btnLogout.setOnClickListener(this); 
		
        
		return view;
	}

	@Override
	public void onClick(View v) {
		
        // TODO Auto-generated method stub
        new UserFunctions().logoutUser(getActivity());
        Intent login = new Intent(getActivity(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        // Closing dashboard screen
        getActivity().finish();
            
        };
}

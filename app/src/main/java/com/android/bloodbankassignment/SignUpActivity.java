package com.android.bloodbankassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annonation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.bloodbankassignment.utils.Config;
import com.android.bloodbankassignment.utils.HttpHandler;
import com.android.bloodbankassignment.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private TextView text;
    private Button LocationButton;
    private Button signupButton;
    public static double latitude, longitude;
    private EditText firstnameText, lastnameText, usernameText, passwordText, emailText, phoneText, dobText, groupText ;
    private String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstnameText = (EditText) findViewById(R.id.firstName);
        lastnameText = (EditText) findViewById(R.id.lastName);
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        emailText = (EditText) findViewById(R.id.email);
        phoneText = (EditText) findViewById(R.id.phone);
        dobText = (EditText) findViewById(R.id.dob);
        groupText = (EditText) findViewById(R.id.group);

        signupButton = findViewById(R.id.signupkey);
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(SignUpActivity.this, TestActivity.class);
//                startActivity(intent);

                String firstname  = firstnameText.getText().toString();
                String lastname  = lastnameText.getText().toString();
                String username= usernameText.getText().toString();
                String password1 = passwordText.getText().toString();
                String email = emailText.getText().toString();
                String phone = phoneText.getText().toString();
                String dob = dobText.getText().toString();
                String blood = groupText.getText().toString();
                String longitude1 = Double.toString(longitude);
                String latitude1 = Double.toString(latitude);

                new PostAsyncSignup().execute(firstname, lastname, username, password1, email, phone,
                        dob, blood, longitude1, latitude1 );

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        LocationButton = findViewById(R.id.viewmaps);
        LocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        text = findViewById(R.id.signup1);
        text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, email.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    class PostAsyncSignup extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private final String LOGIN_URL = Config.base_url + "signup";

        @Override
        protected void onPreExecute() {
//            Toast.makeText(SignUpActivity.this, "Attempting to signup", Toast.LENGTH_LONG).show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("first_name", args[0]);
                params.put("last_name", args[1]);
                params.put("username", args[2]);
                params.put("password", args[3]);
                params.put("email", args[4]);
                params.put("phone", args[5]);
                params.put("dob", args[6]);
                params.put("blood", args[7]);
                params.put("longitude", args[8]);
                params.put("latitude", args[9]);

                for (int i=0; i<9; i++){
                    System.out.println("Data "+ i +": "+args[i]+"\n");
                }

                Log.d("request", "starting");

                JSONObject jsonstr = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);
                String json = jsonstr.toString();

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return jsonstr;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {

            int success = 0;
            String message = "";

            if (json != null) {

                try {
                    success = json.getInt("success");
                    Log.d("value for Success: " , Integer.toString(success));

                    System.out.println(success);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success! ", "great");
                Intent intent = new Intent(SignUpActivity.this, Dashboard.class);
                startActivity(intent);
            }else{
                Log.d("Failure !!!", "ouch");
            }
        }
    }
}

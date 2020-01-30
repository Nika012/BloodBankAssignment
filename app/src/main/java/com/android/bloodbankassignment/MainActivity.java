package com.android.bloodbankassignment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bloodbankassignment.model.Login;
import com.example.facebook.model.Response;
import com.example.facebook.model.User;
import com.example.facebook.network.Retrofit;
import com.example.facebook.network.RetrofitApi;
import com.example.facebook.utils.Constants;
import com.example.facebook.utils.Validator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    EditText emailtxt,passwordtxt;
    Button loginbtn, regbtn;
    TextView forgot;
    List<Response> loginres = new ArrayList<>();
    public static List<User> userlist = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewsInit();


        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

    }

    private void viewsInit(){

        emailtxt = findViewById(R.id.emailtxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        loginbtn = findViewById(R.id.loginbtn);
        regbtn = findViewById(R.id.createacc);
        forgot = findViewById(R.id.forgot);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();

            }
        });
        regbtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          startActivity(new Intent(MainActivity.this, RegisterActivity.class));

                                      }
                                  }
        );
    }

    private void validate(){
        int err = 0;
        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();

        if(!Validator.validateEmail(email) ){
            err++;
            emailtxt.requestFocus();
            emailtxt.setError("Email Cannot Be Empty!");
            emailtxt.setText(null);
        }

        if(!Validator.validateFields(password)){
            err++;
            passwordtxt.requestFocus();
            passwordtxt.setError("Password Cannoot Be Empty!");
            passwordtxt.setText(null);
        }

        if(err == 0){
            login();
        }
    }

    private void setError(){
        emailtxt.setText(null);
        passwordtxt.setText(null);
        emailtxt.requestFocus();
        emailtxt.setError("Incorrect email or password!");
    }

    private void login(){

        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();
        RetrofitApi api =  Retrofit.retrofitInit().create(RetrofitApi.class);
        Call<List<Response>> loginresponse = api.login(new Login(email, password));

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        loginresponse.enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {


                if(response.isSuccessful()){
                    loginres = response.body();
                    userlist.add(loginres.get(0).getUser());
                    editor.putString(Constants.TOKEN, loginres.get(0).getToken());
                    editor.putString(Constants.EMAIL, userlist.get(0).getEmail());

                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

//                    Toast.makeText(MainActivity.this, sharedPreferences.getString(Constants.TOKEN, null), Toast.LENGTH_SHORT).show();


                }else{
                    setError();
//                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });



    }



}

package com.elearneur.edubox.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elearneur.edubox.R;
import com.elearneur.edubox.calendar.Account;
import com.elearneur.edubox.calendar.CalEvent;
import com.elearneur.edubox.calendar.JSONParser;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;

public class Register extends AppCompatActivity {
    EditText editTextFullname, editTextEmail, editTextUsername, editTextPassword;
    TextView textViewCancel;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullname = (EditText) findViewById(R.id.fullname);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textViewCancel = findViewById(R.id.textViewCancel);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname, email, username, password;
                fullname = String.valueOf(editTextFullname.getText());
                email = String.valueOf(editTextEmail.getText());
                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                if(username.length() < 6){
                    Toast.makeText(Register.this,"Username must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 8){
                    Toast.makeText(Register.this,"Password must be at least8 characters long", Toast.LENGTH_SHORT).show();
                }

                //
//                private void getAccount(){
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                JSONParser.getAccount(username,password);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    thread.start();
//                    try {
//                        thread.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }


//                if(!fullname.equals("") && !email.equals("") && !username.equals("") && !password.equals("")){
//
//                Handler handler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                            //Starting Write and Read data with URL
//                            //Creating array for parameters
//                            String[] field = new String[4];
//                            field[0] = "fullname";
//                            field[1] = "email";
//                            field[2] = "username";
//                            field[3] = "password";
//                            //Creating array for data
//                            String[] data = new String[4];
//                            data[0] = fullname;
//                            data[1] = email;
//                            data[2] = username;
//                            data[3] = password;
//                            PutData putData = new PutData("http://192.168.1.4/edubox/signup.php", "POST", field, data);
//                            if (putData.startPut()) {
//                                if (putData.onComplete()) {
//                                    String result = putData.getResult();
//                                    if(result.equals("Sign Up Successfully")){
//                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), Login.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    else {
//                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                            //End Write and Read data with URL
//                        }
//                    });
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
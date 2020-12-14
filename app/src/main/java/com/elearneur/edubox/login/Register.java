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
import com.elearneur.edubox.calendar.JSONParser;
import com.google.gson.Gson;
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
                Account acc = new Account();

                fullname = String.valueOf(editTextFullname.getText()); // to be added in models
                email = String.valueOf(editTextEmail.getText());
                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                acc.setUsername(username);
                acc.setEmail(email);
                acc.setPassword(password);

                Toast toast1 = Toast.makeText(Register.this, "Success!", Toast.LENGTH_LONG);
                Toast toast2 = Toast.makeText(Register.this, "Unsuccessful!", Toast.LENGTH_SHORT);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String jsonResponse = JSONParser.postAccount(acc);
                            System.out.println("RESPONSE JSON = " + jsonResponse);
                            Gson gson = new Gson();
                            Account acc2 = gson.fromJson(jsonResponse, Account.class); // turn response into account instance/s
                            if (acc2!= null && acc2.getId() != 0){ // check if the response is intended for Account.class (id starts at 1 in the server)
                                Intent intent = new Intent(Register.this, Login.class);
                                toast1.show();
                                startActivity(intent);
                                finish();
                            } else {
                                toast2.show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
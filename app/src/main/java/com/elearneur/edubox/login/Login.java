package com.elearneur.edubox.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.elearneur.edubox.MainActivityMenu;
import com.elearneur.edubox.R;
import com.elearneur.edubox.calendar.Account;
import com.elearneur.edubox.calendar.JSONParser;
import com.elearneur.edubox.calendar.PCalendar;
import com.elearneur.edubox.calendar.PersonalCalendar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;

public class Login extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;

    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try { // goto MainActivityMenu if account is already available
            PCalendar pcal = PCalendar.loadCalendar(Login.this);
            if (pcal != null && pcal.getAccount() != null){
                Intent intent = new Intent(Login.this, MainActivityMenu.class);
                startActivity(intent);
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        editTextUsername = (EditText) findViewById(R.id.username_login);
        editTextPassword = (EditText) findViewById(R.id.password_login);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username, password;

                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                Toast toast1 = Toast.makeText(Login.this, "Wrong credentials!", Toast.LENGTH_SHORT);
                Toast toast2 = Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_LONG);
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        Account acc = null;
                        try {
                            acc = JSONParser.getAccount(username, password);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (acc != null){ // if not working, try !acc.isEmpty()
                            Intent intent = new Intent(getApplicationContext(), MainActivityMenu.class);
                            intent.putExtra("account", acc);
                            System.out.println("Account = " + acc);
                            toast2.setText("Welcome " + acc.getUsername() + "!");
                            toast2.show();
                            startActivity(intent);
                            finish();
                        } else {
                            toast1.show();
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


        TextView tv01 = (TextView) this.findViewById(R.id.textView01);
        TextView tv02 = (TextView) this.findViewById(R.id.textView02);
        TextView tv3 = (TextView) this.findViewById(R.id.textView3);

        tv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivityMenu.class);
                startActivity(intent);
                finish();
            }
        });


        tv02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this,Register.class);
                    startActivity(intent);
          }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        textView = findViewById(R.id.textView02);
        String text = "No account? Sign up";

        SpannableString ss = new SpannableString(text);

        ForegroundColorSpan mRed = new ForegroundColorSpan(Color.parseColor("#2459AA"));

        ss.setSpan(mRed, 11, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
    }
}
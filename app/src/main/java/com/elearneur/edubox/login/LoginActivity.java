package com.elearneur.edubox.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.elearneur.edubox.MainActivityMenu;
import com.elearneur.edubox.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLoginButton();
            }
        });
    }

    private void clickLoginButton(){
        Intent i = new Intent(this, MainActivityMenu.class);
        startActivity(i);
        finish();
    }
}
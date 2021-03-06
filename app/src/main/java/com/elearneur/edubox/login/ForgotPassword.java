package com.elearneur.edubox.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.elearneur.edubox.R;

public class ForgotPassword extends AppCompatActivity {
    TextView textViewCancel;
    public TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        textViewCancel = findViewById(R.id.textView8);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView = findViewById(R.id.textView12);
        String text = "No account? Sign up";

        SpannableString ss = new SpannableString(text);

        ForegroundColorSpan mRed = new ForegroundColorSpan(Color.parseColor("#2459AA"));

        ss.setSpan(mRed, 11, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
    }
}
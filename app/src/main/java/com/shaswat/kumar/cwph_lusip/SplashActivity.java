package com.shaswat.kumar.cwph_lusip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private TextView random_quotes_generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        random_quotes_generator=findViewById(R.id.random_quote_genrator);
        String names[]={"You can Win if you Want","A diamond is a chunk of coal that did well under pressure ","If you never try you will never know","Don't Stress Do Your Best Forget the Rest ","If you quit once it becomes a habit .Don't Quit ","I will not be stopped ","Work hard and stay positive ","Don't lose your present to your past ","Behind the cloud the sun is still shining ","You are stronger than you know","It's not whether you get knocked down ,It's whether you Get Up"};
        random_quotes_generator.setText(names[new Random().nextInt(names.length)]);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);



    }
}

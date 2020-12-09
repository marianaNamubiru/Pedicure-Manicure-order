package com.example.makemeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginRegisterActivity extends AppCompatActivity {
    private String TAG = LoginRegisterActivity.class.getSimpleName();

    //declare the views
    EditText contact;
    ImageView back;
    Button submit;

    //variables to hold the shared data
    String Contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //find reference to views

        contact = findViewById(R.id.contact);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);


        //get shared data
        Intent intent = getIntent();
        Contact = intent.getStringExtra(Contact);

        //set the text to containers
        contact.append("Tel: " + Contact);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Use the Builder class for convenient dialog construction
                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(in);
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Use the Builder class for convenient dialog construction
                Intent in = new Intent(getApplicationContext(),LocationActivity.class);
                startActivity(in);
                finish();

            }
        });



    }
    /*
    public void  exit(View v){
        //to terminate an app
        finishAffinity();
    }*/
}
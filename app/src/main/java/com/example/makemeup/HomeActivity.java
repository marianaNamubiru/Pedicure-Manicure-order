package com.example.makemeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private String TAG = HomeActivity.class.getSimpleName();
    private Button phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        phone = findViewById(R.id.phone);

        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Use the Builder class for convenient dialog construction
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

}
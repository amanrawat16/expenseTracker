package com.example.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(v -> openNewActivity());


        button.setOnClickListener(v -> openNewActivity());

    }
    public void openNewActivity(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
}
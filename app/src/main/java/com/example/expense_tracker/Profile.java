package com.example.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    String[] transType = { "Credit","Debit"};

    ImageView ivImage;
    TextView tvName;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    TextView tvEditBalance;
    EditText etEditBalance;
    TextView balance;
    Button add;
    EditText tAmount;

    String TransactionType,transAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        btLogout = findViewById(R.id.bt_logout);
        etEditBalance = findViewById(R.id.editTextBalance);
        tvEditBalance = findViewById(R.id.tvEditBalance);
        balance = findViewById(R.id.balance);
        add = findViewById(R.id.add);
        tAmount = findViewById(R.id.editTextNumber);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    transAmount = tAmount.getText().toString();


            }
        });







        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                transType);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);








        if(firebaseUser != null){
            Glide.with(Profile.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(ivImage);

            tvName.setText(firebaseUser.getDisplayName());


        }
        googleSignInClient = GoogleSignIn.getClient(Profile.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.signOut();

                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Profile.this,Login.class));
                        }
                    }
                });
            }
        });

        tvEditBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            etEditBalance.setVisibility(View.VISIBLE);
                                             }
        });

        etEditBalance.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        balance.setText(etEditBalance.getText());
                        etEditBalance.setVisibility(View.GONE);
                    }
                    return false;
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TransactionType =  transType[position];



        }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
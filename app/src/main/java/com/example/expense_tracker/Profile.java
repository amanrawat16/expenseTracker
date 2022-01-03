package com.example.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    String[] transType = {"Credit", "Debit"};

    ImageView ivImage;
    TextView tvName;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    TextView tvEditBalance;
    EditText etEditBalance;
    TextView balance;
    TextView balanceUpdate;
    EditText tAmount;
    TextView textViewData;
    TextView income,expense;

    private static final String KEY_BALANCE = "balance";
    private static final String KEY_INCOME = "income";
    private static final String KEY_EXPENSE = "expense";
    String TransactionType;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DocumentReference noteRef;
    public CollectionReference notebookRef;


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
        balanceUpdate = findViewById(R.id.balanceUpdate);
        textViewData = findViewById(R.id.text_view_data);
        income =findViewById(R.id.income);
        tAmount = findViewById(R.id.editTextNumber);
        expense = findViewById(R.id.expense);
        TextView history = findViewById(R.id.transHistory);




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();











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
            balanceUpdate.setVisibility(View.VISIBLE);
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

    public void saveNote(View v){
        String bal = etEditBalance.getText().toString();
        String income = "0";
        String expense = "0";

        Map<String,Object> note = new HashMap<>();
        note.put(KEY_BALANCE,bal);
        note.put(KEY_INCOME,income);
        note.put(KEY_EXPENSE,expense);


        db.collection(firebaseAuth.getUid()).document("Balance").set(note)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profile.this, "Saved", Toast.LENGTH_SHORT).show();
                        etEditBalance.setVisibility(View.GONE);
                        balanceUpdate.setVisibility(View.GONE);
                        etEditBalance.setText("");
                        fetchBalance();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                });

    }
    public void fetchBalance(){
        DocumentReference noteRef = db.collection(firebaseAuth.getUid()).document("Balance");
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String Balance = documentSnapshot.getString(KEY_BALANCE);
                            String Expense = documentSnapshot.getString(KEY_EXPENSE);
                            String Income = documentSnapshot.getString(KEY_INCOME);
                            income.setText(Income);
                            expense.setText(Expense);
                            balance.setText(Balance);
                        } else {
                            Toast.makeText(Profile.this, "Welcome, please update your balance", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
   protected void onStart() {
        super.onStart();

        DocumentReference noteRef = db.collection(firebaseAuth.getUid()).document("Balance");
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String Balance = documentSnapshot.getString(KEY_BALANCE);
                            String Expense = documentSnapshot.getString(KEY_EXPENSE);
                            String Income = documentSnapshot.getString(KEY_INCOME);
                            income.setText(Income);
                            expense.setText(Expense);
                            balance.setText(Balance);
                        } else {
                            Toast.makeText(Profile.this, "Welcome, please update your balance", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        CollectionReference notebookRef = db.collection(firebaseAuth.getUid());
        notebookRef.get();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException error) {
                if (error != null){
                    return;
                }

                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Transaction note = documentSnapshot.toObject(Transaction.class);
                    note.setDocumentId(documentSnapshot.getId());
                    String documentId = note.getDocumentId();
                    String Amount = note.getAmount();
                    String Type = note.getType();

                    data += "Amount : "+Amount+"\t\t\t\t\t\t\t\t\t\t\t\t\t\tTransaction Type : "+Type+"\n"+ "ID : "+documentId+"\n\n";
                    textViewData.setText(data);

                }
            }
        });
    }

    public void addNote(View v){
        CollectionReference notebookRef = db.collection(firebaseAuth.getUid());
        String amount = tAmount.getText().toString();
        String Type = TransactionType;


        DocumentReference noteRef = db.collection(firebaseAuth.getUid()).document("Balance");
        if(Type.equals("Credit")){

            noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String Balance = documentSnapshot.getString(KEY_BALANCE);
                                String newBalance = String.valueOf(Integer.parseInt(Balance)+ Integer.parseInt(amount));

                                String Income = documentSnapshot.getString(KEY_INCOME);
                                String newIncome = String.valueOf(Integer.parseInt(Income)+ Integer.parseInt(amount));

                                String Expense = documentSnapshot.getString(KEY_EXPENSE);




                                Map<String,Object> note = new HashMap<>();
                                note.put(KEY_BALANCE,newBalance);
                                note.put(KEY_INCOME,newIncome);
                                note.put(KEY_EXPENSE,Expense);


                                db.collection(firebaseAuth.getUid()).document("Balance").set(note)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Profile.this, "Money credited", Toast.LENGTH_SHORT).show();
                                                fetchBalance();
                                                tAmount.setText("");
                                            }
                                        });

                            } else {
                                Toast.makeText(Profile.this, "Welcome, please update your balance", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
        else{
            noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String Balance = documentSnapshot.getString(KEY_BALANCE);
                                String newBalance = String.valueOf(Integer.parseInt(Balance)- Integer.parseInt(amount));

                                String Expense = documentSnapshot.getString(KEY_EXPENSE);
                                String newExpense = String.valueOf(Integer.parseInt(Expense)+ Integer.parseInt(amount));

                                String Income = documentSnapshot.getString(KEY_INCOME);


                                Map<String,Object> note = new HashMap<>();
                                note.put(KEY_BALANCE,newBalance);
                                note.put(KEY_EXPENSE,newExpense);
                                note.put(KEY_INCOME,Income);

                                db.collection(firebaseAuth.getUid()).document("Balance").set(note)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Profile.this, "Money debited", Toast.LENGTH_SHORT).show();
                                                fetchBalance();
                                                tAmount.setText("");
                                            }
                                        });

                            } else {
                                Toast.makeText(Profile.this, "Welcome, please update your balance", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

        Transaction note = new Transaction(amount,Type);

        notebookRef.add(note);
    }
    public void loadNotes(View v){
        CollectionReference notebookRef = db.collection(firebaseAuth.getUid());
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data ="";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Transaction note = documentSnapshot.toObject(Transaction.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String Amount = note.getAmount();
                            String Type = note.getType();

                            data += "Amount : "+Amount+"\t\t\t\t\t\t\t\t\t\t\t\t\t\tTransaction Type : "+Type+"\n"+ "ID : "+documentId+"\n\n";
                            textViewData.setText(data);

                        }


                    }
                });
    }



}
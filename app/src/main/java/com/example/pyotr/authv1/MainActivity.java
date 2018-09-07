package com.example.pyotr.authv1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.SQLOutput;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends Activity implements  View.OnClickListener{

    private static final String TAG ="MyActivity" ;
    DatabaseReference databaseManager;
    DatabaseReference databaseEmp;
    DatabaseReference databaseUsers;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private Button buttonRegister;
    private Button buttonStep2;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCompanyName;
    private EditText editTextCompanyEmployees;
    private EditText editTextCompanyField;
    private EditText editTextEmpNume;
    private EditText editTextEmpPrenume;
    private EditText editTextEmpManager;
    private TextView textViewLog;
    private TextView textInfo;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private  RadioButton radioButton2;
    private LinearLayout layout1;
    private ConstraintLayout angajat;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
            }

            System.out.println("Date de la notificare_Main:"+tmp);

        }*/







        databaseManager= FirebaseDatabase.getInstance().getReference("manager");
        databaseEmp= FirebaseDatabase.getInstance().getReference("angajati");
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");


        progressDialog=new ProgressDialog(this);


        //layout1=(LinearLayout) findViewById(R.id.layout1);

        textInfo=(TextView) findViewById(R.id.textViewInfo);
        buttonStep2=(Button)findViewById(R.id.buttonRegister2) ;


        radioGroup=(RadioGroup) findViewById(R.id.radioGroupStare) ;
        radioButton1=(RadioButton) findViewById(R.id.radioButton3) ;
        radioButton2=(RadioButton) findViewById(R.id.radioButton4) ;
        buttonRegister=(Button) findViewById(R.id.buttonRegister);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword) ;
        editTextCompanyName=(EditText) findViewById(R.id.editTextCompanyName);
        editTextCompanyEmployees=(EditText) findViewById(R.id.editTextCompanyEmployees);
        editTextCompanyField=(EditText) findViewById(R.id.editTextCompanyField);
        editTextEmpNume=(EditText) findViewById(R.id.editTextEmpNume);
        editTextEmpPrenume=(EditText) findViewById(R.id.editTextEmpPrenume);
        editTextEmpManager=(EditText) findViewById(R.id.editTextEmpPass);
        angajat=(ConstraintLayout) findViewById(R.id.angajat);

       mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(this);
        buttonStep2.setOnClickListener(this);

        findViewById(R.id.textViewLog).setOnClickListener(this);





/*
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
            }
        };*/

    }

/*
   @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

*/
    public void setVisibleOff()
{

    editTextEmail.setVisibility(View.INVISIBLE);
    editTextPassword.setVisibility(View.INVISIBLE);
    radioGroup.setVisibility(View.INVISIBLE);
    buttonRegister.setVisibility(View.INVISIBLE);


}
public  void setVisibleAngajator()
{
    editTextCompanyName.setVisibility(View.VISIBLE);
    editTextCompanyEmployees.setVisibility(View.VISIBLE);
    editTextCompanyField.setVisibility(View.VISIBLE);
    textInfo.setVisibility(View.VISIBLE);
    buttonStep2.setVisibility(View.VISIBLE);

}

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.textViewLog:
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.buttonRegister:

                //inca 2 layout pentru angajat sau angajator

               verificationUser();

                //registerUser();
                break;
            case R.id.buttonRegister2:

                //addUser();
                registerUser();

                //trimitem nr de angajati


                break;
        }
    }





    private void verificationUser()
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Introduceti email");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Introduceti un email valid");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Introduceti parola");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6)
        {
            editTextPassword.setError("Lungimea parolei trebuie sa fie mai lunga decat 6");
            editTextPassword.requestFocus();
            return;
        }
        if(!(radioButton2.isChecked()||radioButton1.isChecked()))
        {
            Toast.makeText(getApplicationContext(), "Pentru a continua bifati casutele de mai jos.",
                    Toast.LENGTH_SHORT).show();
        }
        if(radioButton1.isChecked())
        {
            //angajator

            setVisibleAngajator();
            setVisibleOff();
            //layout1.setVisibility(View.VISIBLE);

        }
        else if(radioButton2.isChecked())
        {
            setVisibleOff();
            angajat.setVisibility(View.VISIBLE);
            buttonStep2.setVisibility(View.VISIBLE);
            //angajat
        }

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Introduceti email");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Introduceti un email valid");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Introduceti parola");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6)
        {
            editTextPassword.setError("Lungimea parolei trebuie sa fie mai lunga decat 6");
            editTextPassword.requestFocus();
            return;
        }
        progressDialog.setMessage("Registering User..");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Registration Succes.",
                                    Toast.LENGTH_SHORT).show();
                            addUser(mAuth.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });


       /* if(!Patterns.EMAIL_ADDRESS.matcher(password).matches())
        {
            editTextEmail.setError("Introduceti o parola valida");
            editTextEmail.requestFocus();
            return;
        }*/
        //putem pune email sau parola gresita
    }
    /* radio button

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_ninjas:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }*/



    public void addUser(String theid)
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String companyName=editTextCompanyName.getText().toString();
        String nume=editTextEmpNume.getText().toString();
        String prenume=editTextEmpPrenume.getText().toString();
        String manager=editTextEmpManager.getText().toString();
      //  System.out.println("USER ID:"+userid);;

        String companyField=editTextCompanyField.getText().toString();
        int numberOfEmployees = 0;

        try {
            numberOfEmployees = Integer.parseInt(editTextCompanyEmployees.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }



        if(radioButton1.isChecked())
        {
            User user=new User(theid,email,password,"manager");
            databaseUsers.child(theid).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugareInBaza de date:success");


                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugareInBaza de date:Failed");
                        }
                    });
            //angajator
            Managar managar=new Managar(theid,email,password,companyName,numberOfEmployees,companyField,false,false,""); //avem status false deorece inca nu a adagaut angajati in baza de date
            databaseManager.child(theid).setValue(managar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Log.d(TAG, "adaugareInBaza de date:success");


                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Log.d(TAG, "adaugareInBaza de date:Failed");
                    }
                });

            Toast.makeText(this,"Manager adaugat", Toast.LENGTH_LONG).show();


            //la crearea unui cont adaugam automat my shift in tabel

            FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();
            String uid = usr.getUid();
            DatabaseReference myshift= FirebaseDatabase
                    .getInstance()
                    .getReference("Employees")
                    .child(uid);
            DatabaseReference pushRef = myshift.push();//fiecare admin are tabelul lui si are copii fiecare angajat adaugat
            String id = pushRef.getKey();
            Client userBaza=new Client(id,"My Shift"," ","Manager",R.color.colorOverlay);


            pushRef.setValue(userBaza)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugare My Shift in baza de date:success");

                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugare My Shift in baza de date:Failed");
                        }
                    });




            DatabaseReference openShift= FirebaseDatabase
                    .getInstance()
                    .getReference("Employees")
                    .child(uid)
                    .child("open_shift_id");
            //DatabaseReference pushRefOpen = openShift.push();//fiecare admin are tabelul lui si are copii fiecare angajat adaugat

            Client userOpen=new Client("open_shift_id","Open","Shift","Manager",R.color.colorOverlay);


            openShift.setValue(userOpen)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugare Open Shift in baza de date:success");

                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugare Open Shift in baza de date:Failed");
                        }
                    });



            //layout1.setVisibility(View.VISIBLE);

        }
        else if(radioButton2.isChecked())
        {
            User user=new User(theid,email,password,"angajat");
            databaseUsers.child(theid).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugareInBaza de date:success");


                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugareInBaza de date:Failed");
                        }
                    });


            Employee emp=new Employee(theid,email,password,nume,prenume,manager);
            databaseEmp.child(theid).setValue(emp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugareInBaza de date:success");


                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugareInBaza de date:Failed");
                        }
                    });
            Toast.makeText(this,"User adaugat", Toast.LENGTH_LONG).show();
            //angajat
        }

        /*
        if(!TextUtils.isEmpty(email))
        {

            //String id= databaseUsers.push().getKey();
            User user=new User(theid,email,password,companyName,numberOfEmployees,companyField);
            databaseUsers.child(theid).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Write was successful!
                    Log.d(TAG, "adaugareInBaza de date:success");


                    // ...
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Log.d(TAG, "adaugareInBaza de date:Failed");
                        }
                    });
            Toast.makeText(this,"User adaugat", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this,"Introduceti un nume",Toast.LENGTH_LONG).show();
        }*/
        startActivity(new Intent(getApplicationContext(),Login.class));
    }



}

package com.example.pyotr.authv1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity implements  View.OnClickListener{

    private static final String TAG ="MyActivity" ;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private Button buttonRegister;
    private Button buttonStep2;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCompanyName;
    private EditText editTextCompanyEmployees;
    private EditText editTextCompanyField;

    private TextView textViewLog;
    private TextView textInfo;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private  RadioButton radioButton2;
    private ConstraintLayout layout1;


    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(this);


        layout1=(ConstraintLayout) findViewById(R.id.layout1);

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

               if(radioButton1.isChecked())
               {
                   //angajator
                  // registerUser();
                   setVisibleAngajator();
                   setVisibleOff();
                  //layout1.setVisibility(View.VISIBLE);

               }
               else if(radioButton2.isChecked())
               {
                   //angajat
               }
                //registerUser();
                break;
            case R.id.buttonRegister2:
                //inregistrare angajator email.password,company name,nr de angaati,tipul companiei
                break;
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

                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
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



}

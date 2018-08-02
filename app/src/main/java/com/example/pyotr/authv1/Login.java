package com.example.pyotr.authv1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Activity implements View.OnClickListener{


    private static final String TAG ="MyActivity" ;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressDialog=new ProgressDialog(this);

        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword) ;
        mAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(this);

        /*if(mAuth.getCurrentUser()!=null) {

            finish();
            //setContentView(R.layout.succes);//daca a reusit login ===>succes
            startActivity(new Intent(getApplicationContext(), Succes.class));
        }
*/
    }

/*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }
*/
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.buttonLogin:
                loginUser();
                break;
        }
    }

    private void loginUser() {
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
        progressDialog.setMessage("Login User..");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "loginUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Login Succes.",
                                    Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                            finish();
                            //setContentView(R.layout.succes);//daca a reusit login ===>succes
                            //startActivity(new Intent(getApplicationContext(),Succes.class));
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //startActivity(new Intent(getApplicationContext(),Succes.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWIthEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed.",
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




}
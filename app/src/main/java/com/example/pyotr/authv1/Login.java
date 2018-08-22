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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

public class Login extends Activity implements View.OnClickListener{


    private static final String AUTH_KEY = "key=AAAAMYJyzak:APA91bEr-ZQX0KVYJ1YbuOvvqHYVLpmhcF_FxHy-9akg46kNb3aIvR-lo4HXJiyTa0OucBZQfKWFIkgJktSgS8_xnaAi8QgIwsOuWwmtNptiNDr1mHqyt6TWmBRf6xCbcw4xa0cqJGuzLm-i_RLDA_bTcyckAJNwTQ";


    private static final String TAG ="MyActivity" ;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    DatabaseReference adminRef;
    DatabaseReference managerRef;
    DatabaseReference empRef;
   // private String rol="";


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressDialog=new ProgressDialog(this);
      adminRef = FirebaseDatabase.getInstance().getReference("users");
        managerRef = FirebaseDatabase.getInstance().getReference("manager");
        empRef = FirebaseDatabase.getInstance().getReference("angajati");

        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword) ;
        mAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(this);
      /*  Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
        try
        {
            numberOfEmp=Integer.parseInt(value);

        }
        catch (NumberFormatException e)
        {
            System.out.println(e);
        }
*/
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
                            //startActivity(new Intent(getApplicationContext(),Succes.class))

                            final String[] rol = {""};
                            adminRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println("Usr:"+dataSnapshot);
                                    rol[0] = dataSnapshot.getValue(User.class).getRol();
                                    System.out.println("User rol:"+ rol[0]);

                                    if(rol[0].equals("manager"))
                                    {

                                        managerRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                //trebuie sa verificam status
                                                ///daca status is false atunci trebuie sa facem redirect la Intentul de adaugare emp
                                                //daca status is true atunci mergem la TabelClienti

                                                Boolean status=dataSnapshot.getValue(Managar.class).getStatus();
                                                if(status)
                                                {
                                                    //daca shedule is true//atunci
                                                    startActivity(new Intent(getApplicationContext(),TabelClienti.class));

                                                }
                                                else {


                                                    System.out.println("Emp:" + dataSnapshot);
                                                    int numberOfEmp = dataSnapshot.getValue(Managar.class).getNumberOfEmployes();
                                                    System.out.println("Emp number:" + numberOfEmp);
                                                    Intent intent = new Intent(getBaseContext(), AddEmployees.class);
                                                    intent.putExtra("key", numberOfEmp);
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Getting Post failed, log a message
                                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                                // ...
                                            }

                                        });


                                    }
                                    else if(rol[0].equals("angajat"))
                                    {
                                        startActivity(new Intent(getApplicationContext(),Login_Emp.class));
                                        //luam manager id din angati tabel si apoi mergem la Employees si aflam angajatul current dupa nume
                                      /*  empRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                System.out.println("Ang:"+dataSnapshot);
                                               String  manager = dataSnapshot.getValue(Employee.class).getManager();
                                                System.out.println("Ang manager:"+manager);
                                                Intent intent = new Intent(getBaseContext(), Login_Emp.class);
                                                intent.putExtra("key",manager);
                                                startActivity(intent);

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Getting Post failed, log a message
                                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                                // ...
                                            }


                                        });*/

                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Post failed, log a message
                                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                    // ...
                                }

                            });

                            System.out.println("Rol in fct:"+ rol[0]);


                            // startActivity(new Intent(getApplicationContext(),Login_Emp.class));
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
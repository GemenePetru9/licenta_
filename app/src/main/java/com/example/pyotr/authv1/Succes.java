package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Succes extends  Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private TextView textViewUser;
    private Button btnLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.succes);

        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,Login.class));
        }

        textViewUser=(TextView) findViewById(R.id.textViewUser);
        btnLogOut=(Button) findViewById(R.id.buttonLogOut);

        FirebaseUser user=mAuth.getCurrentUser();
        textViewUser.setText("Welcome "+user.getEmail());
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnLogOut)
        {
            mAuth.signOut();

            finish();
            startActivity(new Intent(this,Login.class));
        }

    }
}
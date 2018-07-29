package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Succes extends  Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG ="SuccesActicity" ;
    private Button btnLogOut;
    DatabaseReference databaseClienti;
    private ListView mlistView;
private Client user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.succes);
        databaseClienti= FirebaseDatabase.getInstance().getReference();

        btnLogOut=(Button) findViewById(R.id.buttonLogOut);

        mlistView=(ListView) findViewById(R.id.listViewClienti);

        btnLogOut.setOnClickListener(this);
        databaseClienti.child("client").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Client> clienti_afisati=new ArrayList<>();

                List clienti = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {


                   try{
                       Client clienInfo=ds.getValue(Client.class);
                       clienti.add(clienInfo);
                       System.out.println("Client Info:"+clienInfo.toString());

                       }
                       catch (Exception e)
                       {
                           System.out.println("NU MERGEEEEEEE"+e);
                       }
                }

                ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,clienti);
                mlistView.setAdapter(adapter);

                    System.out.println("BAZAAAA DE DATEEEEEEE");
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
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
package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddEmployees extends Activity {

    private static final String TAG ="AddEmployees" ;
    GridView gridview;
    Button button;
    Button btnFinish;
    Spinner spinner;
    // List<String> ITEM_LIST;
    List<Client> listaClienti;
    ArrayAdapter<Client> arrayadapter;
    EditText edittext1;
    EditText edittext2;
    TextView textViewNumarAngajati;
    String GetItem;
    private int numberOfEmp=0;
    private String value="";
    private int dif=0;

    DatabaseReference databaseClienti;


    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addclient);
        databaseClienti= FirebaseDatabase.getInstance().getReference("client");

        //get number of employyesss
        Bundle extras = getIntent().getExtras();
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


        gridview = (GridView)findViewById(R.id.gridView1);

        button = (Button)findViewById(R.id.button1);
        btnFinish=(Button) findViewById(R.id.button2);
        textViewNumarAngajati=(TextView)findViewById(R.id.textViewNumarEmp) ;
        textViewNumarAngajati.setText("Introduceti "+numberOfEmp+" angajati pentru a contiua");

        edittext1 = (EditText)findViewById(R.id.editTextFirstNameEmp1);
        edittext2 = (EditText)findViewById(R.id.editTextLastNameEmp1);
        spinner=(Spinner) findViewById(R.id.type_spinner);

        // ITEM_LIST = new ArrayList<String>();
        listaClienti=new ArrayList<Client>();

        arrayadapter = new ArrayAdapter<Client>(getApplicationContext(),android.R.layout.simple_list_item_1,listaClienti );


        gridview.setAdapter(arrayadapter);

        button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(counter<numberOfEmp)
                {
                counter++;
                dif=numberOfEmp-counter;
                //GetItem = counter+"."+edittext1.getText().toString()+" "+edittext2.getText().toString();
                Client user=new Client(edittext1.getText().toString(),edittext2.getText().toString(),spinner.getSelectedItem().toString());

                // ITEM_LIST.add(ITEM_LIST.size(),GetItem);
                listaClienti.add(listaClienti.size(),user);
                edittext2.setText("");
                edittext1.setText("");
                if(dif!=0) {
                    textViewNumarAngajati.setText("Introduceti " + dif + " angajati pentru a contiua");
                }
                else {
                    textViewNumarAngajati.setText("Puteti continua");
                }


                arrayadapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Item Added SuccessFully", Toast.LENGTH_LONG).show();


            }}
        });
        btnFinish.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                 dif=numberOfEmp-counter;
                if(counter!=numberOfEmp)
                {

                    if(dif==1)
                    { Toast.makeText(getApplicationContext(), "Introduceti inca un angajat pentru a continua", Toast.LENGTH_LONG).show();

                    }
                    else if(dif>1) {
                        Toast.makeText(getApplicationContext(), "Introduceti " + dif + " angajat pentru a continua", Toast.LENGTH_LONG).show();
                    }


                }
                else
                {
                    System.out.println("Numar de clienti:"+counter);
                    //trimitem data catre server;
                    //adaugam clienti in baza de date
                    //treubie facuta verificarea datelor
                    /*Iterator<Client> i = listaClienti.iterator();
                    while (i.hasNext()) {
                        System.out.println( i);
                        String nume=i.next().getNume();
                        String prenume=i.next().getPrenume();
                        String pozitie=i.next().getPozitie();
                        addUser(nume,prenume,pozitie);

                    }*/
                   //startActivity(new Intent(getApplicationContext(),Succes.class));

                    startActivity(new Intent(getApplicationContext(),TabelClienti.class));
                   // addUser();
                   /* for (Client car : listaClienti) {
                       System.out.print("For loop:"+car.toString());
                        String nume=car.getNume();
                        String prenume=car.getPrenume();
                        String pozitie=car.getPozitie();
                        addUser(nume,prenume,pozitie);
                    }*/

                }


            }
        });
    }

    public void addUser()
    {
        for (Client client : listaClienti) {
            String id = databaseClienti.push().getKey();
            Client user = new Client(id, client.getNume(), client.getPrenume(), client.getPozitie());
            databaseClienti.child(id).setValue(user)
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
        }

            Toast.makeText(this,"Clienti adaugati", Toast.LENGTH_LONG).show();















    }

}

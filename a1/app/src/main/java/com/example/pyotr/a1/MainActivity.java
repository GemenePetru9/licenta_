package com.example.pyotr.a1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridview;
    Button button;
    Button btnFinish;
    Spinner spinner;
   // List<String> ITEM_LIST;
    List<Client> listaClienti;
    ArrayAdapter<Client> arrayadapter;
    EditText edittext1;
    EditText edittext2;
    String GetItem;

    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView)findViewById(R.id.gridView1);

        button = (Button)findViewById(R.id.button1);
        btnFinish=(Button) findViewById(R.id.button2);

        edittext1 = (EditText)findViewById(R.id.editTextFirstNameEmp1);
        edittext2 = (EditText)findViewById(R.id.editTextLastNameEmp1);
        spinner=(Spinner) findViewById(R.id.type_spinner);

       // ITEM_LIST = new ArrayList<String>();
        listaClienti=new ArrayList<Client>();

      arrayadapter = new ArrayAdapter<Client>(MainActivity.this,android.R.layout.simple_list_item_1,listaClienti );


       gridview.setAdapter(arrayadapter);

        button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                counter++;
                //GetItem = counter+"."+edittext1.getText().toString()+" "+edittext2.getText().toString();
                Client user=new Client(edittext1.getText().toString(),edittext2.getText().toString(),spinner.getSelectedItem().toString());

               // ITEM_LIST.add(ITEM_LIST.size(),GetItem);
                listaClienti.add(listaClienti.size(),user);
                edittext2.setText("");
                edittext1.setText("");


                arrayadapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Item Added SuccessFully", Toast.LENGTH_LONG).show();


            }
        });
        btnFinish.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               if(counter==0)
               {
                   Toast.makeText(MainActivity.this, "Introduceti cel putin un client pentru a continua", Toast.LENGTH_LONG).show();
               }
               else
               {
                   //trimitem data catre server;
                   //adaugam clienti in baza de date
                   //treubie facuta verificarea datelor
                   Iterator<Client> i = listaClienti.iterator();
                   while (i.hasNext()) {
                       System.out.println(i.next());
                       //i.next().getNume();
                       //i.next().getPrenume();
                     // i.next().getPozitie();
                   }

               }


            }
        });
    }
}

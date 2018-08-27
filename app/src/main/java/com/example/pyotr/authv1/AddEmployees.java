package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reginald.editspinner.EditSpinner;
import com.skydoves.multicolorpicker.ColorEnvelope;
import com.skydoves.multicolorpicker.MultiColorPickerView;
import com.skydoves.multicolorpicker.listeners.ColorListener;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddEmployees extends Activity {

    private static final String TAG ="AddEmployees" ;
    GridView gridview;
    Button button;
    Button btnFinish;
    Button btnUser;
    Spinner spinner;
    // List<String> ITEM_LIST;
    List<Client> listaClienti;
    ArrayAdapter<Client> arrayadapter;
    EditText edittext1;
    EditText edittext2;
    TextView textViewNumarAngajati;
    String GetItem;
    private int numberOfEmp=0;
    private String userid="";
    private String value="";
    private int dif=0;
    private  int culoare;
    private EditSpinner mEditSpinner;
    private Button btnok;
    private  Button btncancel;
    private LinearLayout paletaCulori;
    MultiColorPickerView multiColorPickerView;
    DatabaseReference databaseClienti;
    DatabaseReference managerRef;


    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addclient);
        databaseClienti= FirebaseDatabase.getInstance().getReference("client");

        managerRef= FirebaseDatabase.getInstance().getReference("manager");


        mEditSpinner = (EditSpinner) findViewById(R.id.edit_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.pozitii));
        mEditSpinner.setAdapter(adapter);

        //get number of employyesss
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numberOfEmp= extras.getInt("key");

            /*String[] result=date.split(" ");
            userid=result[0];
            value=result[1];*/

            //The key argument here must match that used in the other activity
        }
       /* try
        {
            numberOfEmp=Integer.parseInt(value);

        }
        catch (NumberFormatException e)
        {
            System.out.println(e);
        }*/


        gridview = (GridView)findViewById(R.id.gridView1);
        button = (Button)findViewById(R.id.button1);
        btnFinish=(Button) findViewById(R.id.button2);
        textViewNumarAngajati=(TextView)findViewById(R.id.textViewNumarEmp) ;
        textViewNumarAngajati.setText("Introduceti "+numberOfEmp+" angajati pentru a contiua");

        edittext1 = (EditText)findViewById(R.id.editTextFirstNameEmp1);
        edittext2 = (EditText)findViewById(R.id.editTextLastNameEmp1);
       // spinner=(Spinner) findViewById(R.id.type_spinner);

        // ITEM_LIST = new ArrayList<String>();
        listaClienti=new ArrayList<Client>();

        arrayadapter = new ArrayAdapter<Client>(getApplicationContext(),android.R.layout.simple_list_item_1,listaClienti );

        gridview.setAdapter(arrayadapter);



        paletaCulori = (LinearLayout) findViewById(R.id.paletaCulori);
        btnok = (Button) findViewById(R.id.btnOk);
        btncancel = (Button) findViewById(R.id.btnCancel);
        multiColorPickerView = findViewById(R.id.multiColorPickerView);
        // toolbarLate=(Toolbar) findViewById(R.id.toolbarLate) ;



        Drawable myDrawable = getResources().getDrawable(R.drawable.selector);

        multiColorPickerView.addSelector(myDrawable, new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope) {
                //int color = envelope.getColor();
                culoare = envelope.getColor();
                // int[] rgb = envelope.getRgb();
                String htmlCode = envelope.getHtmlCode();

                //imageView.setBackgroundColor(color);
                // TODO

            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(culoare==0) {
                    Toast.makeText(getApplicationContext(), "Selectati o culoare", Toast.LENGTH_SHORT).show();
                }
                else {
                    // imageView.setBackgroundColor(culoare);
                    paletaCulori.setVisibility(View.INVISIBLE);
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paletaCulori.setVisibility(View.INVISIBLE);
            }
        });


        TextView colorPallete=findViewById(R.id.textViewColor);
        culoare=0;

        colorPallete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Color Picker", Toast.LENGTH_SHORT).show();
                // multiColorPickerView.setVisibility(View.VISIBLE);
                paletaCulori.setVisibility(View.VISIBLE);
                paletaCulori.bringToFront();
                culoare=0;
            }
        });

        button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(counter<numberOfEmp)
                {
                    counter++;
                    dif=numberOfEmp-counter;
                    //GetItem = counter+"."+edittext1.getText().toString()+" "+edittext2.getText().toString();

                    FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();
                    String uid = usr.getUid();
                    DatabaseReference adminRef = FirebaseDatabase
                            .getInstance()
                            .getReference("Employees")
                            .child(uid);
                    DatabaseReference pushRef = adminRef.push();//fiecare admin are tabelul lui si are copii fiecare angajat adaugat
                    String id = pushRef.getKey();
                 //   Client user=new Client(id,edittext1.getText().toString(),edittext2.getText().toString(),spinner.getSelectedItem().toString());
                    Client user;
                    if(culoare==0)
                    {
                        //punem default gri
                        user=new Client(id,edittext1.getText().toString(),edittext2.getText().toString(),mEditSpinner.getText().toString(),R.color.colorOverlay);

                    }
                    else {
                       user = new Client(id, edittext1.getText().toString(), edittext2.getText().toString(), mEditSpinner.getText().toString(), culoare);
                    }
                    pushRef.setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Log.d(TAG, "adaugare emp in baza de date:success");

                            // ...
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...
                                    Log.d(TAG, "adaugare emp in baza de date:Failed");
                                }
                            });
                    // String pushId = pushRef.getKey();
                    // mRestaurant.setPushId(pushId);
                    // pushRef.setValue(mRestaurant);

                    // ITEM_LIST.add(ITEM_LIST.size(),GetItem);
                    listaClienti.add(listaClienti.size(),user);
                    edittext2.setText("");
                    edittext1.setText("");
                    if(dif!=0) {
                        if(dif==1)
                        {
                            textViewNumarAngajati.setText("Introduceti " + dif + " angajat pentru a contiua");
                        }
                        else {
                            textViewNumarAngajati.setText("Introduceti " + dif + " angajati pentru a contiua");
                        }

                    }
                    else {
                        textViewNumarAngajati.setText(R.string.numar_angajati_complet);
                    }


                    arrayadapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "Item Added SuccessFully", Toast.LENGTH_LONG).show();


                    culoare=0;
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
                    //addEmp();
                    //trimitem data catre server;
                    //adaugam clienti in baza de date
                    //treubie facuta verificarea datelor
                    //startActivity(new Intent(getApplicationContext(),TabelClienti.class));
                    //setam status to true
                    FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("status");
                    databaseReference.setValue(true);
                    System.out.println("Set status true");


                    Intent intent = new Intent(AddEmployees.this, TabelClienti.class);

                    Integer index=5;
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", index);

                    intent.putExtras(bundle);

                    startActivity(intent);




                }


            }
        });
    }
}

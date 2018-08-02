package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabelClienti  extends Activity implements AdapterView.OnItemClickListener  {

    private TextView textViewData;
    private TextView textViewOra;
    private Spinner spinner1;
    private Spinner spinner2;
    private String ora1;
    private String ora2;
    private Button btnAddShift;
    ConstraintLayout constraintLayout;








    private static final String TAG = "TabelClientiActivity";
    DatabaseReference databaseClienti;
    private TextView textViewDay;


    private Client user;
    ArrayList<Client> clienti_afisati = new ArrayList<>();
    ArrayList<Client> mEmpDataSet = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift);
        databaseClienti = FirebaseDatabase.getInstance().getReference();
        textViewDay=findViewById(R.id.textViewDay);
        constraintLayout=(ConstraintLayout) findViewById(R.id.popuplayout);
        btnAddShift=(Button)findViewById(R.id.btnAddShift);

     //calendar
        //from ore class

        textViewData=(TextView)findViewById(R.id.textViewData) ;
        textViewOra=(TextView)findViewById(R.id.textViewDifOre) ;
        spinner1=(Spinner) findViewById(R.id.spinner) ;
        spinner2=(Spinner) findViewById(R.id.spinner2) ;


      //  DisplayMetrics dm=new DisplayMetrics();

      //  getWindowManager().getDefaultDisplay().getMetrics(dm);

       // int width=dm.widthPixels;
       // int height=dm.heightPixels;
////
       // ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams((int)(width*.6),(int)(height*.4));

       // constraintLayout.setLayoutParams(layoutParams);//setam dimensiunea

        Calendar now = Calendar.getInstance();

        System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
                + "-"
                + now.get(Calendar.DATE)
                + "-"
                + now.get(Calendar.YEAR));

        //create an array of days
        String[] strDays = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thusday",
                "Friday",
                "Saturday"
        };
        String[] strMonths = new String[]{
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };
        //zi+luna +day of week+year
        //Day_OF_WEEK starts from 1 while array index starts from 0
        System.out.println("Current day is : " +
                strDays[now.get(Calendar.DAY_OF_WEEK) - 1]
        );
        textViewData.setText( strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ora1=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ora2=adapterView.getItemAtPosition(i).toString();
                try
                {
                    setOra();
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //zi+luna +day of week+year
        //Day_OF_WEEK starts from 1 while array index starts from 0
        System.out.println("Current day is : " +
                strDays[now.get(Calendar.DAY_OF_WEEK) - 1]
        );
        textViewDay.setText( strDays[now.get(Calendar.DAY_OF_WEEK) - 1]+", "+strMonths[now.get(Calendar.MONTH)]+", "+now.get(Calendar.DATE)+", "+now.get(Calendar.YEAR));
















        String[] ore = {"8am", "9am", "10am", "11am", "12am", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm"};
// Get the GridView layout
        //gridViewOre = (GridView) findViewById(R.id.gridViewOre);
        //ArrayAdapter mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, ore);
        //gridViewOre.setAdapter(mAdapter);



        //Prepare DataSet
         mEmpDataSet= prepareDataSet();

        //Initialize Grid View for programming
        GridView gridview = (GridView) findViewById(R.id.gridViewClienti);
        GridView gridShift= (GridView) findViewById(R.id.gridViewShift);

        //Connect DataSet to Adapter
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(this, mEmpDataSet);
       ShiftAdapter shiftAdapter=new ShiftAdapter(this,mEmpDataSet);

        //Now Connect Adapter To GridView
        gridview.setAdapter(employeeAdapter);
        gridShift.setAdapter(shiftAdapter);

        //Add Listener For Grid View Item Click
        //gridview.setOnItemClickListener(this);
       gridShift.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, final View viewShift, int position, long l) {
      // String ora=" ";
        //Show Name Of The Flower
       // Toast.makeText(getApplicationContext(), mEmpDataSet.get(position).getNume(),
                //Toast.LENGTH_SHORT).show();

        //startActivity(new Intent(getApplicationContext(),Ore.class));

        constraintLayout.setVisibility(View.VISIBLE);
        try {
          setOra();
            btnAddShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout elment= (RelativeLayout) viewShift;
                    int count=elment.getChildCount();
                    System.out.println("Nr de copii "+count);
                    View v;
                    for(int i = 0; i < count; i++) {
                        v = elment.getChildAt(i);
                        if (v instanceof TextView || v instanceof Button /*etc.*/) {

                            ((TextView) v).setText(ora1+" "+ora2);
                            ((TextView) v).setBackgroundColor(Color.GREEN);
                        }
                    }
                    constraintLayout.setVisibility(View.INVISIBLE);
                }
            });



        } catch (ParseException e) {
            e.printStackTrace();
        }




       // TextView selectedTextView= (TextView)
        //selectedTextView.setTextColor(getResources().getColor(R.color.colorAccent));
       // selectedTextView.setBackgroundColor(Color.parseColor("#FF9AD082"));
        //selectedTextView.setText(ora);


    }


    private ArrayList<Client> prepareDataSet() {

            databaseClienti.child("client").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    List clienti = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {


                        try {
                            Client clienInfo = ds.getValue(Client.class);
                            clienti.add(clienInfo);
                            clienti_afisati.add(clienInfo);
                            System.out.println("Client Info:" + clienInfo.toString());

                        } catch (Exception e) {
                            System.out.println("NU MERGEEEEEEE" + e);
                        }
                    }



                    //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, clienti);
                   // mlistView.setAdapter(adapter);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }


            });
        return clienti_afisati;
        }



    public void setOra() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(ora1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(ora2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        if(difference<0)
        {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -date1.getTime() )+(date2.getTime()-dateMin.getTime());
        }


        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        Log.i("======= Hours"," :: "+hours);
        textViewOra.setText(hours+" hours shift");

      /* Button btnAddShift=(Button) findViewById(R.id.btnAddShift);

        btnAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setam intervalul pe itemul selectat
                constraintLayout.setVisibility(View.INVISIBLE);


            }
        });*/



    }


    }





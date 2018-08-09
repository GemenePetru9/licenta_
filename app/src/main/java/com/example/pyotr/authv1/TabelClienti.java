package com.example.pyotr.authv1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelClienti  extends Activity implements AdapterView.OnItemClickListener {

    private TextView textViewData;
    private TextView textViewOra;
    private Spinner spinner1;
    private Spinner spinner2;
    private String ora1;
    private String ora2;
    private Button btnAddShift;
    private Button btnPublish;
    private Button btnWeek;
    ConstraintLayout constraintLayout;
    GridLayout gridLayout;
    private Button btnDay;
    GridLayout gridLayout2;
   private  GridView gridDay;


    private static final String TAG = "TabelClientiActivity";
    DatabaseReference databaseClienti;
    FirebaseUser usr;
    private TextView textViewDay;
    private Calendar now;
    private ImageView nextDay;
    private ImageView previousDay;
    int count = 0;
    int countDays=0;
    // private static Map<Integer, String[]> saptamanal;

    private Client user;

    // private String[] day={"off","off","off","off","off","off","off"};


    ArrayList<String[]> saptamana = new ArrayList<String[]>();

    ArrayList<Client> clienti_afisati = new ArrayList<>();
    ArrayList<Client> mEmpDataSet = new ArrayList<>();


    List<HashMap<String, String[]>> aList;
    String[] from = {"Mon"};
    // String[] from = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    int[] to = {R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data4, R.id.data5, R.id.data6, R.id.data7};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift);
        usr=FirebaseAuth.getInstance().getCurrentUser();
        // saptamanal=new HashMap<Integer, String[]>();


        // Each row in the list stores country name, currency and flag
        //aList = new ArrayList<HashMap<String,String[]>>();

        // HashMap<String, String[]> hm = new HashMap<String, String[]>();
        //hm.put("Mon", day);
        // hm.put("Thu",day);
        //  hm.put("Tue", day );
        // hm.put("Wed", day);
        //  hm.put("Thu", day );
        // hm.put("Fri", day);
        // hm.put("Sat", day );
        //hm.put("Sun", day );
        // aList.add(hm);


        databaseClienti = FirebaseDatabase.getInstance().getReference();
        textViewDay = findViewById(R.id.textViewSapt);
        constraintLayout = (ConstraintLayout) findViewById(R.id.popuplayout);
        gridLayout = (GridLayout) findViewById(R.id.GridLayout1);
        gridLayout2 = (GridLayout) findViewById(R.id.GridLayout2);
        btnAddShift = (Button) findViewById(R.id.btnAddShift);
        nextDay = (ImageView) findViewById(R.id.nextDay);
        previousDay = (ImageView) findViewById(R.id.previousDay);


        //calendar
        //from ore class

        textViewData = (TextView) findViewById(R.id.textViewData);
        textViewOra = (TextView) findViewById(R.id.textViewDifOre);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnPublish = (Button) findViewById(R.id.btnPublish);
        btnWeek = (Button) findViewById(R.id.btnWeek);
        btnDay = (Button) findViewById(R.id.btnDay);


        //  DisplayMetrics dm=new DisplayMetrics();

        //  getWindowManager().getDefaultDisplay().getMetrics(dm);

        // int width=dm.widthPixels;
        // int height=dm.heightPixels;
////
        // ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams((int)(width*.6),(int)(height*.4));

        // constraintLayout.setLayoutParams(layoutParams);//setam dimensiunea

        now = Calendar.getInstance();

        System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
                + "-"
                + now.get(Calendar.DATE)
                + "-"
                + now.get(Calendar.YEAR));

        //create an array of days
        final String[] strDays = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thusday",
                "Friday",
                "Saturday"
        };
        final String[] strMonths = new String[]{
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };
        //zi+luna +day of week+year
        //Day_OF_WEEK starts from 1 while array index starts from 0
        System.out.println("Current day is : " +
                strDays[now.get(Calendar.DAY_OF_WEEK) - 1]
        );
        textViewData.setText(strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ora1 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ora2 = adapterView.getItemAtPosition(i).toString();
                try {
                    setOra();
                } catch (ParseException e) {
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
        textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


        String[] ore = {"8am", "9am", "10am", "11am", "12am", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm"};
// Get the GridView layout
        //gridViewOre = (GridView) findViewById(R.id.gridViewOre);
        //ArrayAdapter mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, ore);
        //gridViewOre.setAdapter(mAdapter);

        //Prepare DataSet
        mEmpDataSet = prepareDataSet();
        //Initialize Grid View for programming
        GridView gridview = (GridView) findViewById(R.id.gridViewClienti);
       gridDay = (GridView) findViewById(R.id.gridViewDay);
        final GridView gridShift = (GridView) findViewById(R.id.gridViewShift);

        //Connect DataSet to Adapter
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(this, mEmpDataSet);
        final ShiftAdapter shiftAdapter = new ShiftAdapter(this, mEmpDataSet);


        //Now Connect Adapter To GridView
        gridview.setAdapter(employeeAdapter);
        gridShift.setAdapter(shiftAdapter);

        //Add Listener For Grid View Item Click
        //gridview.setOnItemClickListener(this);
        gridShift.setOnItemClickListener(this);


        previousDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(countDays!=0) {
                    System.out.println("Count:"+countDays);
                    countDays--;
                    previousDay.setEnabled(true);

                    now.add(Calendar.DATE, -1);//scadem zile
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(now.getTime());
                    Log.v("PREVIOUS DATE : ", formattedDate);
                    textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));
                    //dayChanged=true;
                    //reset gridviewShift;
                    //  for(int i=0;i<day.length;i++)
                    // {
                    // System.out.println("DAY: "+i+" "+day[i]);
                    //  }


                    gridShift.setAdapter(shiftAdapter);
                }
                else
                {
                    previousDay.setEnabled(false);
                    nextDay.setEnabled(true);
                }


            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (countDays < 6) {
                    System.out.println("Count:"+countDays);
                    countDays++;
                    previousDay.setEnabled(true);
                    nextDay.setEnabled(true);
                    now.add(Calendar.DATE, 1);//adaugam zile
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(now.getTime());

                    Log.v("NEXT DATE : ", formattedDate);
                    textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


                    gridShift.setAdapter(shiftAdapter);
                }
                else
                {
                    nextDay.setEnabled(false);
                }
            }
        });
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //asociere client cu shift

                Map<String, Object> shift_week = new HashMap<>();
                for(int i=0;i<mEmpDataSet.size();i++) {
                    Client clientInfo = new Client();
                    clientInfo = mEmpDataSet.get(i);
                    System.out.println("Clienti after setting shift:" + clientInfo.toStringShift());
                    List<String> day = clientInfo.getDay();
                    for (int j = 0; j < day.size(); j++) {

                        switch (j) {
                            case 0:
                                shift_week.put("Monday", day.get(j));
                                break;
                            case 1:
                                shift_week.put("Tuesday", day.get(j));
                                break;
                            case 2:
                                shift_week.put("Wednesday", day.get(j));
                                break;
                            case 3:
                                shift_week.put("Thusday", day.get(j));
                                break;
                            case 4:
                                shift_week.put("Friday", day.get(j));

                                break;
                            case 5:
                                shift_week.put("Saturday", day.get(j));

                                break;
                            case 6:
                                shift_week.put("Sunday", day.get(j));
                                break;
                        }
                    }

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("day");
                    databaseReference.setValue(shift_week);


                }
                Log.v("Database update","update");
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //face shift greeview insizibil
                now = Calendar.getInstance();

               gridShift.setVisibility(View.INVISIBLE);
                gridLayout.setVisibility(View.INVISIBLE);
                gridLayout2.setVisibility(View.VISIBLE);
                gridDay.setVisibility(View.VISIBLE);

                //CustomAdapter adapter = new CustomAdapter(getBaseContext(), aList,R.layout.shift,from, to);
                // gridShift.setAdapter(adapter);
                 DayAdapter dayAdapter= new DayAdapter(getApplicationContext(), mEmpDataSet);
                 gridDay.setAdapter(dayAdapter);
                // gridDay.setStretchMode();
                btnWeek.setVisibility(View.INVISIBLE);
                btnDay.setVisibility(View.VISIBLE);
                //afisare zile current day-day+6
                //String currentDay=(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));
               // String lastDay=(strDays[now.get(Calendar.DAY_OF_WEEK) +5] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));
//                textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1]+"-"+strDays[now.get(Calendar.DAY_OF_WEEK) +5] + " " + strMonths[now.get(Calendar.MONTH)] +  " " + now.get(Calendar.YEAR));
                //textViewDay.setText(currentDay+"-"+lastDay);
                //textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1]+"-"+strDays[now.get(Calendar.DAY_OF_WEEK) +5] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));
            }
        });
        btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //face shift greeview insizibil

                gridShift.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.VISIBLE);
                gridLayout2.setVisibility(View.INVISIBLE);
                gridDay.setVisibility(View.INVISIBLE);
            //    textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


                //CustomAdapter adapter = new CustomAdapter(getBaseContext(), aList,R.layout.shift,from, to);
                // gridShift.setAdapter(adapter);
               // DayAdapter dayAdapter= new DayAdapter(getApplicationContext(), mEmpDataSet);
                //gridDay.setAdapter(dayAdapter);
                // gridDay.setStretchMode();
                btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);


            }
        });
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, final View viewShift, final int position, long l) {
        // String ora=" ";
        //Show Name Of The Flower
        // Toast.makeText(getApplicationContext(), mEmpDataSet.get(position).getNume(),
        //Toast.LENGTH_SHORT).show();

        //startActivity(new Intent(getApplicationContext(),Ore.class));
        //System.out.println("SIZE count:"+count+"/n SIZE emp: "+mEmpDataSet.size());
        // day=new String[mEmpDataSet.size()];

        constraintLayout.setVisibility(View.VISIBLE);
        try {
            setOra();
            btnAddShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout elment = (RelativeLayout) viewShift;
                    int count = elment.getChildCount();
                    System.out.println("Nr de copii " + count);
                    View v;
                    TextView element;
                    for (int i = 0; i < count; i++) {
                        v = elment.getChildAt(i);
                        if (v instanceof TextView || v instanceof Button /*etc.*/) {

                            String ora = ora1 + " " + ora2;
                            String[] strDays = new String[]{
                                    "Sunday",
                                    "Monday",
                                    "Tuesday",
                                    "Wednesday",
                                    "Thusday",
                                    "Friday",
                                    "Saturday"
                            };
                            System.out.println("DAY:"+strDays[now.get(Calendar.DAY_OF_WEEK)-1]);
                            String currentDay = strDays[now.get(Calendar.DAY_OF_WEEK)-1];
                            //element=((TextView) v);
                            ((TextView) v).setText(ora);
                            ((TextView) v).setBackgroundColor(Color.GREEN);
                            mEmpDataSet.get(position).setShift(ora);
                            String shiftClient = mEmpDataSet.get(position).getNume() + ":"+mEmpDataSet.get(position).getShift();
                            System.out.println("Adaugare Shit la CLient:" + shiftClient);

                           // mEmpDataSet.get(position).setShift(currentDay, ora);//adaugam ziua si shift la clientul current
                            //day[position]=ora;
                           // System.out.println("Adaugare Shit la HASHMAP:" + mEmpDataSet.get(position).MapToString());
                            int n=0;
                            switch (currentDay) {
                                case "Monday":
                                    mEmpDataSet.get(position).setDay(0,ora);
                                    break;

                                case "Tuesday":
                                    mEmpDataSet.get(position).setDay(1,ora);
                                    break;
                                case "Wednesday":
                                    mEmpDataSet.get(position).setDay(2,ora);
                                    break;
                                case "Thusday":
                                    mEmpDataSet.get(position).setDay(3,ora);
                                    break;

                                case "Friday":
                                    n=5;
                                    mEmpDataSet.get(position).setDay(4,ora);
                                    break;
                                case "Saturday":
                                     n=5;
                                    mEmpDataSet.get(position).setDay(5,ora);
                                    break;
                                case "Sunday":
                                    n=6;
                                    mEmpDataSet.get(position).setDay(6,ora);


                                    break;
                            }
                            List<String> date1=mEmpDataSet.get(position).getDay();
                            for(int j=0;j<date1.size();j++)
                            {
                                System.out.println("DAY : "+j+" "+ date1.get(j));
                            }



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



        databaseClienti.child("Employees").child(usr.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                List clienti = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    try {
                        Client clienInfo = ds.getValue(Client.class);
                        clienti.add(clienInfo);
                        clienti_afisati.add(clienInfo);
                        System.out.println("Client Info:" + clienInfo.toString());
                        count++;

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
        if (difference < 0) {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference = (dateMax.getTime() - date1.getTime()) + (date2.getTime() - dateMin.getTime());
        }


        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        hours = (hours < 0 ? -hours : hours);
        Log.i("======= Hours", " :: " + hours);
        textViewOra.setText(hours + " hours shift");

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









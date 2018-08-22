package com.example.pyotr.authv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class TabelClienti  extends Activity implements AdapterView.OnItemClickListener{
    private static final String AUTH_KEY = "key=AAAAMYJyzak:APA91bEr-ZQX0KVYJ1YbuOvvqHYVLpmhcF_FxHy-9akg46kNb3aIvR-lo4HXJiyTa0OucBZQfKWFIkgJktSgS8_xnaAi8QgIwsOuWwmtNptiNDr1mHqyt6TWmBRf6xCbcw4xa0cqJGuzLm-i_RLDA_bTcyckAJNwTQ";
    private TextView textViewData;
    private TextView textViewOra;
    private TextView textViewMun;
    private TextView textViewTue;
    private TextView textViewWed;
    private TextView textViewThu;
    private TextView textViewFri;
    private TextView textViewSat;
    private TextView textViewSun;
    private Boolean saptamana1=false;
    private Boolean saptamana2=false;

    private Spinner spinner1;
    private Spinner spinner2;
    private String ora1;
    private String ora2;
    private String day1;
    private String day2;
    private Button btnAddShift;
    private Button btnPublish;
    private Button btnWeek;

    private Button btnAddMap;
    private Button btnlogout;
    ConstraintLayout constraintLayout;
    GridLayout gridLayout;
    private Button btnDay;
    GridLayout gridLayout2;
   private  GridView gridDay;
   private GridView gridShift;
   private GridView gridview;
    private EmployeeAdapter employeeAdapter;
    private ShiftAdapter shiftAdapter;
    private DayAdapter weekAdapter;
    private ShiftDayAdapter shiftDayAdapter;
    private Boolean firstSchedule=false;

    private static final String TAG = "TabelClientiActivity";
    DatabaseReference databaseClienti;
    DatabaseReference managerRef;
    DatabaseReference databaseReference;
    FirebaseUser usr;
    private TextView textViewDay;
    private Calendar now;
    private ImageView nextDay;
    private ImageView previousDay;
    private String[] strDays;
    private String[] strMonths;
    private String scheduleState="";
    int count = 0;
    int countDays=0;
    // private static Map<Integer, String[]> saptamanal;

    private Client user;

    // private String[] day={"off","off","off","off","off","off","off"};


    ArrayList<String[]> saptamana = new ArrayList<String[]>();

    ArrayList<Client> clienti_afisati = new ArrayList<>();
   private ArrayList<Client> mEmpDataSet = new ArrayList<>();


    List<HashMap<String, String[]>> aList;
    String[] from = {"Mon"};
    // String[] from = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    //int[] to = {R.id.data1, R.id.data2, R.id.data3, R.id.data4, R.id.data4, R.id.data5, R.id.data6, R.id.data7};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift);




        usr=FirebaseAuth.getInstance().getCurrentUser();


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);/////   android.os.NetworkOnMainThreadException
           //at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(
        }





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
        managerRef = FirebaseDatabase.getInstance().getReference("manager");
        textViewDay = findViewById(R.id.textViewSapt);
        textViewMun=(TextView) findViewById(R.id.textViewMun);
        textViewTue=(TextView) findViewById(R.id. textViewTue);
        textViewWed=(TextView) findViewById(R.id.textViewWed);
        textViewThu =(TextView) findViewById(R.id.textViewThu);
        textViewFri=(TextView) findViewById(R.id.textViewFri);
        textViewSat=(TextView) findViewById(R.id.textViewSat);
        textViewSun=(TextView) findViewById(R.id.textViewSun);
        constraintLayout = (ConstraintLayout) findViewById(R.id.popuplayout);
        gridLayout = (GridLayout) findViewById(R.id.GridLayout1);
        gridLayout2 = (GridLayout) findViewById(R.id.GridLayout2);
        btnAddShift = (Button) findViewById(R.id.btnAddShift);
        nextDay = (ImageView) findViewById(R.id.nextDay);
        previousDay = (ImageView) findViewById(R.id.previousDay);
        textViewData = (TextView) findViewById(R.id.textViewData);
        textViewOra = (TextView) findViewById(R.id.textViewDifOre);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnPublish = (Button) findViewById(R.id.btnPublish);
        btnWeek = (Button) findViewById(R.id.btnWeek);
        btnDay = (Button) findViewById(R.id.btnDay);
       btnAddMap = (Button) findViewById(R.id.btnAddGeofence);
        btnlogout= (Button) findViewById(R.id.btnLogOutManager);
        Button btnsendNot=(Button)findViewById(R.id.btnSendNotifcation) ;


        now = Calendar.getInstance();

        System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
                + "-"
                + now.get(Calendar.DATE)
                + "-"
                + now.get(Calendar.YEAR));

        //create an array of days
        strDays = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thusday",
                "Friday",
                "Saturday"
        };
      strMonths = new String[]{
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
        gridview = (GridView) findViewById(R.id.gridViewClienti);
        gridDay = (GridView) findViewById(R.id.gridViewDay);
        gridShift = (GridView) findViewById(R.id.gridViewShift);


      /*  Bundle e = getIntent().getExtras();
        if (e != null) {


            scheduleState = e.getString("keySchedule");
            System.out.println("Schedule State Bundle:" + scheduleState);

        }
        else
        {
            System.out.println("Schedule State Bundle este null");
        }*/


        //Prepare DataSet
        //first();

       // checkScheduleNew();

       prepareDataSet();


       //verificam daca admin a adaugat shifturi
        //Initialize Grid View for programming


        //Connect DataSet to Adapter
        //EmployeeAdapter employeeAdapter = new EmployeeAdapter(this, mEmpDataSet);
       // ShiftAdapter shiftAdapter = new ShiftAdapter(this, mEmpDataSet);


        //Now Connect Adapter To GridView
       // gridview.setAdapter(employeeAdapter);
        //gridShift.setAdapter(shiftAdapter);

        //Add Listener For Grid View Item Click
        //gridview.setOnItemClickListener(this);
        gridShift.setOnItemClickListener(this);


        btnsendNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushNotification("topic",usr.getUid(),"Your schedule has been posted!");
            }
        });
        textViewMun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Monday");

            }
        });
        textViewTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Tuesday");

            }
        });
        textViewWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Wednesday");

            }
        });
        textViewThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Thusday");

            }
        });
        textViewFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Friday");

            }
        });
        textViewSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Saturday");

            }
        });
        textViewSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);

                setDayShiftAdapter("Sun");

            }
        });


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
                    textViewData.setText(strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE));
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
                    textViewData.setText(strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE));


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



                    Map<String, String> shift_week = new HashMap<>();
                    for (int i = 0; i < mEmpDataSet.size(); i++) {
                        Client clientInfo = new Client();
                        clientInfo = mEmpDataSet.get(i);
                        shift_week=mEmpDataSet.get(i).getDay();
                        System.out.println("Clienti after setting shift:" + clientInfo.toStringShift());
                        System.out.println("Clienti after setting shift:" + mEmpDataSet.get(i).getDay());
                      /*  Map<String,String> day = clientInfo.getDay();
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
                        } //trebuie sa adaugam si data saptamanii*/

                        String sapt = day1 + "-" + day2;

                       // pushNotification("topic",usr.getUid());

                        //adaugam pentru prima sapt
                        if(firstSchedule)
                        {
                            pushNotification("topic",usr.getUid(),"Your schedule has been posted!");
                            databaseReference= FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("day");
                            databaseReference.setValue(mEmpDataSet.get(i).getDay());
                            if (day1 != null && day2 != null) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("sapt");

                                databaseReference.setValue(sapt);

                            }
                            databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("sapt1");
                            databaseReference.setValue(sapt);
                            databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("setSchedule");
                            databaseReference.setValue(true);


                        }
                       /* if (saptamana1) {
                            pushNotification("topic",usr.getUid(),"Your schedule has been posted!");
                            databaseReference= FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("day");
                            databaseReference.setValue(mEmpDataSet.get(i).getDay());
                            if (day1 != null && day2 != null) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("sapt");

                                databaseReference.setValue(sapt);

                            }
                            databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("setSchedule");
                            databaseReference.setValue(true);
                            databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("sapt1");
                            databaseReference.setValue(sapt);
                        }
                        else if(saptamana2)
                        {
                            pushNotification("topic",usr.getUid(),"Your schedule has been posted!");
                            databaseReference= FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("day1");
                            databaseReference.setValue(shift_week);
                            if (day1 != null && day2 != null) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(usr.getUid()).child(clientInfo.getClientId()).child("sapt1");

                                databaseReference.setValue(sapt);

                            }
                            //databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("setSchedule");
                           // databaseReference.setValue(true);
                            databaseReference = FirebaseDatabase.getInstance().getReference("manager").child(usr.getUid()).child("sapt2");
                            databaseReference.setValue(sapt);
                        }*/






                    }
                    Log.v("Database update", "update");
                }

        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //face shift greeview insizibil
                now = Calendar.getInstance();

                gridShift.setVisibility(View.INVISIBLE);
                gridview.setVisibility(View.INVISIBLE);
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
                //adaugam zilele adaugate_sapt respectiva
                try {
                    showDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                setEmpAdapter();
                setShiftAdapter();
                gridview.setVisibility(View.VISIBLE);
                gridShift.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.VISIBLE);
                gridLayout2.setVisibility(View.INVISIBLE);
                gridDay.setVisibility(View.INVISIBLE);
                textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


                //CustomAdapter adapter = new CustomAdapter(getBaseContext(), aList,R.layout.shift,from, to);

                // DayAdapter dayAdapter= new DayAdapter(getApplicationContext(), mEmpDataSet);
                //gridDay.setAdapter(dayAdapter);
                // gridDay.setStretchMode();
                btnWeek.setVisibility(View.VISIBLE);
                btnDay.setVisibility(View.INVISIBLE);


            }
        });

        btnAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);

               /* FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersRef = ref.child("locations");
                String userId = usersRef.push().getKey();
                usersRef.child(userId).child("name").setValue("User1");
                usersRef.child(userId).child("latitudine").setValue(47.64);
                usersRef.child(userId).child("longitudine").setValue(26.26);*/

            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void first()
    {


        if(!scheduleState.isEmpty()) {
            if (scheduleState.equals("ok")) {
                System.out.println("Schedule State:" + scheduleState);
                prepareDataSetNewSchedule();

            } else {
                System.out.println("Schedule State:" + scheduleState);
                prepareDataSet();
            }
        }else
        {
            System.out.println("Schedule state is null");
        }
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
                            switch (currentDay) {
                                case "Monday":
                                    mEmpDataSet.get(position).setDay("Monday",ora);
                                    break;

                                case "Tuesday":
                                    mEmpDataSet.get(position).setDay("Tuesday",ora);
                                    break;
                                case "Wednesday":
                                    mEmpDataSet.get(position).setDay("Wednesday",ora);
                                    break;
                                case "Thusday":
                                    mEmpDataSet.get(position).setDay("Thusday",ora);
                                    break;

                                case "Friday":
                                    mEmpDataSet.get(position).setDay("Friday",ora);
                                    break;
                                case "Saturday":

                                    mEmpDataSet.get(position).setDay("Saturday",ora);
                                    break;
                                case "Sunday":

                                    mEmpDataSet.get(position).setDay("Sunday",ora);


                                    break;
                            }
                            Map<String,String> date1=mEmpDataSet.get(position).getDay();

                            for (Map.Entry<String, String> entry : date1.entrySet()) {

                                System.out.println("entry key : " + entry.getKey());
                                System.out.println("Object value :" + entry.getValue());

                                System.out.println("DAY : "+entry.getKey()+" "+ entry.getValue());
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



    //pentru notificare
    //crea
    private void pushNotification(String type,String topic,String notification) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Schedule Manager");
            //jNotification.put("body", "Your schedule has been posted!");
            jNotification.put("body", notification);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_notification");

            jData.put("picture", "http://opsbug.com/static/google-io.jpg");

            switch(type) {
              /*  case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;*/
                case "topic":
                    String topics="/topics/"+topic;
                    jPayload.put("to", topics);
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                  //  mTextView.setText(resp);
                    Log.i(TAG,resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
    public void setEmpAdapter()
    {

        if(!mEmpDataSet.isEmpty())
        {
             employeeAdapter = new EmployeeAdapter(this, mEmpDataSet);
            gridview.setAdapter(employeeAdapter);
        }
        else
        {
            Log.i("Info","mEmpDataSet is empty");
        }

    }
    public void setDayShiftAdapter(String day)
    {

        if(!mEmpDataSet.isEmpty())
        {
             shiftDayAdapter= new ShiftDayAdapter(this, mEmpDataSet,day);
             gridDay.setAdapter(shiftDayAdapter);

        }
        else
        {
            Log.i("Info","mEmpDataSet is empty");
        }

    }
    public void setShiftAdapter()
    {

        if(!mEmpDataSet.isEmpty())
        {
            shiftAdapter=new ShiftAdapter(this,mEmpDataSet);
            gridShift.setAdapter(shiftAdapter);
        }
        else
        {
            Log.i("Info","mEmpDataSet is empty");
        }

    }
    public void setWeekAdapter()
    {
        if(!mEmpDataSet.isEmpty())
    {
        weekAdapter=new DayAdapter(this,mEmpDataSet);
           gridDay.setAdapter(weekAdapter);
    }
    else
    {
        Log.i("Info","mEmpDataSet is empty");
    }

    }
    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.newSchedule: {
                Toast.makeText(getApplicationContext(),"Add new Schedule",Toast.LENGTH_SHORT).show();
                addNewSchedule();
                return true;
            }
            case R.id.editSchedule: {
                Toast.makeText(getApplicationContext(),"Edit this Schedule",Toast.LENGTH_SHORT).show();
                //editSchedule();
                return true;
            }
            case R.id.addEmployer:
            {
                //addEmployer activity
                return true;
            }
            case R.id.showToken:
            {
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(TabelClienti.this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle("Schedule Manager Employeer Token")
                        .setMessage("Send this to your employee:"+usr.getUid())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();



                //addEmployer activity
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public long getLongAsDate(int year, int month, int date) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }
    private void addNewSchedule() {

        saptamana2=true;
        setEmpAdapter();
        setShiftAdapter();
        gridview.setVisibility(View.VISIBLE);
        gridShift.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        gridLayout2.setVisibility(View.INVISIBLE);
        gridDay.setVisibility(View.INVISIBLE);
        btnWeek.setVisibility(View.VISIBLE);
        btnDay.setVisibility(View.INVISIBLE);

        if(saptamana2)
        {
            for(int i=0;i<mEmpDataSet.size();i++) {
                mEmpDataSet.get(i).resetSapt();
            }
        }
        managerRef.child(usr.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              //  Boolean check=dataSnapshot.getValue(Managar.class).getSetSchedule();
                String[] zile=dataSnapshot.getValue(Managar.class).getSapt1().split("-");
                Integer lastDayScheduele= extractDigits(zile[1]);

                long startTime = getLongAsDate( now.get(Calendar.YEAR), now.get(Calendar.MONTH), lastDayScheduele);

                    now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), lastDayScheduele+1);

                    textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK) - 1] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));

                    textViewData.setText(strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE));
                    Integer zicurenta=now.get(Calendar.DATE);
                    //adaugam zilele adaugate_sapt respectiva
                    // try {
                    //  showDate();
                    // } catch (ParseException e) {
                    //    e.printStackTrace();
                    // }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });



    }


    public Integer extractDigits(String src) {
        String day=src;
        if(src!=null) {
            System.out.println("Dayin extract digits"+day);

            day = day.replaceAll("\\D+", "");

        }

        return Integer.parseInt(day);
    }
    public void checkScheduleState()
    {
        managerRef.child(usr.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //trebuie sa verificam status
                ///daca status is false atunci trebuie sa facem redirect la Intentul de adaugare emp
                //daca status is true atunci mergem la TabelClienti

                Boolean check=dataSnapshot.getValue(Managar.class).getSetSchedule();

                if(check)
                {

                    now = Calendar.getInstance();
                    saptamana1=true;


                    gridShift.setVisibility(View.INVISIBLE);
                    gridLayout.setVisibility(View.INVISIBLE);
                    gridLayout2.setVisibility(View.VISIBLE);
                    gridDay.setVisibility(View.VISIBLE);

                    //CustomAdapter adapter = new CustomAdapter(getBaseContext(), aList,R.layout.shift,from, to);
                    // gridShift.setAdapter(adapter);
                    //setEmpAdapter();
                    setWeekAdapter();
                    // gridDay.setStretchMode();
                    btnWeek.setVisibility(View.INVISIBLE);
                    btnDay.setVisibility(View.VISIBLE);
                   textViewDay.setText(mEmpDataSet.get(0).getSapt()+", "+strMonths[now.get(Calendar.MONTH)]  + ", " + now.get(Calendar.YEAR));


                   Integer zicurenta=now.get(Calendar.DATE);
                    String[] zile = dataSnapshot.getValue(Managar.class).getSapt1().split("-");

                   if(zile.length!=0) {
                       //String[] zile = dataSnapshot.getValue(Managar.class).getSapt1().split("-");
                       // String zile=dataSnapshot.getValue(Managar.class).getSapt1();
                       System.out.println("Zile" + zile);

                       Integer lastDayScheduele = extractDigits(zile[1]);
                       System.out.println("Schedule lastDay:" + zicurenta + " -" + lastDayScheduele);
                       if (zicurenta - lastDayScheduele == 1) {
                           Toast.makeText(getApplicationContext(), "Mai este o zi si expira orarul", Toast.LENGTH_SHORT).show();
                           System.out.println("Mai este o zi si expira orarul");
                           btnDay.setVisibility(View.INVISIBLE);
                           btnWeek.setVisibility(View.INVISIBLE);

                       } else if (zicurenta - lastDayScheduele == 0) {
                           Toast.makeText(getApplicationContext(), "Astazi expira orarul.Ar trebui sa il reinoiti", Toast.LENGTH_SHORT).show();
                           System.out.println("Astazi expira orarul.Ar trebui sa il reinoiti");
                           btnDay.setVisibility(View.INVISIBLE);
                           btnWeek.setVisibility(View.INVISIBLE);

                       }
                   }

                    //adaugam zilele adaugate_sapt respectiva
                   // try {
                      //  showDate();
                   // } catch (ParseException e) {
                    //    e.printStackTrace();
                   // }
                }
                else
                {
                    //atunci face primul orar
                    firstSchedule=true;
                    FirebaseMessaging.getInstance().subscribeToTopic(usr.getUid());

                    setEmpAdapter();
                    setShiftAdapter();


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
    private void prepareDataSetNewSchedule() {
        Query queryRef = databaseClienti.child("Employees").child(usr.getUid()).orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    Log.i("Count ", "" + dataSnapshot.getChildrenCount());
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            Map<String, Object> mapObj = (Map<String, Object>) obj;
                            Client itemsReceived = new Client();
                            itemsReceived.setNume((String) mapObj.get("nume"));
                            itemsReceived.setClientId((String) mapObj.get("clientId"));
                            // itemsReceived.setAdded((long) mapObj.get("comment"));
                            mEmpDataSet.add(itemsReceived);
                            Log.i(TAG, "Data is:" + itemsReceived.getNume());
                            Log.i(TAG, "Data is:" + itemsReceived.getClientId());
                        }
                    }
                    setShiftAdapter();
                    setEmpAdapter();


                    //checkScheduleState();
                    //in felul asta suntem siguri ca mEmpDataSet nu e null

                    //
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void prepareDataSet() {
        Query queryRef = databaseClienti.child("Employees").child(usr.getUid()).orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    Log.i("Count angajati ", "" + dataSnapshot.getChildrenCount());
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            Map<String, Object> mapObj = (Map<String, Object>) obj;
                            Client itemsReceived = new Client();
                            itemsReceived.setNume((String) mapObj.get("nume"));
                            itemsReceived.setClientId((String) mapObj.get("clientId"));
                            itemsReceived.setPrenume((String) mapObj.get("prenume"));
                            itemsReceived.setPozitie((String) mapObj.get("pozitie"));
                           // if((  Map<String, Object>) mapObj.get("day").equals(null))
                            itemsReceived.setDay((  Map<String, String>) mapObj.get("day"));

                                itemsReceived.setSapt((String) mapObj.get("sapt"));

                                // No such key


                            // itemsReceived.setAdded((long) mapObj.get("comment"));
                            mEmpDataSet.add(itemsReceived);
                          Log.i(TAG, "Data is:" + itemsReceived.getNume()+"-"+itemsReceived.getPrenume()+"-"+itemsReceived.getPozitie());
                          Log.i(TAG, "Data is:" + itemsReceived.getClientId());
                        }
                    }


                    checkScheduleState();
                      //in felul asta suntem siguri ca mEmpDataSet nu e null

                    //
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    }

    public void showDate() throws ParseException {
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
       // now = Calendar.getInstance();
        String dayCur=strDays[now.get(Calendar.DAY_OF_WEEK) - 1];

        String day=now.get(Calendar.DATE)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.YEAR);
        System.out.println("DAY:"+day);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date myDate = dateFormat.parse(day);
        Date newDate = new Date(myDate.getTime() + 604800000L); // 6 * 24 * 60 * 60 * 1000

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, +6);

        int indexDay=calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("DAY:Index"+indexDay);

        Date nextDate = calendar.getTime();
        String date = dateFormat.format(newDate);

        String[] part=date.split("-");
        String dayNext=strDays[indexDay-1];
        System.out.println("DAY:"+part[0]);
        day1=dayCur+now.get(Calendar.DATE);
        day2=dayNext+calendar.get(Calendar.DATE);

        textViewDay.setText(dayCur+now.get(Calendar.DATE)+"-"+dayNext+calendar.get(Calendar.DATE) + ", " + strMonths[now.get(Calendar.MONTH)]  + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


    }

}









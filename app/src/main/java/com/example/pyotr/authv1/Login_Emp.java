package com.example.pyotr.authv1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;

public class Login_Emp extends Activity  {
    ArrayList<Client> mEmpDataSet = new ArrayList<>();
    DatabaseReference databaseEmp;
    DatabaseReference empRef;
    private GridView gridViewWeek;
    private TextView textViewDay;
    private Calendar now;
    private Button logout;
    private TextView textViewNume;
    private TextView textViewShift;
   List<String> das=new ArrayList<String>();
    String json="";
    List<String> date=new ArrayList<>();
    String managerId="";

    //String nume="";
    int count=0;
 // public String[] day={"off","off","off","off","off","off","off"};
    private String[] strDays = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thusday",
            "Friday",
            "Saturday"
    };
    private String[] strMonths = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_angajat);
        databaseEmp = FirebaseDatabase.getInstance().getReference();
        empRef = FirebaseDatabase.getInstance().getReference("angajati");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            managerId= extras.getString("key");

        }


      //  mEmpDataSet = prepareDataSet();

        gridViewWeek=(GridView)findViewById(R.id.gridViewWeek) ;
        textViewDay=(TextView)findViewById(R.id.textViewSapt) ;
        textViewNume=(TextView)findViewById(R.id.userCurrent) ;
        textViewShift=(TextView) findViewById(R.id.textViewShiftDay) ;
        logout=(Button) findViewById(R.id.btnOut);
        try {
            showDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // DayAdapter dayAdapter= new DayAdapter(getApplicationContext(), mEmpDataSet);
       // gridViewWeek.setAdapter(dayAdapter);




        // Populate a List from Array elements


        // Create a new ArrayAdapter

       getNume();
        //prepareDataSet();


   // String[] ore = new String[das.size()];
     // ore = das.toArray(ore);
   // gridViewWeek.setAdapter(new AdapterOre(this,ore));
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    });


    }
    public void setAdapter()
    {
        if(!das.isEmpty()) {
        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, das);
        gridViewWeek.setAdapter(gridViewArrayAdapter);

            String[] ore = new String[das.size()];
            ore = das.toArray(ore);
             //gridViewWeek.setAdapter(new AdapterOre(this,ore));
    }else
    {
        Log.i("Info","Das is empty");
    }

    }




    public void getNume() {
        FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();


        empRef.child(userCurrent.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              String nume="";
              String manger="";
                System.out.println("Ang:" + dataSnapshot);
                nume=(dataSnapshot.getValue(Employee.class).getNume());
                System.out.println("Ang nume:" + nume);
                manger=( dataSnapshot.getValue(Employee.class).getManager());

                getData(nume,manger);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
    }


    public void getData(final String nume, String manager)
    {



        if(!nume.isEmpty()) {
            Query queryRef = databaseEmp.child("Employees").child(manager).orderByKey();
            queryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                        Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        ArrayList<Client> itemsReceivedList = new ArrayList<>();

                        for (Object obj : objectMap.values()) {
                            if (obj instanceof Map) {
                                Map<String, Object> mapObj = (Map<String, Object>) obj;
                                Client itemsReceived = new Client();
                                itemsReceived.setNume((String) mapObj.get("nume"));
                                itemsReceived.setClientId((String) mapObj.get("clientId"));
                                // itemsReceived.setAdded((long) mapObj.get("comment"));
                                itemsReceivedList.add(itemsReceived);
                                // Log.e(TAG, "Data is:" + itemsReceived.getNume());
                                // Log.e(TAG, "Data is:" + itemsReceived.getClientId());
                                if (itemsReceived.getNume().equals(nume)) {
                                    textViewNume.setText("Welcome, " + (String) mapObj.get("nume") + " " + (String) mapObj.get("prenume"));
                                    String userCurrent = itemsReceived.getClientId();
                                    Log.e(TAG, "Data is:" + itemsReceived.getNume());
                                    Log.e(TAG, "Data is:" + itemsReceived.getClientId());
                                    Object object = (Object) mapObj.get("day");
                                    json = new Gson().toJson(object);

                                    System.out.println("LOGIN date:" + json);
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(json);
                                        // Getting the value of a attribute in a JSONObject
                                        das.add(jsonObject.getString("Sunday"));
                                        das.add(jsonObject.getString("Monday"));
                                        das.add(jsonObject.getString("Tuesday"));
                                        das.add(jsonObject.getString("Wednesday"));
                                        das.add(jsonObject.getString("Thusday"));
                                        das.add(jsonObject.getString("Friday"));
                                        das.add(jsonObject.getString("Saturday"));

                                        setAdapter();//setam adapter cu orele clientului
                                        showShift();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }

                        // itemsRecievedListAdapter = new ItemsRecievedListAdapter(MainActivity.this, itemsReceivedList);
                        // mRecyclerView.setAdapter(itemsRecievedListAdapter);
                        //
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Log.i("Info login:","date is empty");
        }
    }
    public void showShift() {
        now = Calendar.getInstance();
        int index = now.get(Calendar.DAY_OF_WEEK) - 1;
        String dayCur = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];

        String shiftCurrent = das.get(index);
        System.out.println("Shift:" + dayCur+"-"+shiftCurrent + "-" + index);
        String shiftFinal = "";
        if (shiftCurrent.equals("off")) {
            textViewShift.setText("Off");
        } else {
            String[] part = shiftCurrent.split(" ");
            textViewShift.setText(part[0] + "-" + part[1]);


        }
    }

    public void showDate() throws ParseException {
        now = Calendar.getInstance();
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

         textViewDay.setText(dayCur+now.get(Calendar.DATE)+"-"+dayNext+calendar.get(Calendar.DATE) + ", " + strMonths[now.get(Calendar.MONTH)]  + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));


    }


}

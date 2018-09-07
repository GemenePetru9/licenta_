package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Angajat_Shift extends Activity implements View.OnClickListener{

    List<String> das=new ArrayList<>();
    Button mButton1;
    Button mButton2;
    Button mButton3;
    TextView mText1;
    private GridView gridViewWeek;
    private String managerID;
    private TextView textViewNume;
    private TextView textViewSapt;
    private TextView textViewOpenShift;
    private TextView takeOpenShift;
    private GridView gridDay;
   private DatabaseReference databaseClienti;
    private ArrayList<Client> mEmpDataSet = new ArrayList<>();
    private DayAdapter weekAdapter;
    private GridLayout gridLayoutDayz;
    private LinearLayout linearLayoutOpenShift;
    private Calendar now;
    private String[] strDays;
    private String currentDay;
   private Boolean openShiftState;
   private Boolean acceptShift=false;
    DatabaseReference empRef;
    private DatabaseReference databaseEmp;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.angajat_shift_week);

        databaseClienti = FirebaseDatabase.getInstance().getReference();

        gridViewWeek=(GridView)findViewById(R.id.gridViewWeek) ;
        textViewNume=(TextView)findViewById(R.id.userCurrent) ;
        textViewSapt=(TextView) findViewById(R.id.textViewSapt) ;
        textViewOpenShift=findViewByIdAndCast(R.id.textViewOpen);
        gridDay = (GridView) findViewById(R.id.gridViewDay);
        gridLayoutDayz=findViewByIdAndCast(R.id.GridLayout2);
        linearLayoutOpenShift=findViewByIdAndCast(R.id.OpenShiftLayout);
        takeOpenShift=findViewById(R.id.textViewOpenShiftAccept);

        empRef = FirebaseDatabase.getInstance().getReference("angajati");
        databaseEmp = FirebaseDatabase.getInstance().getReference();



        now= Calendar.getInstance();
        strDays = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thusday",
                "Friday",
                "Saturday"
        };
        currentDay= strDays[now.get(Calendar.DAY_OF_WEEK) - 1];


        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton3.setEnabled(false);
        mButton3.setAlpha(.8f);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);

        Bundle open = getIntent().getExtras();
        if (open != null ){


           openShiftState = open.getBoolean("openShiftState");
           String manager=open.getString("managerID");
            System.out.println("Open openShiftState" + openShiftState+manager);
            if(openShiftState) {
               // textViewNume.setText(open.getString("nume")+", this week shift");
                //mButton1.setEnabled(false);
               // mButton2.setEnabled(false);
                mButton3.setEnabled(true);
                mButton3.setAlpha(1);
                mButton3.setSelected(true);
                mButton3.setPressed(true);
                allShifts(manager);

            }

        }
        else
        {
            System.out.println("Open Shift openBundle este null");
        }


        Bundle extras = getIntent().getExtras();
       if (extras != null&& extras.getString("managerID")!=null) {

           textViewNume.setText(extras.getString("nume")+", this week shift");
           System.out.println("Date from intent:"+extras.getString("nume"));
           das.add(extras.getString("Monday"));
          /// das.set(0,Monday);
           das.add(extras.getString("Tuesday"));
           das.add(extras.getString("Wednesday"));
           das.add(extras.getString("Thusday"));
           das.add(extras.getString("Friday"));
           das.add(extras.getString("Saturday"));
           das.add(extras.getString("Sunday"));
           managerID=extras.getString("managerID");
           System.out.println("Date from intent:"+(extras.getString("Monday"))+"ID:"+managerID);

           if(managerID!=null) {
               getNume(managerID,false);
           }
        }

        takeOpenShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shift angajat curen precum si stergerea shiftului din OpenSift.
                if(managerID!=null)
                {
                    getNume(managerID,true);
                }

            }
        });


    }


    public void getNume(String id, final Boolean stateAcceptOrIgnore) {

        empRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nume="";
                String manger="";
               // System.out.println("Ang in notificare handle:" + dataSnapshot);
                nume=(dataSnapshot.getValue(Employee.class).getNume());
                String prenume=(dataSnapshot.getValue(Employee.class).getPrenume());
                textViewNume.setText(nume+" "+prenume+",this week schedule");
                System.out.println("Ang  in notificare handle:" + nume);
                manger=( dataSnapshot.getValue(Employee.class).getManager());

                getData(manger,stateAcceptOrIgnore,nume);
                // FirebaseMessaging.getInstance().unsubscribeFromTopic(manger);
               // subscribe(manger);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
    }

    public void getData(final String manager, final Boolean stateAcceptOrIgnore,final String numeleAngCurent)
    {

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
                                String numele = (String) mapObj.get("nume");
                                String shiftAngCurent="";
                                if(stateAcceptOrIgnore)
                                {
                                    if (numele.equals("Open Shift")) {

                                        shiftAngCurent=((Map<String, String>) mapObj.get("day")).get(currentDay);;
                                        //stergem shiftul deoarece a fost acceptat
                                        databaseReference= FirebaseDatabase.getInstance().getReference("Employees").child(manager).child("open_shift_id").child("day").child(currentDay);
                                        databaseReference .setValue("off");


                                    }
                                    if(numele.equals(numeleAngCurent))
                                    {
                                        String angcurentID=(String)mapObj.get("clientId");
                                        databaseReference= FirebaseDatabase.getInstance().getReference("Employees").child(manager).child(angcurentID).child("day").child(currentDay);
                                        databaseReference .setValue(shiftAngCurent);
                                    }
                                }
                                if (numele.equals("Open Shift")) {
                                    String pozitie = (String) mapObj.get("pozitie");
                                    String shift = ((Map<String, String>) mapObj.get("day")).get(currentDay);
                                    setOpenShift(shift, pozitie);

                                }
                                else {
                                    itemsReceived.setNume((String) mapObj.get("nume"));
                                    itemsReceived.setPrenume((String) mapObj.get("prenume"));
                                    itemsReceived.setPozitie((String) mapObj.get("pozitie"));
                                    // if((  Map<String, Object>) mapObj.get("day").equals(null))
                                    itemsReceived.setDay((  Map<String, String>) mapObj.get("day"));

                                    // itemsReceived.setSapt((String) mapObj.get("sapt"));
                                    itemsReceived.setCuloare(((Long) mapObj.get("culoare")).intValue());

                                }
                                // itemsReceived.setAdded((long) mapObj.get("comment"));
                                mEmpDataSet.add(itemsReceived);
                                Log.i(TAG, "Data is:" + itemsReceived.getNume()+"-"+itemsReceived.getPrenume()+"-"+itemsReceived.getPozitie());
                                Log.i(TAG, "Data is:" + itemsReceived.getClientId());


                            }
                        }
                        setWeekAdapter();


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
    public void allShifts(String managerID)
    {

        Query queryRef = databaseClienti.child("Employees").child(managerID).orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {
            public static final String TAG = "Aganajat_Shift";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    Log.i("Count angajati ", "" + dataSnapshot.getChildrenCount());
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            Map<String, Object> mapObj = (Map<String, Object>) obj;
                            Client itemsReceived = new Client();
                            String numele = (String) mapObj.get("nume");
                            if (numele.equals("Open")) {
                                String pozitie = (String) mapObj.get("pozitie");
                                String shift = ((Map<String, String>) mapObj.get("day")).get(currentDay);
                                setOpenShift(shift, pozitie);

                            }
                            else {
                                itemsReceived.setNume((String) mapObj.get("nume"));
                                itemsReceived.setPrenume((String) mapObj.get("prenume"));
                                itemsReceived.setPozitie((String) mapObj.get("pozitie"));
                                // if((  Map<String, Object>) mapObj.get("day").equals(null))
                                itemsReceived.setDay((  Map<String, String>) mapObj.get("day"));

                               // itemsReceived.setSapt((String) mapObj.get("sapt"));
                                itemsReceived.setCuloare(((Long) mapObj.get("culoare")).intValue());

                            }

                            // itemsReceived.setAdded((long) mapObj.get("comment"));
                            mEmpDataSet.add(itemsReceived);
                            Log.i(TAG, "Data is:" + itemsReceived.getNume()+"-"+itemsReceived.getPrenume()+"-"+itemsReceived.getPozitie());
                            Log.i(TAG, "Data is:" + itemsReceived.getClientId());
                        }
                    }
                    setWeekAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        // clear state
        mButton1.setSelected(false);
        mButton1.setPressed(false);
        mButton2.setSelected(false);
        mButton2.setPressed(false);
        mButton3.setSelected(false);
        mButton3.setPressed(false);


        // change state
        button.setSelected(true);
        button.setPressed(false);

        System.out.println("Selected:"+button.getText());

        switch (v.getId())
        {
            case R.id.button1:
                //MyShift
                gridViewWeek.setVisibility(View.VISIBLE);
                gridLayoutDayz.setVisibility(View.VISIBLE);

                gridDay.setVisibility(View.INVISIBLE);
                linearLayoutOpenShift.setVisibility(View.INVISIBLE);
                break;
            case R.id.button2:
                //All Shifts
                gridDay.setVisibility(View.VISIBLE);
                gridViewWeek.setVisibility(View.INVISIBLE);
                gridLayoutDayz.setVisibility(View.INVISIBLE);
                linearLayoutOpenShift.setVisibility(View.INVISIBLE);


                break;
            case R.id.button3:
                //Open Shifts
                linearLayoutOpenShift.setVisibility(View.VISIBLE);

                gridDay.setVisibility(View.INVISIBLE);
                gridViewWeek.setVisibility(View.INVISIBLE);
                gridLayoutDayz.setVisibility(View.INVISIBLE);


                break;
        }
    }
    private void setOpenShift(String shift,String pozitie)
    {


        if(shift!=null)
        {
            textViewOpenShift.setText(shift+" "+pozitie);
        }
    }


    @SuppressWarnings("unchecked")
    private <T> T findViewByIdAndCast(int id) {
        return (T) findViewById(id);
    }

}

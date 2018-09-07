package com.example.pyotr.authv1;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
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
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;

public class Login_Emp extends Activity  {
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    private static final String AUTH_KEY = "key=AAAAMYJyzak:APA91bEr-ZQX0KVYJ1YbuOvvqHYVLpmhcF_FxHy-9akg46kNb3aIvR-lo4HXJiyTa0OucBZQfKWFIkgJktSgS8_xnaAi8QgIwsOuWwmtNptiNDr1mHqyt6TWmBRf6xCbcw4xa0cqJGuzLm-i_RLDA_bTcyckAJNwTQ";
    private static Boolean clockState=false;
    ArrayList<Client> mEmpDataSet = new ArrayList<>();
    DatabaseReference databaseEmp;
    DatabaseReference empRef;
   List<String> das=new ArrayList<String>();
    List<String> dateToIntent=new ArrayList<String>();
    String json="";
    List<String> date=new ArrayList<>();
    String managerId="";
    //String nume="";
    int count=0;
    private TextView textViewDay;
    private Calendar now;
    private Button logout;
    private TextView textViewNume;
    private TextView textViewShift;
    private Button clockin;
    private String numele="";
    private String managerul="";
    private ImageView imageViewSick;
    private ImageView imageViewLate;
    private String currentDayShift="";
    private FirebaseUser usr;
    private  int h=0;
    private  int s=0;
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
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private TextView tvEventStart;
    private Handler handler;
    private Runnable runnable;
    private RelativeLayout relativeLayoutCountDown;

    // Create a Intent send by the notification
   public static Intent makeNotificationIntent(Context context, String msg) {

        clockState=true;
        //am primit notification
        Intent intent = new Intent( context, Login_Emp.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_angajat);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);/////   android.os.NetworkOnMainThreadException
            //at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(
        }

        databaseEmp = FirebaseDatabase.getInstance().getReference();


        empRef = FirebaseDatabase.getInstance().getReference("angajati");

        usr=FirebaseAuth.getInstance().getCurrentUser();
        clockin=(Button)findViewById(R.id.button) ;
        imageViewSick= findViewById(R.id.imageViewSick);
        imageViewLate=findViewById(R.id.imageViewLate);
        relativeLayoutCountDown=findViewById(R.id.relativeLayoutCountDown);


        txtHour = (TextView) findViewById(R.id.txtHour);
        txtMinute = (TextView) findViewById(R.id.txtMinute);
        txtSecond = (TextView) findViewById(R.id.txtSecond);
        tvEventStart = (TextView) findViewById(R.id.tveventStart);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            managerId= extras.getString("key");

        }


        //primim datele de la notificare si facem redirect catre openSHift si facem butonul OpneShift Enable
       Bundle open = getIntent().getExtras();
        if (open != null &&open.getBoolean("openShift")){

            Boolean openShiftState = open.getBoolean("openShift");
            System.out.println("Open ShiftState" + openShiftState);
            Intent intent = new Intent(getBaseContext(), Angajat_Shift.class);
            intent.putExtra("openShiftState", openShiftState);
            intent.putExtra("managerID", usr.getUid());
            if(numele!=null)
            {
                intent.putExtra("nume",numele);
            }

            startActivity(intent);

        }
        else
        {
            System.out.println("Open Shift openBundle este null");
        }


        Bundle extrasMap = getIntent().getExtras();
        if (extrasMap != null) {


            clockState = extrasMap.getBoolean("keyMap");
            if (clockState) {
                //clientul este in zona
                clockin.setText("Clock out");


                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute  = Calendar.getInstance().get(Calendar.MINUTE);
                s=minute;
                h=hour;

                System.out.println("Clockin Succes la ora: " + hour+":"+minute);
                Toast.makeText(getApplicationContext(), "Clock in Succesful", Toast.LENGTH_SHORT).show();


                //adaugam ora in baza de date si cand da clock out
            } else {
                Toast.makeText(getApplicationContext(), "Trebuie sa fiti in zona de lucruc pentru a putea da clock in", Toast.LENGTH_SHORT).show();
            }
        }

        if(h!=0&&s!=0)
        {
            countDownStart(h,s);
            System.out.println("Merge"+h+"-"+s);
        }
        else
        {
            System.out.println("Ore null");
        }






        //  mEmpDataSet = prepareDataSet();


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
    clockin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            //verificam
            Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
            startActivity(intent);
        }
    });


        ImageView imageButton2= findViewById(R.id.imageViewShift);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(),"View shift is clicked",Toast.LENGTH_LONG).show();

               // Intent i = new Intent(Login_Emp.this, Angajat_Shift.class);
               // startActivity(i);

                Intent intent = new Intent(getApplicationContext(),Angajat_Shift.class);
                if(!dateToIntent.isEmpty()) {
                    if(numele!=null)
                    {
                        intent.putExtra("nume",numele);
                    }

                    intent.putExtra("Sunday", dateToIntent.get(0));
                    intent.putExtra("Monday", dateToIntent.get(1));
                    intent.putExtra("Tuesday", dateToIntent.get(2));
                    intent.putExtra("Wednesday", dateToIntent.get(3));
                    intent.putExtra("Thusday", dateToIntent.get(4));
                    intent.putExtra("Friday", dateToIntent.get(5));
                    intent.putExtra("Saturday", dateToIntent.get(6));
                    intent.putExtra("managerID",managerul);
                    System.out.println("Date to intent:"+dateToIntent.get(6));
                    System.out.println("Date to intent:"+managerul);
                }
                Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slideinleft, R.anim.slideinright).toBundle();

                startActivity(intent, bndlAnimation);
            }
        });

        imageViewSick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //alert dialog
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(Login_Emp.this, android.R.style.Theme_Material_Dialog_Alert);
               // if (currentDayShift.equals("off")) {
                   // Toast.makeText(Login_Emp.this, "Today you are OFF, you can't announce sick!", Toast.LENGTH_SHORT).show();
                //} else {

                    builder.setTitle("Are you sure you want to anounce sick for today?")
                            //.setMessage("Send this to your employee:"+usr.getUid())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                    empRef.child(usr.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String topic = (dataSnapshot.getValue(Employee.class).getManager()) + "_MANAGER";

                                            String nume = "";
                                            System.out.println("Ang in anunta sick" + dataSnapshot);
                                            nume += (dataSnapshot.getValue(Employee.class).getNume());
                                            nume +=" "+(dataSnapshot.getValue(Employee.class).getPrenume());
                                            System.out.println("Ang nume:" + nume);

                                            pushNotification("topic", topic, nume + " is sick today!","SICK");


                                        }


                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                            // ...
                                        }


                                    });

                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
           // }
        });
        imageViewLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //alert dialog
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(Login_Emp.this, android.R.style.Theme_Material_Dialog_Alert);
                // if (currentDayShift.equals("off")) {
                // Toast.makeText(Login_Emp.this, "Today you are OFF, you can't announce sick!", Toast.LENGTH_SHORT).show();
                //} else {

                builder.setTitle("Are you sure you want to anounce LATE for today?")
                        //.setMessage("Send this to your employee:"+usr.getUid())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                empRef.child(usr.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String topic = (dataSnapshot.getValue(Employee.class).getManager()) + "_MANAGER";

                                        String nume = "";
                                        System.out.println("Ang in anunta late" + dataSnapshot);
                                        nume += (dataSnapshot.getValue(Employee.class).getNume());
                                        nume +=" "+(dataSnapshot.getValue(Employee.class).getPrenume());
                                        System.out.println("Ang nume:" + nume);

                                        pushNotification("topic", topic, nume + " announce that is going to be late!","LATE");


                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message
                                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                        // ...
                                    }


                                });

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
            // }
        });






    }

    public void countDownStart(final int hourClockIn, final int secondsClockIn) {
       relativeLayoutCountDown.setVisibility(View.VISIBLE);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "HH:mm:ss");
                    // Please here set your event date//YYYY-MM-DD


                    String shiftEnd=textViewShift.getText().toString();
                    String[] part = shiftEnd.split("-");
                    Date futureDate = dateFormat.parse(part[1]+":00");
                    String h=Integer.toString(hourClockIn);
                    String min=Integer.toString(secondsClockIn);
                    String shiftCurent=h+":"+min+":"+"00";
                    Date currentDate = dateFormat.parse(shiftCurent);
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText("" + String.format("%02d", minutes));
                        txtSecond.setText("" + String.format("%02d", seconds));
                        System.out.println("CountDown:"+seconds);
                    } else {
                        tvEventStart.setVisibility(View.VISIBLE);
                        tvEventStart.setText("The event started!");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {
        findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout3).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout4).setVisibility(View.GONE);
      //  findViewById(R.id.textViewheader1).setVisibility(View.GONE);
       // findViewById(R.id.textViewheader2).setVisibility(View.GONE);
    }

    public void subscribe(String managerID)
    {
        FirebaseMessaging.getInstance().subscribeToTopic(managerID);
    }

    public void subscribeToAvailable(String managerID)
    {
        FirebaseMessaging.getInstance().subscribeToTopic(managerID+"_AVAILABLE");
        Log.i(TAG,"Subscribe to Available");
    }

    public void unsubscribeToAvailable(String managerID)
    {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(managerID+"_AVAILABLE");
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
                // FirebaseMessaging.getInstance().unsubscribeFromTopic(manger);
                subscribe(manger);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
    }

    public void getData(final String nume, final String manager)
    {



       // final String man=manager;
      //  System.out.println("MangerID from function:"+man);
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

                                       // das.add(manager);

                                        String name=(String) mapObj.get("nume") + " " + (String) mapObj.get("prenume");

                                        //System.out.println("MangerID from function2:"+man);
                                        setAdapter(das,name,manager);//setam adapter cu orele clientului
                                        showShift(manager);
                                       // subscribeToAvailable(manager);
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

    private void setAdapter(List<String> das, String name,String man) {
        dateToIntent=new ArrayList<String>(das);
        numele=name;
        managerul=man;


    }

    public void showShift(String manager) {
        now = Calendar.getInstance();
        int index = now.get(Calendar.DAY_OF_WEEK) - 1;
        String dayCur = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];

        String shiftCurrent = das.get(index);
        System.out.println("Shift:" + dayCur+"-"+shiftCurrent + "-" + index);
        String shiftFinal = "";
        currentDayShift=shiftCurrent;
        if (shiftCurrent.equals("off")) {
            textViewShift.setText("Off");


            //facem Subscribe

           subscribeToAvailable(manager);

            //facem Subscribe pentru topic disponibil//

        } else {
            String[] part = shiftCurrent.split(" ");
            textViewShift.setText(part[0] + "-" + part[1]);

            //lucreaza deja deci un este disponibil

            //facem Subscribe pentru topic disponibil//
            unsubscribeToAvailable(manager);


        }
    }




    //notificare pentru manager

    //pentru notificare

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

    //crea
    private void pushNotification(String type,String topic,String notification,String clickAction) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Notification from employee");
            jNotification.put("body", notification);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_notification");

            //jData.put("picture", "http://opsbug.com/static/google-io.jpg");

            jData.put("picture", "http://agape-com.reggaebeatmaker.com/wp-content/uploads/2015/02/schedule_icon_image.jpg");

            //jData.put("picture", R.drawable.schedule_icon_image);
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





}

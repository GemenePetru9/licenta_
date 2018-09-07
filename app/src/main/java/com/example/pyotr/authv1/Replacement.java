package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.example.pyotr.authv1.MapsActivity.TAG;

public class Replacement extends Activity implements AdapterView.OnItemClickListener{
    private static final String AUTH_KEY = "key=AAAAMYJyzak:APA91bEr-ZQX0KVYJ1YbuOvvqHYVLpmhcF_FxHy-9akg46kNb3aIvR-lo4HXJiyTa0OucBZQfKWFIkgJktSgS8_xnaAi8QgIwsOuWwmtNptiNDr1mHqyt6TWmBRf6xCbcw4xa0cqJGuzLm-i_RLDA_bTcyckAJNwTQ";
    DatabaseReference databaseClienti;
    String numeAngSick;
    Calendar now;
    String strDays[];
    String currentDay;
    String shiftSick;
    String openShiftiId;
    String value;
    TextView textView;
    private TextView textViewAngSick;
    private  String managerId;
    private TextView send;
    private GridView gridDay;
    private ShiftReplacement shiftDayAdapter;
    private ArrayList<Client> mEmpDataSet = new ArrayList<>();
    private ArrayList<Client> mEmpOff = new ArrayList<>();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        now=Calendar.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.replacement);

        databaseClienti = FirebaseDatabase.getInstance().getReference();
        textView=findViewById(R.id.textViewAngReplacement);
        textViewAngSick=(TextView) findViewById(R.id.angSick) ;
        gridDay = (GridView) findViewById(R.id.gridViewDayReplacement);
        send=findViewById(R.id.txtReplacementSend);

        now=Calendar.getInstance();
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

        Bundle e = getIntent().getExtras();
        if (e != null) {


          numeAngSick = e.getString("numeAngSick");

            textViewAngSick.setText(numeAngSick+" is sick.Can someone cover this sift?");
            managerId= e.getString("managerId");

            System.out.println("Numele ang Sick from bundle" + numeAngSick+"- "+managerId);
            if(managerId!=null)
            {
                prepareDataSet(managerId);
            }


        }
        else
        {
            System.out.println("Numele ang Sick From Tabel Bundle este null");
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChecked();

            }
        });


    }

    public void setDayShiftAdapter(String day)
    {

        if(!mEmpOff.isEmpty())
        {
            shiftDayAdapter= new ShiftReplacement(this, mEmpOff,day);
            gridDay.setAdapter(shiftDayAdapter);
            gridDay.setOnItemClickListener(this);
           // getChecked();

        }
        else
        {
            Log.i("Info","mEmpDataSet is empty");
        }

    }

    private void prepareDataSet(String manager) {
        if (manager != null) {
            Query queryRef = databaseClienti.child("Employees").child(manager).orderByKey();
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
                                //itemsReceived.setClientId((String) mapObj.get("clientId"));
                                itemsReceived.setPrenume((String) mapObj.get("prenume"));
                                itemsReceived.setPozitie((String) mapObj.get("pozitie"));
                                // if((  Map<String, Object>) mapObj.get("day").equals(null))
                                itemsReceived.setDay((Map<String, String>) mapObj.get("day"));
                                String numele=(mapObj.get("nume"))+" "+(mapObj.get("prenume"));

                               // itemsReceived.setSapt((String) mapObj.get("sapt"));
                                itemsReceived.setCuloare(((Long) mapObj.get("culoare")).intValue());
                                if(numele.equals(numeAngSick))
                                {


                                    shiftSick=((Map<String, String>) mapObj.get("day")).get(currentDay);
                                    System.out.println("Replacement shift:"+shiftSick);

                                    textView.setText("Shift:"+shiftSick);
                                }
                                if(((Map<String, String>) mapObj.get("day")).get(currentDay).equals("off"))
                                {
                                    mEmpOff.add(itemsReceived);
                                    System.out.println("REplacement:"+((Map<String, String>) mapObj.get("day")).get(currentDay)+(String) mapObj.get("nume"));
                                }




                                // No such key


                                // itemsReceived.setAdded((long) mapObj.get("comment"));
                                mEmpDataSet.add(itemsReceived);
                                Log.i(TAG, "Data is:" + itemsReceived.getNume() + "-" + itemsReceived.getPrenume() + "-" + itemsReceived.getPozitie());
                                Log.i(TAG, "Data is:" + itemsReceived.getClientId());
                            }
                        }
                        if(shiftSick!=null)
                        {  FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();
                            String uid = usr.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Employees").child(uid).child("open_shift_id").child("day").child(currentDay);
                            databaseReference.setValue(shiftSick);
                            System.out.println("Adaugare shfickSIck in Firebase:"+shiftSick);
                            //adaugare shiftt in Open Shift
                        }
                        //luam agajatul cu numele si afsam shiftul


                        //checkScheduleState();
                        //in felul asta suntem siguri ca mEmpDataSet nu e
                        if(mEmpOff.isEmpty())
                        {
                            Toast.makeText(Replacement.this, "Niciun angajat disponibil", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            setDayShiftAdapter(currentDay);
                        }

                        //
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    //notificare pentru manager

    //pentru notificare

    public void getChecked()
    {
        Log.i(TAG, "getChecked: ");
        String id="";
        Bundle e = getIntent().getExtras();
        if (e != null) {
            id= e.getString("managerId");
            //System.out.println("Numele ang Sick" + numeAngSick+"- "+managerId);
            System.out.println("Size of emp");

            String topic =id+"_AVAILABLE";
            System.out.println("Size of emp:"+mEmpOff.size());
            for(int i=0;i<mEmpOff.size();i++)
            {
                Log.i(TAG, mEmpOff.get(i).getAvailable().toString());
                if(mEmpOff.get(i).getAvailable())
                {
                    //send notification

                    pushNotification("topic",topic);
                    Log.i(TAG, "getChecked: ");
                }
            }
        }
        else
        {
            System.out.println("Numele ang Sick From Tabel Bundle este null");
        }

        String topic =id+"_AVAILABLE";
        System.out.println("Size of emp:"+mEmpOff.size());
        for(int i=0;i<mEmpOff.size();i++)
        {
            Log.i(TAG, mEmpOff.get(i).getAvailable().toString());
            if(mEmpOff.get(i).getAvailable())
            {
                //send notification

                pushNotification("topic",topic);
                Log.i(TAG, "getChecked: ");
            }
        }

    }

    //crea
    private void pushNotification(String type,String topic) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Notification from manager");
            //jNotification.put("body", "Your schedule has been posted!");
            jNotification.put("body", "Open Shift Alert!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "_REPLACEMENT");
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
                    Log.i(ContentValues.TAG,resp);
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
    @Override
    public void onItemClick(AdapterView<?> adapterView, final View viewShift, final int position, long l) {
                    GridLayout elment = (GridLayout) viewShift;
                    int count = elment.getChildCount();
                    System.out.println("Nr de copii " + count);
                    View v;
                    CheckedTextView simpleCheckedTextView;
                    for (int i = 0; i < count; i++) {
                        v = elment.getChildAt(i);
                        if (v instanceof CheckedTextView || v instanceof Button /*etc.*/) {
                            simpleCheckedTextView=((CheckedTextView) v);

                            if (((CheckedTextView) v).isChecked()) {
                           // set cheek mark drawable and set checked property to false
                                value = "un-Checked";
                                mEmpOff.get(position).setAvailable(false);
                                simpleCheckedTextView.setCheckMarkDrawable(0);
                                simpleCheckedTextView.setChecked(false);
                            } else {
                            // set cheek mark drawable and set checked property to true
                                value = "Checked";
                                simpleCheckedTextView.setCheckMarkDrawable(R.drawable.checked);
                                simpleCheckedTextView.setChecked(true);
                                mEmpOff.get(position).setAvailable(true);
                            }

                        }
                    }
                }



    }

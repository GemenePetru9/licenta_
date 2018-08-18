package com.example.pyotr.authv1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Angajat_Shift extends Activity {

    private GridView gridViewWeek;
    List<String> das=new ArrayList<>();
    private TextView textViewNume;
    private TextView textViewSapt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.angajat_shift_week);

        gridViewWeek=(GridView)findViewById(R.id.gridViewWeek) ;
        textViewNume=(TextView)findViewById(R.id.userCurrent) ;
        textViewSapt=(TextView) findViewById(R.id.textViewSapt) ;


        Bundle extras = getIntent().getExtras();
       if (extras != null) {

           textViewNume.setText(extras.getString("nume")+", this week shift");
           System.out.println("Date from intent:"+extras.getString("nume"));
           String Monday=extras.getString("Monday");
           String Tuesday=extras.getString("Tuesday");
           String Wednesday=extras.getString("Wednesday");
           String Thusday=extras.getString("Thusday");
           String Friday=extras.getString("Friday");
           String Saturday=extras.getString("Saturday");
           String Sunday=extras.getString("Sunday");
           das.add(extras.getString("Monday"));
          /// das.set(0,Monday);
           das.add(extras.getString("Tuesday"));
           das.add(extras.getString("Wednesday"));
           das.add(extras.getString("Thusday"));
           das.add(extras.getString("Friday"));
           das.add(extras.getString("Saturday"));
           das.add(extras.getString("Sunday"));
           System.out.println("Date from intent:"+Monday);


        }
       setAdapter();
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

}

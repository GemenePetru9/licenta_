package com.example.pyotr.authv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class Replacement extends Activity {
    private TextView textViewAngSick;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.replacement);
        textViewAngSick=(TextView) findViewById(R.id.angSick) ;

        Bundle e = getIntent().getExtras();
        if (e != null) {


            String numeAngSick = e.getString("numeAngSickTabel");
            System.out.println("Numele ang Sick" + numeAngSick);
            textViewAngSick.setText(numeAngSick+" is sick.Can someone cover this sift?");


        }
        else
        {
            System.out.println("Numele ang Sick From Tabel Bundle este null");
        }

    }
}

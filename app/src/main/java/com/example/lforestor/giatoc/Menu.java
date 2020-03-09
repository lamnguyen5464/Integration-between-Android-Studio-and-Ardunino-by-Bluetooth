package com.example.lforestor.giatoc;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Menu extends Activity {
    Button b1,b2,b3,b4,cl;
    BluetoothAdapter myBluetooth = null;
    Dialog dialog;
    void set_dialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.about);
        cl = (Button) dialog.findViewById(R.id.cl);
    }
    void requestTurnOn(){
        /*
        Intent EnableI = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(EnableI,1);
        */
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }
    void design(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        design();
        set_dialog();
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth==null) Toast.makeText(this,"Your device does not support bluetooth",Toast.LENGTH_SHORT).show();
        //if (!myBluetooth.isEnabled()) requestTurnOn();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTurnOn();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myBluetooth.isEnabled()) Toast.makeText(Menu.this,"Please turn on bluetooth",Toast.LENGTH_SHORT).show();
                else{
                    try{
                        startActivity(new Intent(Menu.this,MainActivity.class));
                    }catch (Exception e){Toast.makeText(Menu.this,"Try again!",Toast.LENGTH_SHORT).show();}
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //ads
//        MobileAds.initialize(this,"ca-app-pub-7104657885074562~9150569357");
//        AdView admview = (AdView)findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        admview.loadAd(adRequest);



    }
}

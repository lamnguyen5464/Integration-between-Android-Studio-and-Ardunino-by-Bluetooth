package com.example.lforestor.giatoc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    Button b1,b2,b3,b4;
    TextView t1,t2;
    ProgressBar pb;
    String o;
    String address = null;
    String arduino = "00:28:13:00:23:BD";
    BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    Boolean light=true;
    Handler handler = new Handler();
    Intent intent;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public class GetBtSocket implements Serializable{
        public BluetoothSocket get(){
            return btSocket;
        }
    }
    void declare() {
        t1 = (TextView) findViewById(R.id.t1);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
    }
    void connectDevice(){
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        address = myBluetooth.getAddress();
            //get id bonded device
        pairedDevices = myBluetooth.getBondedDevices();
        if (pairedDevices.size()>0)
        {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();
                    if (address == arduino) break;

                }
        }
        Toast.makeText(this,"Please wait",Toast.LENGTH_SHORT).show();
        t1.setText("Connecting...");
        if (address.equals(arduino)){
                //Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        b1.setEnabled(true);
                        b2.setEnabled(true);
                        b3.setEnabled(true);
                        b4.setEnabled(true);
                        t1.setText("");
                        BluetoothDevice bluetoothDevice = myBluetooth.getRemoteDevice(address);
                        try{
                            btSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                            btSocket.connect();
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(),"Can not find a device!",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

//                        transferBlueooth = new TransferBlueooth(btSocket);
//                        transferBlueooth.start();

                    }
                },2000);

            }else{
                Toast.makeText(getBaseContext(),"Can not find a device!",Toast.LENGTH_SHORT).show();
                finish();
            }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ads
//        MobileAds.initialize(this,"ca-app-pub-7104657885074562~9150569357");
//        AdView admview = (AdView)findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        admview.loadAd(adRequest);




        declare();
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        connectDevice();
        //event upload
         b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String s1=e1.getText().toString();
//                String s2=e2.getText().toString();
//                if (s1.isEmpty() || s2.isEmpty() ||s1.length()>21 ||s2.length()>21){
//                    Toast.makeText(MainActivity.this,"Please fill in all the blank!",Toast.LENGTH_SHORT).show();
//                }else{
//                    transferBlueooth.write(s1+"#"+s2+"#");
//                    bt.setEnabled(false);
//                    pb.setVisibility(View.VISIBLE);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            bt.setEnabled(true);
//                            pb.setVisibility(View.INVISIBLE);
//                        }
//                    },1500);
//                }
                startActivity(new Intent(MainActivity.this, Counter1.class));

            }
        });
         b2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(MainActivity.this,counter2.class));
             }
         });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,counter3.class));

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,counter4.class));

            }
        });
    }
//    @Override
//    protected void onStart(){
//        super.onStart();
//        connectDevice();
//    }
}

package com.example.lforestor.giatoc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class counter4 extends Activity {
    Switch sw1,sw2;
    TextView min, sec, milisec;
    Button start, reset;
    byte second, minute, milisecond;
    public static boolean process=false;

    TextView min1, sec1, milisec1;
    Button reset1;
    byte second1, minute1, milisecond1;
    public static boolean process1=false;
    Handler handler = new Handler();
    TransferBluetooth transfer;
    Timer timer = new Timer();
    Boolean flag;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (process){
                Log.d("AAA","AEfa "+second);
                milisecond++;
                if (milisecond<10) milisec.setText("0"+milisecond);
                else if (milisecond<100) milisec.setText(""+milisecond);
                else{
                    milisecond=0;
                    milisec.setText("00");
                    second++;
                    if (second<10) sec.setText("0"+second);
                    else if (second<60) sec.setText(""+second);
                    else{
                        minute++;
                        second=0;
                        sec.setText("00");
                        if (minute<10) min.setText("0"+minute);
                        else min.setText(""+minute);
                    }
                }
            }
        }
    };
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            if (process1){
                Log.d("AAA","AEfa "+second1);
                milisecond1++;
                if (milisecond1<10) milisec1.setText("0"+milisecond1);
                else if (milisecond1<100) milisec1.setText(""+milisecond1);
                else{
                    milisecond1=0;
                    milisec1.setText("00");
                    second1++;
                    if (second1<10) sec1.setText("0"+second1);
                    else if (second1<60) sec1.setText(""+second1);
                    else{
                        minute1++;
                        second1=0;
                        sec1.setText("00");
                        if (minute1<10) min1.setText("0"+minute1);
                        else min1.setText(""+minute1);
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter4);
        //
        sw1 = (Switch) findViewById(R.id.switch1);
        sw2 = (Switch) findViewById(R.id.switch2);
        min = (TextView) findViewById(R.id.minute);
        sec = (TextView) findViewById(R.id.second);
        milisec = (TextView) findViewById(R.id.milisec);
        reset = (Button) findViewById(R.id.reset);
        min1 = (TextView) findViewById(R.id.minute_);
        sec1 = (TextView) findViewById(R.id.second_);
        milisec1 = (TextView) findViewById(R.id.milisec_);
        reset1 = (Button) findViewById(R.id.reset_);

        //
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process=false;
                minute=0; second=0; milisecond=0;
                min.setText("00"); sec.setText("00"); milisec.setText("00");
            }
        });

        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process1=false;
                minute1=0; second1=0; milisecond1=0;
                min1.setText("00"); sec1.setText("00"); milisec1.setText("00");
            }
        });
    }

    //
    class TransferBluetooth extends Thread{
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public TransferBluetooth(BluetoothSocket bluetoothSocket) {
            InputStream tmpinput=null;
            OutputStream tmpoutput=null;
            try{
                tmpinput = bluetoothSocket.getInputStream();
                tmpoutput = bluetoothSocket.getOutputStream();
            }catch (IOException e){}
            inputStream = tmpinput;
            outputStream = tmpoutput;
            try {
                inputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void write(String s){
            try {
                outputStream.flush();
                outputStream.write(s.getBytes());
            } catch (IOException e){Toast.makeText(getBaseContext(),"Fail to send!",Toast.LENGTH_SHORT).show();}
        }
        public void run(){
            byte[] buffer = new byte[2048];
            int bytes;
            while (flag){
                try {
                    bytes = inputStream.read(buffer);
                    recieve.obtainMessage(1,bytes,1,buffer).sendToTarget();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Fail to receive",Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }
    String ss="";
    Handler recieve = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if (1 == msg.what){
                byte[] data = (byte[]) msg.obj;
                String s = new String(data,0,msg.arg1);
                ss+=s;
                if (ss.indexOf("\r\n")>0){
                    char ch = ss.charAt(ss.length()-3);
                    switch (ch){
                        case 'b': process=true; sw1.setChecked(true); break;
                        case 'B': process=false; sw1.setChecked(false); break;
                        case 'a': process1=true; sw2.setChecked(true); break;
                        case 'A': process1=false; sw2.setChecked(false); break;
                    }
                    Log.d("runn",ss+" " +" " +process);
                    ss="";
                }
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        process = false;
        flag=true;
        sw1.setClickable(false);
        sw2.setClickable(false);
        transfer = new TransferBluetooth(MainActivity.btSocket);
        transfer.start();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("st1",""+process+" "+second);
                if (process) handler.post(runnable);
                if (process1) handler.post(runnable1);
            }
        },0,10);
        Log.d("stt","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("stt","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("stt","onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("stt","onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        process=false;
        flag=false;
        timer.cancel();
        finish();
        Log.d("stt","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("stt","onDestroy");
    }

}

package com.example.lforestor.giatoc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import com.google.android.gms.common.util.Base64Utils;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Counter1 extends Activity {
    TextView min, sec, milisec;
    Button start, reset;
    byte second, minute, milisecond;
    public static boolean process=false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter1);
        //
        min = (TextView) findViewById(R.id.minute);
        sec = (TextView) findViewById(R.id.second);
        milisec = (TextView) findViewById(R.id.milisec);
        start = (Button) findViewById(R.id.start);
        reset = (Button) findViewById(R.id.reset);

        //
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process=true;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process=false;
                minute=0; second=0; milisecond=0;
                min.setText("00"); sec.setText("00"); milisec.setText("00");
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
            while ( flag){
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
    String ss="",s1="bb"+"\r\n";
    Handler recieve = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if (1 == msg.what){
                byte[] data = (byte[]) msg.obj;
                String s = new String(data,0,msg.arg1);
                ss+=s;
                if (ss.indexOf("\r\n")>0){
                    if (ss.charAt(ss.length()-3)=='B') process=false;
                    Log.d("runn",ss+" counter 1 " +process);
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
        transfer = new TransferBluetooth(MainActivity.btSocket);
        transfer.start();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("st1",""+process+" "+second);
                if (process) handler.post(runnable);
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
        timer.cancel();
        flag=false;
        finish();
        Log.d("stt counter 1","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("stt","onDestroy");
    }

}

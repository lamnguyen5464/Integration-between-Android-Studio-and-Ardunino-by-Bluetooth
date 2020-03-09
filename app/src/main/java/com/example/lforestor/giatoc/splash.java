package com.example.lforestor.giatoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class splash extends Activity {
    TextView textView,textView2;
    Animation animation,animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         animation = AnimationUtils.loadAnimation(this,R.anim.welcome);
         animation2 = AnimationUtils.loadAnimation(this,R.anim.tittle);
        ImageView imageView = (ImageView) findViewById(R.id.im1);
         textView = (TextView) findViewById(R.id.t1);
         textView2 = (TextView) findViewById(R.id.t2);
        imageView.startAnimation(animation);
        textView2.startAnimation(animation);
        textView.startAnimation(animation2);
        Timer timer = new Timer();
        timer.schedule(new after_delay(),4000);
    }


    class after_delay extends TimerTask{
        public void run(){
            Intent i = new Intent(splash.this, Menu.class);
            startActivity(i);
            finish();
        }
    }
    class after_delay1 extends TimerTask{
        public void run(){
        }
    }

}

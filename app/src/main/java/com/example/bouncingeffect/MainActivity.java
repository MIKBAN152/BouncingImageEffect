package com.example.bouncingeffect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //objects
    ImageView im_big_circle, im_small_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im_big_circle = (ImageView) findViewById(R.id.IV_big_circle);
        im_small_circle = (ImageView) findViewById(R.id.IV_small_circle);


        //BouncingEffect be = new BouncingEffect(im_big_circle,im_small_circle,5,bottomDif,periodUp,periodDown,true);
        BouncingEffect be = new BouncingEffect(im_big_circle,im_small_circle,4);

        im_small_circle.setOnClickListener(view -> {
            be.StartBounce();
        });
        im_big_circle.setOnClickListener(view -> {
            be.ResetPosition();
        });

    }
}
package com.example.bouncingeffect;

import android.os.Handler;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/*******************************/
/*****Created by Miguel Bravo***/
/*****2022-06-10***/
/*******************************/

public class BouncingEffect {

    //time aux
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //images
    ImageView iv_static;
    ImageView iv_moving;

    //position
    private float y_origin = -1; //initial moving image y position
    private float y_position = 0; //current y position of moving image
    private float y_top = 0; //max height that the button bounces to

    //bounce effect parameters
    int bounce_times=3; //how many bounces
    int bounce_counter = 0; //counts each bounce
    int bounce_max_distance=500; //max distance between origin and last position
    int going_up_interval = 15; //time between going up steps
    int going_down_interval = 10; //time between going down steps
    private int current_interval = going_down_interval; //interval to use
    boolean gravity_effect = true; //speed will increase after each step when going down and decrease when going up

    private boolean going_up = false; //false when going down
    private float step_increment_percentage = 1.020f; //speed up when going down
    private float step_decrement_percentage = 1.050f; //speed down when going up
    private int step_counter = 0; //each step/movement/transition while going up or down
    private float step_distance_orig = 7f; //pixels to move originally in each interval
    private float step_distance = step_distance_orig; //current step distance


    public BouncingEffect(ImageView iv_static, ImageView iv_moving, int bounce_times, int bounce_max_distance, int going_up_interval, int going_down_interval, boolean gravity_effect) {
        this.iv_static = iv_static;
        this.iv_moving = iv_moving;
        this.bounce_times = bounce_times;
        this.bounce_max_distance = bounce_max_distance;
        this.going_up_interval = going_up_interval;
        this.going_down_interval = going_down_interval;
        this.gravity_effect = gravity_effect;
    }

    public BouncingEffect(ImageView iv_static, ImageView iv_moving, int bounce_times) {
        //use class parameters
        this.iv_static = iv_static;
        this.iv_moving = iv_moving;
        this.bounce_times = bounce_times;
    }

    public void StartBounce(){

        if(y_origin <= -1){
            y_origin = iv_moving.getY();
        }
        y_position = y_origin;
        iv_moving.setY(y_position);

        step_distance = step_distance_orig;
        step_counter = 0;
        bounce_counter = 0;
        current_interval = going_down_interval;
        y_top = 0;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ChangePosition();
                    }
                });
            }
        },0,current_interval);

    }

    private void ChangePosition(){
        //while counts how many times it goes down

        if (going_up){

            y_position -= step_distance;
            if (gravity_effect){
                step_distance = step_distance/step_decrement_percentage;
            }

            if(y_position <= y_origin + y_top){
                current_interval = going_down_interval;
                step_counter = 0;
                going_up = false;
                step_distance = step_distance_orig * step_increment_percentage;
            }
        }else{
            y_position += step_distance;
            if (gravity_effect){
                step_distance = step_distance * step_increment_percentage;
            }
            step_counter += 1;
            if(y_position >= y_origin + bounce_max_distance){
                bounce_counter += 1;
                current_interval = going_up_interval;
                //step_counter = 0;
                going_up = true;
                y_top += (bounce_max_distance - y_top) / 2;
            }
        }
        iv_moving.setY(y_position);
        step_counter += 1;
        if (bounce_counter > bounce_times ) {
            timer.purge();
            timer.cancel();
        }
    }


    public void ResetPosition(){
        if(y_origin> -1){
            iv_moving.setY(y_origin);
        }
    }
}

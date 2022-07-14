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
    ImageView iv_big;
    ImageView iv_small;

    //position mall
    private float y_origin_small = -1; //initial moving image y position
    private float y_position_small = 0; //current y position of moving image
    private float y_max_bounce_height_small = 0; //max height that the button bounces to
    private boolean lock_click = false;
    private float y_origin_big = -1; //initial moving image y position
    private float y_position_big = 0; //current y position of moving image


    //bounce effect parameters
    int bounce_times=3; //how many bounces
    int bounce_counter = 0; //counts each bounce
    float bounce_max_distance=500; //max distance between origin and last position
    int going_up_interval = 15; //time between going up steps
    int going_down_interval = 10; //time between going down steps
    private int current_interval = going_down_interval; //interval to use
    boolean gravity_effect = true; //speed will increase after each step when going down and decrease when going up
    int getting_close_interval = 15;

    private boolean going_up_small = false; //false when going down
    private boolean going_up_big = false; //false when going down
    private float step_increment_percentage = 1.020f; //speed up when going down
    private float step_decrement_percentage = 1.050f; //speed down when going up
    private int step_counter = 0; //each step/movement/transition while going up or down
    private float step_distance_orig = 7f; //pixels to move originally in each interval
    private float step_distance = step_distance_orig; //current step distance


    public BouncingEffect(ImageView iv_big, ImageView iv_small, int bounce_times, int bounce_max_distance, int going_up_interval, int going_down_interval, boolean gravity_effect) {
        this.iv_big = iv_big;
        this.iv_small = iv_small;
        this.bounce_times = bounce_times;
        this.bounce_max_distance = bounce_max_distance;
        this.going_up_interval = going_up_interval;
        this.going_down_interval = going_down_interval;
        this.gravity_effect = gravity_effect;
    }

    public BouncingEffect(ImageView iv_big, ImageView iv_small, int bounce_times) {
        //use class parameters
        this.iv_big = iv_big;
        this.iv_small = iv_small;
        this.bounce_times = bounce_times;
    }

    public void StartBounce(){
        if (!lock_click){
            lock_click = true;
            if(y_origin_small <= -1){
                y_origin_small = iv_small.getY();
            }
            y_position_small = y_origin_small;
            iv_small.setY(y_position_small);

            step_distance = step_distance_orig;
            step_counter = 0;
            bounce_counter = 0;
            current_interval = going_down_interval;
            y_max_bounce_height_small = 0;

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
    }

    private void ChangePosition(){
        //while counts how many times it goes down

        if (going_up_small){

            y_position_small -= step_distance;
            if (gravity_effect){
                step_distance = step_distance/step_decrement_percentage;
            }

            if(y_position_small <= y_origin_small + y_max_bounce_height_small){
                current_interval = going_down_interval;
                step_counter = 0;
                going_up_small = false;
                step_distance = step_distance_orig * step_increment_percentage;
            }
        }else{
            y_position_small += step_distance;
            if (gravity_effect){
                step_distance = step_distance * step_increment_percentage;
            }
            step_counter += 1;
            if(y_position_small >= y_origin_small + bounce_max_distance){
                bounce_counter += 1;
                current_interval = going_up_interval;
                //step_counter = 0;
                going_up_small = true;
                y_max_bounce_height_small += (bounce_max_distance - y_max_bounce_height_small) / 2;
            }
        }
        iv_small.setY(y_position_small);
        step_counter += 1;
        if (bounce_counter > bounce_times ) {
            timer.purge();
            timer.cancel();
            lock_click= false;
        }
    }


    public void ResetPosition(){
        if(y_origin_small > -1 && !lock_click){

            if(y_origin_big <= -1){
                y_origin_big = iv_big.getY();
            }
            y_position_big = y_origin_big;
            going_up_big = false;

            step_distance = step_distance_orig;
            step_counter = 0;
            current_interval = getting_close_interval;

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            PlanetsGetClose();
                        }
                    });
                }
            },0,current_interval);
        }
    }

    private void PlanetsGetClose(){
        //small_planet goes up and big planet goes down and meet in the middle

        y_position_small -= step_distance_orig;

        if (y_position_small <= y_origin_small ) {
            //force last position and finish
            y_position_small = y_origin_small;

            timer.purge();
            timer.cancel();
            lock_click= false;
        }else{
            //move
        }

        if(going_up_big){
            y_position_big -= step_distance_orig;

            if (y_position_big <= y_origin_big  ) {
                //force last position and finish
                y_position_big = y_origin_big;
                //change and go up
                going_up_big = false;
            }else{
                //move
            }
        }else{
            y_position_big += step_distance_orig;

            if (y_position_big >= y_origin_big + bounce_max_distance/2 ) {
                //force last position and finish
                y_position_big = y_origin_big + bounce_max_distance/2;
                //change and go up
                going_up_big = true;
            }else{
                //move
            }
        }

        iv_small.setY(y_position_small);
        iv_big.setY(y_position_big);
    }
}

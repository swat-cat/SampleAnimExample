package com.example.max_ermakov.sampleanimexample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView imageView;
    private Spinner spinner;
    private ImageAdapter adapter;
    private ObjectAnimator scaleDown;
    private AnimatorSet move;
    private ObjectAnimator scale;
    private ObjectAnimator swing;
    private AnimatorSet translateAnimation;
    private Handler handler;
    private String choosedAnim = "None";
    boolean imageChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        imageView = (ImageView)findViewById(R.id.main_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preparePulse();
                scaleDown.start();
                imageChoose = true;

            }
        });
        gridView = (GridView) findViewById(R.id.grid_view);
        adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageChoose){
                    prepareMove(view.getX()+((float)view.getWidth()),view.getY());
                    prepareSet(move,view);
                    translateAnimation.start();
                }else {
                    Toast.makeText(getApplicationContext(),"Choose item, pls. I know that its only one :)",Toast.LENGTH_LONG).show();
                }
            }
        });
        spinner = (Spinner)findViewById(R.id.anim_spinner);
        String[]strings = {"None","Swing","Scale"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,strings));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosedAnim = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void prepareSet(AnimatorSet move, final View itemView){
        translateAnimation = new AnimatorSet();
        translateAnimation.play(prepareScale(false)).before(move);
        translateAnimation.play(move);
        switch (choosedAnim){
            case "Swing":
                translateAnimation.play(prepareSwing()).after(move);
                break;
            case "Scale":
                translateAnimation.play(prepareScale(true)).after(move);
                break;
            default:break;
        }
        translateAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                scaleDown.cancel();
                ((ImageView)itemView).setColorFilter(getResources().getColor(R.color.black_050));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ImageView)itemView).setColorFilter(null);
                imageView.setVisibility(View.GONE);
                ((ImageView)itemView).setImageDrawable(getResources().getDrawable(R.mipmap.images_c));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                },1000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private ObjectAnimator prepareScale(boolean reverse) {
        scale = ObjectAnimator.ofPropertyValuesHolder(imageView,
                PropertyValuesHolder.ofFloat("scaleX", reverse?1f:1.2f),
                PropertyValuesHolder.ofFloat("scaleY", reverse?1f:1.2f));
        scaleDown.setDuration(150);
        return scale;
    }

    private void prepareMove(float toX, float toY){
        move = new AnimatorSet();
        ObjectAnimator y = ObjectAnimator.ofFloat(imageView,
                "y",imageView.getY(), toY);

        ObjectAnimator x = ObjectAnimator.ofFloat(imageView,
                "x", imageView.getX(), toX);
        move.playTogether(x,y);
        move.setInterpolator(new DecelerateInterpolator());
        move.setDuration(600);
    }

    private void preparePulse() {
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
    }

    private ObjectAnimator prepareSwing(){
        swing =  ObjectAnimator.ofFloat(imageView,"rotation",0, 10, -10);
        swing.setDuration(200);

        swing.setRepeatCount(2);
        return swing;
    }
}

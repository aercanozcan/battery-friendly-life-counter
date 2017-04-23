package com.apptopus.bflifecounter.view;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apptopus.bflifecounter.R;

import java.util.Random;


/**
 * Created by ercanozcan on 11/03/17.
 */
public class PlayerView extends FrameLayout implements View.OnClickListener {

    private PlayerViewListener listener;
    private View btnPositive;
    private View btnNegative;
    private TextView lblPlayer;
    private Random random = new Random();
    private static final int RANDOM_RANGE = 25;// +10 minimum range
    private EndReaction endReaction = new EndReaction();
    private static final int ANIMATION_LIMIT = 4;// to avoid oversized text
    private int animationCount = 0;

    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.layout_player_view, this);
        btnPositive = findViewById(R.id.btnPositive);
        btnNegative = findViewById(R.id.btnNegative);
        lblPlayer = (TextView) findViewById(R.id.lblPlayer);
        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
    }

    private void animateNegativeClicked() {
        //hit like animation
        final int direction = random.nextBoolean() ? 1 : -1;
        final int rotateBy = 10 + random.nextInt(RANDOM_RANGE) * direction;

        lblPlayer.animate().cancel();
        lblPlayer.animate()
                .rotationX(rotateBy / 2)
                .rotationYBy(random.nextInt(RANDOM_RANGE) * direction)
                .withEndAction(endReaction)
                .setDuration(120).start();
    }


    private void animatePositiveClicked() {
        //looks like hearth beating if called repeatedly increases the size.
        lblPlayer.animate().cancel();
        if (animationCount < ANIMATION_LIMIT) {
            lblPlayer.animate()
                    .scaleXBy(0.02f)
                    .scaleYBy(0.02f)
                    .setDuration(100)
                    .withEndAction(endReaction)
                    .setInterpolator(new FastOutLinearInInterpolator())
                    .start();
            animationCount++;
        } else {
            lblPlayer.postDelayed(endReaction, 150);
            animationCount = 0;
        }

    }

    private void resetAnimations() {
        lblPlayer.animate().cancel();
        lblPlayer.animate()
                .rotationX(0)
                .rotationY(0)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400).start();
    }

    public void setPlayerViewListener(PlayerViewListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        lblPlayer.setText(text);
    }

    public void setText(int number) {
        setText(String.valueOf(number));
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v.equals(btnPositive)) {
                listener.onPositiveClicked(this);
                animatePositiveClicked();
            } else {
                listener.onNegativeClicked(this);
                animateNegativeClicked();
            }
        }
    }


    private class EndReaction implements Runnable {
        @Override
        public void run() {
            resetAnimations();
        }
    }
}
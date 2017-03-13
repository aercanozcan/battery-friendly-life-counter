package com.apptopus.bflifecounter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apptopus.bflifecounter.R;


/**
 * Created by ercanozcan on 11/03/17.
 */
public class PlayerView extends FrameLayout implements View.OnClickListener {

    private PlayerViewListener listener;
    private View btnPositive;
    private View btnNegative;
    private TextView lblPlayer;
    //TODO add animations to lblPlayer
    //TODO change the ugly buttons to material design

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
            } else {
                listener.onNegativeClicked(this);
            }
        }
    }
}

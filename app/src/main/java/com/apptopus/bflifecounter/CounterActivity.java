package com.apptopus.bflifecounter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.apptopus.bflifecounter.model.Player;
import com.apptopus.bflifecounter.view.PlayerView;
import com.apptopus.bflifecounter.view.PlayerViewListener;


public class CounterActivity extends AppCompatActivity implements PlayerViewListener {
    int REQUEST_CODE = 1;
    PlayerView playerView1;
    PlayerView playerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        checkDrawOverlayPermission();
        initViews();
        initPlayers();//TODO initialise player1 & player2 from db or sharedprefferences
    }


    private void initViews() {
        playerView1 = (PlayerView) findViewById(R.id.player1);
        playerView2 = (PlayerView) findViewById(R.id.player2);
        playerView1.setPlayerViewListener(this);
        playerView2.setPlayerViewListener(this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    private void initPlayers() {
        playerView1.setTag(new Player());
        playerView2.setTag(new Player());//lazy & easy
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {//MarshMallow
            /** check if we already  have permission to draw over other apps */
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                showOverLockScreen();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {//MarshMallow
            /** check if received result code
             is equal our requested code for draw permission  */
            if (requestCode == REQUEST_CODE) {
            /* if so check once again if we have permission */
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                    showOverLockScreen();
                } else {
                    // Y U NO LET ME SAVE YOUR BATTERY?
                }
            }
        }
    }


    //this is the part where we really save battery life ;)
    private void showOverLockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    @Override
    public void onPositiveClicked(PlayerView playerView) {
        Player player = (Player) playerView.getTag();
        player.setLife(player.getLife() + 1);
        playerView.setText(player.getLife());
    }

    @Override
    public void onNegativeClicked(PlayerView playerView) {
        Player player = (Player) playerView.getTag();
        player.setLife(player.getLife() - 1);
        playerView.setText(player.getLife());
    }
}

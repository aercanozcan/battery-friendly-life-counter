package com.apptopus.bflifecounter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class CounterActivity extends AppCompatActivity {
    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        checkDrawOverlayPermission();
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
                }
            }
        }
    }


    private void showOverLockScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }
}

package com.apptopus.bflifecounter;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.apptopus.bflifecounter.game.Game;
import com.apptopus.bflifecounter.model.Player;
import com.apptopus.bflifecounter.view.PlayerView;
import com.apptopus.bflifecounter.view.PlayerViewListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;


public class CounterActivity extends AppCompatActivity implements PlayerViewListener {
    private int REQUEST_CODE = 1;
    private PlayerView playerView1;
    private PlayerView playerView2;
    private CheckBox dontShowAgain;
    private CircleMenu circleMenu;

    private Game game = Game.getInstance();

    private static final int MENU_ITEM_NONE = -1;
    private static final int MENU_ITEM_REFRESH = 0;
    private int currentMenuItem = MENU_ITEM_NONE;


    public static final String PREFS_NAME = "BFLCPrefsFile";

    //TODO add link to https://icons8.com/
    //TODO add energy counter buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        checkDrawOverlayPermission();
        initViews();
        initPlayers();
        showLockScreenPermissionDialog();
    }


    private void initViews() {
        Resources resources = getResources();
        playerView1 = (PlayerView) findViewById(R.id.player1);
        playerView2 = (PlayerView) findViewById(R.id.player2);
        playerView1.setPlayerViewListener(this);
        playerView2.setPlayerViewListener(this);
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .addSubMenu(resources.getColor(R.color.plain), R.drawable.refresh)//refresh //TODO FIND BLACK ICONS FOR CONTRAST!
                .addSubMenu(resources.getColor(R.color.island), R.mipmap.ic_launcher)
                .addSubMenu(resources.getColor(R.color.swamp), R.mipmap.ic_launcher)
                .addSubMenu(resources.getColor(R.color.mountain), R.mipmap.ic_launcher)
                .addSubMenu(resources.getColor(R.color.forest), R.mipmap.ic_launcher)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {
                        currentMenuItem = index;
                        //to perform immediately
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {
                switch (currentMenuItem) {//to perform after menu is closed
                    case MENU_ITEM_REFRESH: {
                        game.refreshGame();
                        initPlayers();
                    }
                }
                currentMenuItem = MENU_ITEM_NONE;
            }

        });
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    private void initPlayers() {//lazy & easy
        playerView1.setTag(game.playerOne());
        playerView2.setTag(game.playerTwo());
        playerView1.setText(game.playerOne().getLife());
        playerView2.setText(game.playerTwo().getLife());
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    @Override
    public void onPositiveClicked(PlayerView playerView) {
        Player player = (Player) playerView.getTag();
        game.increasePlayerLife(player);
        playerView.setText(player.getLife());
    }

    @Override
    public void onNegativeClicked(PlayerView playerView) {
        Player player = (Player) playerView.getTag();
        game.decreasePlayerLife(player);
        playerView.setText(player.getLife());
    }

    private void showLockScreenPermissionDialog() {//TODO clean up copy paste mess
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        adb.setView(eulaLayout);
        adb.setTitle(R.string.lock_screen_dialog_title);
        adb.setMessage(R.string.lock_screen_dialog_message);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked())
                    checkBoxResult = "checked";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("skipMessage", checkBoxResult);
                // Commit the edits!
                editor.commit();
                checkDrawOverlayPermission();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked())
                    checkBoxResult = "checked";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("skipMessage", checkBoxResult);
                // Commit the edits!
                editor.commit();
            }
        });
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("skipMessage", "NOT checked");
        if (!skipMessage.equals("checked"))
            adb.show();
    }

}

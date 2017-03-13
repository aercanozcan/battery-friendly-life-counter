package com.apptopus.bflifecounter;

import android.app.Application;
import android.util.Log;

import com.apptopus.bflifecounter.game.Game;
import com.apptopus.bflifecounter.model.Player;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

/**
 * Created by ercanozcan on 12/03/17.
 */
public class CounterApplication extends Application {
    //TODO implement Fyber adds
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(Player.class);
        config.setClasses(classes);
        RushCore.initialize(config);
    }
}

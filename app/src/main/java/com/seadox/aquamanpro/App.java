package com.seadox.aquamanpro;

import android.app.Application;

import com.seadox.aquamanpro.Utilities.SignalGenerator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SignalGenerator.init(this);
    }
}

package com.codegemz.elfi.coreapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codegemz.elfi.coreapp.module.wifi_for_indoor.XMPPModule;

import dagger.ObjectGraph;

/**
 * Created by adrobnych on 9/22/15.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph og = ((BrainApp) getApplication()).getObjectGraph();

        og.inject(this);

    }
}

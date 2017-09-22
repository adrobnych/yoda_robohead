package com.codegemz.elfi.coreapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;
import com.codegemz.elfi.coreapp.module.wifi_for_indoor.XMPPModule;

import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by adrobnych on 5/23/15.
 */
public class BrainApp extends Application{
    public BrainActivity getBrainActivity() {
        return brainActivity;
    }

    public void setBrainActivity(BrainActivity brainActivity) {
        this.brainActivity = brainActivity;
    }

    BrainActivity brainActivity;

    public synchronized Connector getConnector() {
        return bodyConnector;
    }

    public synchronized void setConnector(Connector connector) {
        this.bodyConnector = connector;
    }

    private Connector bodyConnector = null;

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(new XMPPModule());
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    public List<String> getRepeatProgramMap() {
        return repeatProgramMap;
    }

    public void setRepeatProgramMap(List<String> repeatProgramMap) {
        this.repeatProgramMap = repeatProgramMap;
    }

    private List<String> repeatProgramMap;

}

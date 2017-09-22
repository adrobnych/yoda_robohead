package com.codegemz.elfi.coreapp.api.event_generator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//send initial Alarm intent from onCreate of BrainActivity
public class PeriodicAlarmReceiver extends BroadcastReceiver {
    public PeriodicAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        checkGoogleCalendarEventsForOwner();
        checkIncomingEmailsForOwner();
        checkWeatherConditions();
        checkNews();
        checkInterestFeeds();
        checkGoogleCalendarEventsForELFi();
        checkELFiBatteries();
        checkNewFeaturesFromRoboMarket();
        checkRoboChat(); // robot to robot messages

        fairNextAlarm();
    }

    private void fairNextAlarm() {

    }

    private void checkRoboChat() {

    }

    private void checkNewFeaturesFromRoboMarket() {

    }

    private void checkELFiBatteries() {

    }

    private void checkGoogleCalendarEventsForELFi() {

    }

    private void checkInterestFeeds() {

    }

    private void checkNews() {

    }

    private void checkWeatherConditions() {

    }

    private void checkIncomingEmailsForOwner() {

    }

    private void checkGoogleCalendarEventsForOwner() {

    }
}

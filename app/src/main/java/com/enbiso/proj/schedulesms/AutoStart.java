package com.enbiso.proj.schedulesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver {

    private AlarmReceiver alarmReceiver;

    public AutoStart() {
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmReceiver.setAlarm(context);
        }
    }
}

package com.enbiso.proj.schedulesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;

public class AutoStart extends BroadcastReceiver {

    private AlarmReceiver alarmReceiver;

    public AutoStart() {
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //setup db
            DatabaseHelper.init(context);
            DatabaseHelper helper = DatabaseHelper.getInstance();
            helper.addHelper(new MessageHelper(context));
            helper.addHelper(new ScheduleHelper(context));

            alarmReceiver.setAlarm(context);
        }
    }
}

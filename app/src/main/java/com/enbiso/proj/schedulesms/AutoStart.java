package com.enbiso.proj.schedulesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;

public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainService.class);
        context.startService(myIntent);
    }
}

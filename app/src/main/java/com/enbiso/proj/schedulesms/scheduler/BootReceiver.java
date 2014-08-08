package com.enbiso.proj.schedulesms.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            Intent myIntent = new Intent(context, SchedulerService.class);
            context.startService(myIntent);
    }
}

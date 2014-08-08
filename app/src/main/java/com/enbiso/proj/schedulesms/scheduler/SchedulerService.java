package com.enbiso.proj.schedulesms.scheduler;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;

public class SchedulerService extends Service {

    private AlarmReceiver alarmReceiver;
    private long interval = 1000 * 10 * 1; //1 min in milli seconds
    private MessageHelper messageHelper;


    public SchedulerService() {
        DatabaseHelper.init(this);
        this.messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);
        this.alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmReceiver.setAlarm(this, interval);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}



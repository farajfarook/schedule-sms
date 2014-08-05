package com.enbiso.proj.schedulesms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;

import java.util.Calendar;

public class MainService extends Service {

    private AlarmReceiver alarmReceiver;

    public MainService() {
        alarmReceiver = new AlarmReceiver();
        //setup db
        DatabaseHelper.init(this);
        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.addHelper(new MessageHelper(this));
        helper.addHelper(new ScheduleHelper(this));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmReceiver.setAlarm(MainService.this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

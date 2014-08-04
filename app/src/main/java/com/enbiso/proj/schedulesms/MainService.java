package com.enbiso.proj.schedulesms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class MainService extends Service {

    private AlarmReceiver alarmReceiver;

    public MainService() {
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show();
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

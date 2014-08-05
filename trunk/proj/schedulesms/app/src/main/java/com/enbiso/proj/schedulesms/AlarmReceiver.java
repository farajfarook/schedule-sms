package com.enbiso.proj.schedulesms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;

import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private boolean alarmSet = false;
    private int notificationId = 001;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        MessageHelper messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);

        messageHelper.initMessages(Calendar.getInstance());
        messageHelper.removeHistoryMessages();
        List<Message> messages = messageHelper.getMessagesFromQueue();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            sendMessage(context, message);
            messageHelper.markAsSent(message);
            if(settings.getBoolean("notifications_enable", true)){
                showNotification("Schedule SMS sent to " + message.getReceiverString(10), message.getMessage(20) , context);
            }
        }
        wl.release();
    }

    private void sendMessage(Context context, Message message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(message.getReceiverString(), null, message.getMessage(), null, null);

        Uri uri = Uri.parse("content://sms/sent/");
        ContentValues values = new ContentValues();
        values.put("address", message.getReceiverString());
        values.put("body", message.getMessage());
        context.getContentResolver().insert(uri, values);
    }

    private void showNotification(String title, String text, Context context) {
        //We get a reference to the NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId++ , mBuilder.build());
    }

    public void setAlarm(Context context){
        if(!alarmSet) {
            //setup alarm
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
            alarmSet = true;
        }
    }

    public void cancelAlarm(Context context){
        if(alarmSet) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
            alarmSet = false;
        }
    }
}

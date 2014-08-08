package com.enbiso.proj.schedulesms.scheduler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by farflk on 8/6/2014.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String SMS_SENT = "com.enbiso.proj.schedulesms.SMS_SENT";
    public static final String SMS_DELIVERED = "com.enbiso.proj.schedulesms.SMS_DELIVERED";

    public AlarmReceiver() { }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        DatabaseHelper.init(context);
        MessageHelper messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);

        messageHelper.initMessages(Calendar.getInstance());
        messageHelper.removeHistoryMessages();
        List<Message> messages = messageHelper.getMessagesFromQueue();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            sendMessage(context, message);
            messageHelper.markAsSending(message);
        }

        wl.release();
    }

    public void sendMessage(Context context, final Message message){
        //Sent
        Intent sentPiIntent = new Intent(SMS_SENT);
        sentPiIntent.putExtra("message_id", message.get_id());
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentPiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //delivered
        Intent deliverPiIntent = new Intent(SMS_DELIVERED);
        deliverPiIntent.putExtra("message_id", message.get_id());
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliverPiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //send sms
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(message.getReceiverString(), null, message.getMessage(), sentPI, deliveredPI);
        //update the sms history
        Uri uri = Uri.parse("content://sms/sent/");
        ContentValues values = new ContentValues();
        values.put("address", message.getReceiverString());
        values.put("body", message.getMessage());
        context.getContentResolver().insert(uri, values);
    }

    public void setAlarm(SchedulerService schedulerService, long interval){
        AlarmManager alarmManager = (AlarmManager) schedulerService.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(schedulerService, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(schedulerService, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), interval, alarmIntent);
    }

    public void cancelAlarm(SchedulerService schedulerService){
        Intent intent = new Intent(schedulerService, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(schedulerService, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) schedulerService.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
    }
}

package com.enbiso.proj.schedulesms.scheduler;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;

public class MessageReceiver extends BroadcastReceiver {

    private int notificationId = 001;

    @Override
    public void onReceive(Context context, Intent intent) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        DatabaseHelper.init(context);
        MessageHelper messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);
        final Message message = (Message) messageHelper.getBy("_id", intent.getExtras().getString("message_id"));
        if(message == null){
            return;
        }
        if(intent.getAction().equals(AlarmReceiver.SMS_DELIVERED)){
            //SMS Delivered
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    messageHelper.markAsDelivered(message);
                    if (settings.getBoolean("notifications_delivery_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_delivered), message.getReceiverNameString(35), NOTIFY_TYPE_OK);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    messageHelper.markAsFailed(message, context.getString(R.string.message_not_delivered));
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_not_delivered), message.getReceiverNameString(35), NOTIFY_TYPE_FAILED);
                    }
                    break;
            }

        }else if(intent.getAction().equals(AlarmReceiver.SMS_SENT)){
            //SMS Sent
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    messageHelper.markAsSent(message);
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, message.getReceiverNameString(25), message.getMessage(55), NOTIFY_TYPE_OK);
                    }
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    messageHelper.markAsFailed(message, context.getString(R.string.message_generic_failure));
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_generic_failure), message.getReceiverNameString(35), NOTIFY_TYPE_FAILED);
                    }
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    messageHelper.markAsFailed(message, context.getString(R.string.message_no_service));
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_no_service), message.getReceiverNameString(35), NOTIFY_TYPE_FAILED);
                    }
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    messageHelper.markAsFailed(message, context.getString(R.string.message_null_pdu));
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_null_pdu), message.getReceiverNameString(35), NOTIFY_TYPE_FAILED);
                    }
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    messageHelper.markAsFailed(message, context.getString(R.string.message_radio_off));
                    if (settings.getBoolean("notifications_enable", true)) {
                        showNotification(context, message, context.getString(R.string.message_radio_off), message.getReceiverNameString(35), NOTIFY_TYPE_FAILED);
                    }
                    break;
            }
        }
    }

    private static final int NOTIFY_TYPE_OK = 0;
    private static final int NOTIFY_TYPE_FAILED = 1;

    private void showNotification(Context context, Message message, String title, String text, int type) {
        //We get a reference to the NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon((type == NOTIFY_TYPE_OK)? R.drawable.notification_check: R.drawable.notification_cross)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentText(text);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("message_id", message.get_id());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId++ , mBuilder.build());

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        if((settings.getBoolean("notifications_new_message_vibrate", true))) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }
}

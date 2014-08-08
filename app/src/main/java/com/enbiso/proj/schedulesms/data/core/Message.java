package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by farflk on 7/24/2014.
 */
public class Message extends AbstractModel {
    private String scheduleId;
    private Calendar executed;
    private List<ContactItem> receivers = new ArrayList<ContactItem>();
    private String message;
    private String error;

    private Schedule schedule;

    public Message() {
        receivers = new ArrayList<ContactItem>();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("schedule_id", scheduleId);
        if(executed != null) {
            contentValues.put("executed", dateTimeFormat.format(executed.getTime()));
        }
        contentValues.put("receivers", encodeReceiverList(receivers));
        contentValues.put("message", message);
        contentValues.put("error", error);
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        scheduleId = fetchData(data, "schedule_id");
        executed = fetchDataCalender(data, "executed");
        receivers = decodeReceiverString(fetchData(data, "receivers"));
        message = fetchData(data, "message");
        error = fetchData(data, "error");
    }

    private static String encodeReceiverList(List<ContactItem> receivers){
        StringBuilder stringBuilder = new StringBuilder();
        if(receivers != null) {
            for (int i = 0; i < receivers.size(); i++) {
                stringBuilder.append(receivers.get(i).getPhone());
                if(i < receivers.size() - 1){
                    stringBuilder.append(";");
                }
            }
        }
        return stringBuilder.toString();
    }

    private static List<ContactItem> decodeReceiverString(String receiverStr){
        List<ContactItem> receivers = new ArrayList<ContactItem>();
        if(receiverStr == null){
            receiverStr = "";
        }
        List<String> numbers = Arrays.asList(receiverStr.split(";"));
        for (int i = 0; i < numbers.size(); i++) {
            ContactItem contactItem = (ContactItem) DatabaseHelper.getInstance().getHelper(ContactItemHelper.class).getBy("_id", numbers.get(i));
            if(contactItem == null) {
                contactItem = new ContactItem(numbers.get(i));
            }
            receivers.add(contactItem);
        }
        return receivers;
    }

    public String getReceiverString(int limit){
        String receiverStr  = getReceiverString();
        if(receiverStr.length() <= limit){
            return  receiverStr;
        }else {
            return receiverStr.substring(0, limit) + "...";
        }
    }

    public String getReceiverString(){
        String receiverStr  = encodeReceiverList(receivers);
        return receiverStr;
    }

    public String getReceiverNameString(int limit){
        String receiverStr  = getReceiverNameString();
        if(receiverStr.length() <= limit){
            return  receiverStr;
        }else {
            return receiverStr.substring(0, limit) + "...";
        }
    }

    public String getReceiverNameString(){
        StringBuilder stringBuilder = new StringBuilder();
        if(receivers != null) {
            for (int i = 0; i < receivers.size(); i++) {
                String value = receivers.get(i).getName();
                if(value == null){
                    value = receivers.get(i).getPhone();
                }
                stringBuilder.append(value);
                if(i < receivers.size() - 1){
                    stringBuilder.append(";");
                }
            }
        }
        return stringBuilder.toString();
    }

    public String getMessage(int limit){
        if(message.length() <= limit){
            return  message;
        }else {
            return message.substring(0, limit) + "...";
        }
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Calendar getExecuted() {
        return executed;
    }

    public void setExecuted(Calendar executed) {
        this.executed = executed;
    }

    public List<ContactItem> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<ContactItem> receivers) {
        this.receivers = receivers;
    }

    public void addReceiver(ContactItem receiver){
        this.receivers.add(receiver);
    }

    public boolean removeReceiver(ContactItem receiver){
        return this.receivers.remove(receiver);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

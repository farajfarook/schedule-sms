package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.form.wizard.ContactItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        scheduleId = fetchData(data, "schedule_id");
        executed = fetchDataCalender(data, "executed");
        receivers = decodeReceiverString(fetchData(data, "receivers"));
        message = fetchData(data, "message");
    }

    private static String encodeReceiverList(List<ContactItem> receivers){
        StringBuilder stringBuilder = new StringBuilder();
        if(receivers != null) {
            for (int i = 0; i < receivers.size(); i++) {
                stringBuilder.append(receivers.get(i).getNumber()).append(";");
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
            receivers.add(new ContactItem(numbers.get(i)));
        }
        return receivers;
    }

    public String getReceiverString(int limit){
        String receiverStr  = encodeReceiverList(receivers);
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
}

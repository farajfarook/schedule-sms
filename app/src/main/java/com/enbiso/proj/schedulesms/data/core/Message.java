package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by farflk on 7/24/2014.
 */
public class Message extends AbstractModel {
    private String scheduleId;
    private int scheduled;
    private int executed;
    private List<String> receivers;
    private String message;

    private Schedule schedule;

    public Message() {
        receivers = new ArrayList<String>();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("schedule_id", scheduleId);
        contentValues.put("scheduled", scheduled);
        contentValues.put("executed", executed);
        contentValues.put("receivers", encodeReceiverList(receivers));
        contentValues.put("message", message);
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        scheduleId = fetchData(data, "schedule_id");
        scheduled = fetchDataInteger(data, "scheduled");
        executed = fetchDataInteger(data, "executed");
        receivers = decodeReceiverString(fetchData(data, "receivers"));
        message = fetchData(data, "message");
    }

    private static String encodeReceiverList(List<String> receivers){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < receivers.size(); i++) {
            stringBuilder.append(receivers.get(i)).append(";");
        }
        return stringBuilder.toString();
    }

    private static List<String> decodeReceiverString(String receiverStr){
        List<String> receivers = new ArrayList<String>();
        receivers.addAll(Arrays.asList(receiverStr.split(";")));
        return receivers;
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

    public int getScheduled() {
        return scheduled;
    }

    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public void addReceiver(String receiver){
        this.receivers.add(receiver);
    }

    public boolean removeReceiver(String receiver){
        return this.receivers.remove(receiver);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

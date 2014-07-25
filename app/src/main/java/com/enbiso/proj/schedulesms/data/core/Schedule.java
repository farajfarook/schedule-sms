package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by farflk on 7/24/2014.
 */
public class Schedule extends AbstractModel{
    private String type;
    private String description;
    private String scheduleData;
    private List<String> receivers;
    private String message;

    private List<Message> messages;

    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("type", type);
        contentValues.put("description", description);
        contentValues.put("schedule_data", scheduleData);
        contentValues.put("receivers", encodeReceiverList(receivers));
        contentValues.put("message", message);
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        description = fetchData(data, "description");
        scheduleData = fetchData(data, "schedule_data");
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduleData() {
        return scheduleData;
    }

    public void setScheduleData(String scheduleData) {
        this.scheduleData = scheduleData;
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

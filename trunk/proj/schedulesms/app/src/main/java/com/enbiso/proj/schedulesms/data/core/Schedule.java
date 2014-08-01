package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.form.wizard.ContactItem;

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
    private String repeatEnable;
    private String scheduleDate;
    private String repeatValidTillDate;
    private String repeatType;
    private String repeatValue;
    private List<ContactItem> receivers;
    private String message;

    private List<Message> messages;

    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("description", description);
        contentValues.put("repeat_enable", repeatEnable);
        contentValues.put("schedule_date", scheduleDate);
        contentValues.put("repeat_valid_till_date", repeatValidTillDate);
        contentValues.put("repeat_type", repeatType);
        contentValues.put("repeat_value", repeatValue);
        contentValues.put("receivers", encodeReceiverList(receivers));
        contentValues.put("message", message);
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        description = fetchData(data, "description");
        repeatEnable = fetchData(data, "repeat_enable");
        scheduleDate = fetchData(data, "schedule_date");
        repeatValidTillDate = fetchData(data, "repeat_valid_till_date");
        repeatType = fetchData(data, "repeat_type");
        repeatValue = fetchData(data, "repeat_value");
        receivers = decodeReceiverString(fetchData(data, "receivers"));
        message = fetchData(data, "message");
    }

    private static String encodeReceiverList(List<ContactItem> receivers){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < receivers.size(); i++) {
            stringBuilder.append(receivers.get(i).getNumber()).append(";");
        }
        return stringBuilder.toString();
    }

    private static List<ContactItem> decodeReceiverString(String receiverStr){
        List<ContactItem> receivers = new ArrayList<ContactItem>();
        List<String> numbers = Arrays.asList(receiverStr.split(";"));
        for (int i = 0; i < numbers.size(); i++) {
            receivers.add(new ContactItem(numbers.get(i)));
        }
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

    public String getRepeatEnable() {
        return repeatEnable;
    }

    public void setRepeatEnable(String repeatEnable) {
        this.repeatEnable = repeatEnable;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getRepeatValidTillDate() {
        return repeatValidTillDate;
    }

    public void setRepeatValidTillDate(String repeatValidTillDate) {
        this.repeatValidTillDate = repeatValidTillDate;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public String getRepeatValue() {
        return repeatValue;
    }

    public void setRepeatValue(String repeatValue) {
        this.repeatValue = repeatValue;
    }

    public List<ContactItem> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<ContactItem> receivers) {
        this.receivers = receivers;
    }

    public boolean removeReceiver(String receiver){
        return this.receivers.remove(receiver);
    }

    public String getReceiverString(int limit){
        String receiverStr  = encodeReceiverList(receivers);
        if(receiverStr.length() <= limit){
            return  receiverStr;
        }else {
            return receiverStr.substring(0, limit);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(int limit){
        if(message.length() <= limit){
            return  message;
        }else {
            return message.substring(0, limit);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

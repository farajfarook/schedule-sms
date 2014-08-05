package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;

import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.form.wizard.ContactItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by farflk on 7/24/2014.
 */
public class Schedule extends AbstractModel{
    private String description;
    private String repeatEnable = String.valueOf(false);
    private Calendar scheduleDate = Calendar.getInstance();
    private Calendar nextExecute = Calendar.getInstance();
    private Calendar repeatValidTillDate = Calendar.getInstance();
    private String repeatType;
    private String repeatValue = "1";
    private List<ContactItem> receivers = new ArrayList<ContactItem>();
    private String message;

    private List<Message> messages;

    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("description", description);
        contentValues.put("repeat_enable", repeatEnable);
        contentValues.put("schedule_date", dateTimeFormat.format(scheduleDate.getTime()));
        contentValues.put("next_execute", dateTimeFormat.format(nextExecute.getTime()));
        contentValues.put("repeat_valid_till_date", dateTimeFormat.format(repeatValidTillDate.getTime()));
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
        repeatEnable = fetchData(data, "repeat_enable", String.valueOf(false));
        scheduleDate = fetchDataCalender(data, "schedule_date");
        nextExecute = fetchDataCalender(data, "next_execute");
        repeatValidTillDate = fetchDataCalender(data, "repeat_valid_till_date");
        repeatType = fetchData(data, "repeat_type");
        repeatValue = fetchData(data, "repeat_value", "1");
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

    public Calendar getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Calendar scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Calendar getRepeatValidTillDate() {
        return repeatValidTillDate;
    }

    public void setRepeatValidTillDate(Calendar repeatValidTillDate) {
        this.repeatValidTillDate = repeatValidTillDate;
    }

    public int getRepeatTypeSelected(String[] types){
        for (int i = 0; i < types.length; i++) {
            if(types[i].equalsIgnoreCase(repeatType)){
                return i;
            }
        }
        return 0;
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
            return receiverStr.substring(0, limit) + "...";
        }
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(int limit){
        if(message.length() <= limit){
            return  message;
        }else {
            return message.substring(0, limit) + "...";
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean addReceiver(ContactItem contactItem) {
        if(!receivers.contains(contactItem)) {
            return receivers.add(contactItem);
        }else{
            return false;
        }
    }

    public boolean removeReceiver(ContactItem contactItem) {
        return receivers.remove(contactItem);
    }

    public Calendar getNextExecute() {
        return nextExecute;
    }

    public void setNextExecute(Calendar nextExecute) {
        this.nextExecute = nextExecute;
    }
}

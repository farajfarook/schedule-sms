package com.enbiso.proj.schedulesms.data.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.enbiso.proj.schedulesms.data.AbstractHelper;
import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.SearchEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by farflk on 7/24/2014.
 */
public class MessageHelper extends AbstractHelper{

    public MessageHelper(Context context) {
        super(context);
        this.tableName = "core_tbl_message";

        this.columns.add("schedule_id VARCHAR(100)");
        this.columns.add("executed TEXT");
        this.columns.add("receivers TEXT");
        this.columns.add("message TEXT");
    }

    @Override
    protected AbstractModel getModelInstance() {
        return new Message();
    }

    @Override
    public AbstractModel populateRelations(AbstractModel abstractModel) {
        Message message = (Message)super.populateRelations(abstractModel);
        Schedule schedule = (Schedule)DatabaseHelper.getInstance().getHelper(MessageHelper.class).getBy("_id", message.getScheduleId());
        message.setSchedule(schedule);
        return message;
    }

    public void initMessages(Calendar calendar){
        List<Schedule> schedules = (List<Schedule>)(List<?>)DatabaseHelper.getInstance().getHelper(ScheduleHelper.class).findBy("_state", "active");
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            if(Boolean.parseBoolean(schedule.getRepeatEnable())){
                if(schedule.getRepeatValidTillDate().getTimeInMillis() >= calendar.getTimeInMillis()){
                    //valid check instance
                    int value = Integer.parseInt(schedule.getRepeatValue());
                    Calendar nextExecute = schedule.getNextExecute();
                    if(nextExecute.getTimeInMillis() <= calendar.getTimeInMillis()) {
                        createMessageFromSchedule(schedule);
                        if (schedule.getRepeatType().equalsIgnoreCase("Minutes")) {
                            nextExecute.add(Calendar.MINUTE, value);
                        } else if (schedule.getRepeatType().equalsIgnoreCase("Hours")) {
                            nextExecute.add(Calendar.HOUR, value);
                        } else if (schedule.getRepeatType().equalsIgnoreCase("Days")) {
                            nextExecute.add(Calendar.DATE, value);
                        } else if (schedule.getRepeatType().equalsIgnoreCase("Weeks")) {
                            nextExecute.add(Calendar.DATE, value * 7);
                        } else if (schedule.getRepeatType().equalsIgnoreCase("Months")) {
                            nextExecute.add(Calendar.MONTH, value);
                        } else if (schedule.getRepeatType().equalsIgnoreCase("Years")) {
                            nextExecute.add(Calendar.YEAR, value);
                        }
                        if (nextExecute.getTimeInMillis() > schedule.getRepeatValidTillDate().getTimeInMillis()) {
                            schedule.set_state("completed");
                        } else {
                            schedule.setNextExecute(nextExecute);
                        }
                        DatabaseHelper.getInstance().getHelper(ScheduleHelper.class).createOrUpdate(schedule);
                    }
                }
            }else if(schedule.getNextExecute().getTimeInMillis() <= calendar.getTimeInMillis()) {
                //single execution
                createMessageFromSchedule(schedule);
                schedule.set_state("completed");
                DatabaseHelper.getInstance().getHelper(ScheduleHelper.class).createOrUpdate(schedule);
            }
        }
    }

    public void removeHistoryMessages(){
        List<Message> messages = (List<Message>)(List<?>)findBy("_state", "sent");

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Calendar limitTime = Calendar.getInstance();
        String val = settings.getString("history_till_list", "M-1");
        String[] valArray = val.split("-");
        int valInt = Integer.parseInt(valArray[1]);
        if(valArray[0].equalsIgnoreCase("D")){
            limitTime.add(Calendar.DATE, valInt);
        }else if(valArray[0].equalsIgnoreCase("M")){
            limitTime.add(Calendar.MONTH, valInt);
        }else if(valArray[0].equalsIgnoreCase("Y")){
            limitTime.add(Calendar.YEAR, valInt);
        }
        for (int i = 0; i < messages.size(); i++) {
            if(messages.get(i).getExecuted().getTimeInMillis() > limitTime.getTimeInMillis()){
                delete(messages.get(i).get_id());
            }
        }
    }

    private void createMessageFromSchedule(Schedule schedule){
        Message message = new Message();
        message.set_state("ready");
        message.setScheduleId(schedule.get_id());
        message.setMessage(schedule.getMessage());
        message.setReceivers(schedule.getReceivers());
        create(message);
    }

    public List<Message> getMessagesFromQueue(){
        List<Message> messages = (List<Message>)(List<?>)findBy("_state", "ready");
        return messages;
    }

    public void markAsSent(Message message){
        message.set_state("sent");
        message.setExecuted(Calendar.getInstance());
        this.createOrUpdate(message);
    }

    public void markAsFailed(Message message){
        message.set_state("failed");
        message.setExecuted(Calendar.getInstance());
        this.createOrUpdate(message);
    }
}
